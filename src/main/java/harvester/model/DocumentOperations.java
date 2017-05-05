package harvester.model;

import harvester.dataObjects.Creator;
import interfaces.IContributor;
import interfaces.ICreator;
import interfaces.IDocument;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Thagus on 30/04/17.
 *
 * Class to manage simple CRUD operations for Documents
 */
public class DocumentOperations {
    private PreparedStatement stAddDocument;
    private PreparedStatement stAddCreator;
    private PreparedStatement stAddContributor;
    private PreparedStatement stAddDocumentType;
    private PreparedStatement stAddDocumentFormat;

    private PreparedStatement stDeleteDocument;

    private PreparedStatement stGetDocument;
    private PreparedStatement stGetCreator;
    private PreparedStatement stGetContributor;
    private PreparedStatement stGetDocumentType;
    private PreparedStatement stGetDocumentFormat;

    private PreparedStatement stCountDocuments;

    public DocumentOperations(Connection con) throws SQLException {
        stAddDocument = con.prepareStatement("INSERT INTO UDLAPOAI.DOCUMENT(documentID,locationURL,title,description,publisher,date,language) VALUES(?,?,?,?,?,?,?)");
        stAddCreator = con.prepareStatement("INSERT INTO UDLAPOAI.CREATOR(documentID,creatorName,career,degree) VALUES(?,?,?,?)");
        stAddContributor = con.prepareStatement("INSERT INTO UDLAPOAI.CONTRIBUTOR(documentID,contributorName) VALUES(?,?)");
        stAddDocumentType = con.prepareStatement("INSERT INTO UDLAPOAI.DOCUMENTTYPE(documentID,documentType) VALUES(?,?)");
        stAddDocumentFormat = con.prepareStatement("INSERT INTO UDLAPOAI.DOCUMENTFORMAT(documentID,documentFormat) VALUES(?,?)");

        stDeleteDocument = con.prepareStatement("DELETE FROM UDLAPOAI.DOCUMENT WHERE documentID=?");

        stGetDocument = con.prepareStatement("SELECT * FROM UDLAPOAI.DOCUMENT WHERE documentID=?");
        stGetCreator = con.prepareStatement("SELECT * FROM UDLAPOAI.CREATOR WHERE documentID=?");
        stGetContributor = con.prepareStatement("SELECT * FROM UDLAPOAI.CONTRIBUTOR WHERE documentID=?");
        stGetDocumentType = con.prepareStatement("SELECT * FROM UDLAPOAI.DOCUMENTTYPE WHERE documentID=?");
        stGetDocumentFormat = con.prepareStatement("SELECT * FROM UDLAPOAI.DOCUMENTFORMAT WHERE documentID=?");

        stCountDocuments = con.prepareStatement("SELECT COUNT(*) FROM UDLAPOAI.DOCUMENT");
    }

    /**
     * Counts the total number of documents in teh database
     * @return the numbe of documents
     */
    public int countDocuments(){
        try {
            ResultSet rs = stCountDocuments.executeQuery();

            int count = 0;

            while(rs.next()){
                count = rs.getInt(1);
            }
            rs.close();

            if(count<=0){
                System.out.println("There are no documents");
            }

            return count;
        } catch(SQLException e){
            e.printStackTrace();
        }
        return 0;
    }

    public boolean addDocument(IDocument doc) {
        try {
            stAddDocument.clearParameters();

            stAddDocument.setString(1, doc.getDocumentID());
            stAddDocument.setString(2, doc.getLocationURL());
            stAddDocument.setString(3, doc.getTitle());
            stAddDocument.setString(4, doc.getDescription());
            stAddDocument.setString(5, doc.getPublisher());
            stAddDocument.setDate(6, doc.getDate());
            stAddDocument.setString(7, doc.getLanguage());

            stAddDocument.executeUpdate();

            for(ICreator creator : doc.getCreators()){
                stAddCreator.setString(1, doc.getDocumentID());
                stAddCreator.setString(2, creator.getName());
                stAddCreator.setString(3, creator.getCareer());
                stAddCreator.setString(4, creator.getDegree());

                stAddCreator.executeUpdate();
            }

            for(IContributor contributor : doc.getContributors()){
                stAddContributor.setString(1, doc.getDocumentID());
                stAddContributor.setString(2, contributor.getName());

                stAddContributor.executeUpdate();
            }

            for(String type : doc.getTypes()){
                stAddDocumentType.setString(1, doc.getDocumentID());
                stAddDocumentType.setString(2, type);

                stAddDocumentType.executeUpdate();
            }

            for(String format : doc.getFormats()){
                stAddDocumentFormat.setString(1, doc.getDocumentID());
                stAddDocumentFormat.setString(2, format);

                stAddDocumentFormat.executeUpdate();
            }
        } catch(SQLException e){
            //The insertion fails due to duplicate key
            if(e.getErrorCode()==23505){
                System.out.println("There is already a document with id: " + doc.getDocumentID());
                return false;
            }
            else {
                e.printStackTrace();
            }
        }
        return false;
    }

    public IDocument getDocument(String documentID){

        return null;
    }
}
