package harvester;

import harvester.model.DCHarvester;
import harvester.model.ModelDatabase;
import interfaces.IDocument;

import java.util.ArrayList;

/**
 * Created by Thagus on 05/05/17.
 */
public class Facade {
    private ModelDatabase db;
    private String SITE = "http://catarina.udlap.mx/tales/oai/requestETD.jsp";

    public Facade(){
        this.db = ModelDatabase.instance();
    }

    public void addDocument(IDocument document){
        db.docOps.addDocument(document);
    }

    public ArrayList<IDocument> getDocuments(){
        return db.docOps.getDocuments();
    }

    public IDocument getDocument(String identifier){
        return db.docOps.getDocument(identifier);
    }

    public ArrayList<IDocument> getDocumentsByTitle(String title){
        return null;
    }

    public void doHarvest(){
        DCHarvester dcHarvester = new DCHarvester();

        try {
            dcHarvester.harvest(SITE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //A message to alert the user about the number of read documents
        System.out.println("Read " + db.docOps.countDocuments() + " documents");
    }
}
