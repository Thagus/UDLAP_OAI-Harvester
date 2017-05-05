import harvester.Facade;
import harvester.dataObjects.Contributor;
import interfaces.IContributor;
import interfaces.ICreator;
import interfaces.IDocument;

/**
 * Created by Thagus on 05/05/17.
 */
public class Main {
    private static Facade facade;

    public static void main(String[] args){
        facade = new Facade();

        //facade.doHarvest();

        displayDocuments();
    }

    private static void displayDocuments(){
        for(IDocument document : facade.getDocuments()){
            System.out.println(
                    "ID: " + document.getDocumentID() + "\n" +
                    "Title: " + document.getTitle() + "\n" +
                    "Description: " + document.getDescription() + "\n"+
                    "Publisher: " + document.getPublisher() + "\n"+
                    "Language: " + document.getLanguage() + "\n"+
                    "URL: " + document.getLocationURL() + "\n"+
                    "Date: " + document.getDate()
            );

            for(String type : document.getTypes()){
                System.out.print(type + " | ");
            }
            System.out.println();

            for(String format : document.getFormats()){
                System.out.print(format + " | ");
            }
            System.out.println();

            if(document.getContributors()!=null) {
                for (IContributor contributor : document.getContributors()) {
                    System.out.print(contributor.getName() + " | ");
                }
                System.out.println();
            }

            for(ICreator creator : document.getCreators()){
                System.out.println("- Creator name: " + creator.getName());
                System.out.println("- Career: " + creator.getCareer());
                System.out.println("- Degree: " + creator.getDegree());
            }
            //System.out.println();

            System.out.println("====================================");
        }
    }
}
