package harvester.model;

import harvester.dataObjects.Contributor;
import harvester.dataObjects.Creator;
import harvester.dataObjects.Document;
import org.dom4j.Element;
import se.kb.oai.OAIException;
import se.kb.oai.pmh.Header;
import se.kb.oai.pmh.IdentifiersList;
import se.kb.oai.pmh.OaiPmhServer;
import se.kb.oai.pmh.Record;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

/**
 * Created by Thagus on 08/12/16.
 */
public class DCHarvester {
    private ModelDatabase db;
    private DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    public DCHarvester(){
        db = ModelDatabase.instance();
    }

    /**
     * Method to harvest an OAI-PMH collection that follows the Dublin Core standard
     * @param repository the URL of the OAI-PMH collection
     */
    public void harvest(String repository) throws OAIException, IOException {
        OaiPmhServer server = new OaiPmhServer(repository);

        //To list all identifiers in the repository that has "oai_dc" metadata
        IdentifiersList list = server.listIdentifiers("oai_dc");
        boolean more = true;
        while (more) {
            for (Header header : list.asList()) {
                //Read the record of the given identifier
                Record record = server.getRecord(header.getIdentifier(), "oai_dc");

                Document document = new Document(header.getIdentifier());

                ArrayList<String> careers = new ArrayList<>();
                ArrayList<String> academicDegrees = new ArrayList<>();
                ArrayList<String> creatorNames = new ArrayList<>();
                ArrayList<String> contributorNames = new ArrayList<>();

                //Now use the dom4j to handle the metadata
                Element root = record.getMetadata();
                //Iterate through every element withing the record to obtain the metadata
                for (Iterator i = root.elementIterator(); i.hasNext(); ) {
                    Element element = (Element) i.next();

                    String elementName = element.getName();
                    String elementText = element.getText();

                    if(elementText.length()>0) {
                        switch (elementName) {
                            case "identifier":
                                document.setLocationURL(elementText);
                                break;
                            case "title":
                                document.setTitle(elementText);
                                break;
                            case "type":
                                document.addType(elementText);
                                break;
                            case "subject": //Career of Creator
                                careers.add(elementText);
                                break;
                            case "creator": //Name of Creator
                                creatorNames.add(elementText);
                                break;
                            case "contributor": //Name of Contributor
                                contributorNames.add(elementText);
                                break;
                            case "description":
                                document.setDescription(elementText);
                                break;
                            case "publisher":
                                document.setPublisher(elementText);
                                break;
                            case "format":
                                document.addFormat(elementText);
                                break;
                            case "date":
                                try {
                                    document.setDate(df.parse(elementText));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case "coverage":    //Academic degree of Creator
                                academicDegrees.add(elementText);
                                break;
                            case "language":
                                document.setLanguage(elementText);
                                break;
                            default:
                                System.out.println("Metadata not recognized: " + elementName + " \"" + elementText + "\"");
                                break;
                        }
                    }
                }

                for(int i=0; i<creatorNames.size(); i++){
                    Creator creator = new Creator(creatorNames.get(i), careers.get(i), academicDegrees.get(i));
                    document.addCreator(creator);
                }

                for(String name : contributorNames){
                    Contributor contributor = new Contributor(name);
                    document.addContributor(contributor);
                }

                feedDatabase(document);
            }
            if (list.getResumptionToken() != null)
                list = server.listIdentifiers(list.getResumptionToken());
            else
                more = false;
        }
    }

    /**
     * Adds the document to the database
     *
     * @param doc The document to be added
     */
    private synchronized void feedDatabase(Document doc){
        if(doc.getLocationURL()!=null && doc.getLocationURL().length()>0) {
            boolean insertCheck = db.docOps.addDocument(doc);
            if(!insertCheck){
                System.out.println("Failed to add: " + doc.getIdentifier());
            }
        }
    }
}
