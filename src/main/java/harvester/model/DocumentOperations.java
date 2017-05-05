package harvester.model;

import harvester.dataObjects.Contributor;
import harvester.dataObjects.Creator;
import harvester.dataObjects.Document;
import interfaces.IContributor;
import interfaces.ICreator;
import interfaces.IDocument;

import java.sql.*;
import java.util.ArrayList;

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
    private PreparedStatement stGetDocuments;
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
        stGetDocuments = con.prepareStatement("SELECT * FROM UDLAPOAI.DOCUMENT");
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
            if(doc.getDescription()==null){
                stAddDocument.setNull(4, Types.VARCHAR);
            } else {
                stAddDocument.setString(4, doc.getDescription());
            }
            stAddDocument.setString(5, doc.getPublisher());
            stAddDocument.setDate(6, doc.getDate());
            stAddDocument.setString(7, doc.getLanguage());

            stAddDocument.executeUpdate();

            if(doc.getCreators()!=null) {
                for (ICreator creator : doc.getCreators()) {
                    stAddCreator.setString(1, doc.getDocumentID());
                    stAddCreator.setString(2, creator.getName());
                    stAddCreator.setString(3, creator.getCareer());
                    stAddCreator.setString(4, creator.getDegree());

                    stAddCreator.executeUpdate();
                }
            }

            if(doc.getContributors()!=null) {
                for (IContributor contributor : doc.getContributors()) {
                    stAddContributor.setString(1, doc.getDocumentID());
                    stAddContributor.setString(2, contributor.getName());

                    stAddContributor.executeUpdate();
                }
            }

            if(doc.getTypes()!=null) {
                for (String type : doc.getTypes()) {
                    stAddDocumentType.setString(1, doc.getDocumentID());
                    stAddDocumentType.setString(2, type);

                    stAddDocumentType.executeUpdate();
                }
            }

            if(doc.getFormats()!=null) {
                for (String format : doc.getFormats()) {
                    stAddDocumentFormat.setString(1, doc.getDocumentID());
                    stAddDocumentFormat.setString(2, format);

                    stAddDocumentFormat.executeUpdate();
                }
            }

            return true;
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
        try{
            stGetDocument.setString(1, documentID);
            ResultSet documentRS = stGetDocument.executeQuery();

            while(documentRS.next()){
                Document document = new Document(documentID);
                document.setLocationURL(documentRS.getString("locationURL"));
                document.setTitle(documentRS.getString("title"));
                document.setDescription(documentRS.getString("description"));
                document.setPublisher(documentRS.getString("publisher"));
                document.setDate(documentRS.getDate("date"));
                document.setLanguage(documentRS.getString("language"));

                //Retrieve the document types
                stGetDocumentType.setString(1, documentID);
                ResultSet typeRS = stGetDocumentType.executeQuery();

                while (typeRS.next()){
                    document.addType(typeRS.getString("documentType"));
                }

                //Retrieve the document formats
                stGetDocumentFormat.setString(1, documentID);
                ResultSet formatRS = stGetDocumentFormat.executeQuery();

                while (formatRS.next()){
                    document.addFormat(documentRS.getString("documentFormat"));
                }

                //Retrieve contributors
                stGetContributor.setString(1, documentID);
                ResultSet contributorRS = stGetContributor.executeQuery();

                while (contributorRS.next()){
                    document.addContributor(
                            new Contributor(contributorRS.getString("contributorName"))
                    );
                }

                //Retrieve cretors
                stGetCreator.setString(1, documentID);
                ResultSet creatorsRS = stGetCreator.executeQuery();

                while (creatorsRS.next()){
                    document.addCreator(
                            new Creator(
                                    creatorsRS.getString("creatorName"),
                                    creatorsRS.getString("career"),
                                    creatorsRS.getString("degree")
                            )
                    );
                }

                return document;
            }

        } catch(SQLException e){
            e.printStackTrace();
        }

        return null;
    }

    public ArrayList<IDocument> getDocuments() {
        try{
            ResultSet documentRS = stGetDocuments.executeQuery();

            ArrayList<IDocument> documents = new ArrayList<>();

            while(documentRS.next()){
                Document document = new Document(documentRS.getString("documentID"));
                document.setLocationURL(documentRS.getString("locationURL"));
                document.setTitle(documentRS.getString("title"));
                document.setDescription(documentRS.getString("description"));
                document.setPublisher(documentRS.getString("publisher"));
                document.setDate(documentRS.getDate("date"));
                document.setLanguage(documentRS.getString("language"));

                //Retrieve the document types
                stGetDocumentType.setString(1, document.getDocumentID());
                ResultSet typeRS = stGetDocumentType.executeQuery();

                while (typeRS.next()){
                    document.addType(typeRS.getString("documentType"));
                }

                //Retrieve the document formats
                stGetDocumentFormat.setString(1, document.getDocumentID());
                ResultSet formatRS = stGetDocumentFormat.executeQuery();

                while (formatRS.next()){
                    document.addFormat(documentRS.getString("documentFormat"));
                }

                //Retrieve contributors
                stGetContributor.setString(1, document.getDocumentID());
                ResultSet contributorRS = stGetContributor.executeQuery();

                while (contributorRS.next()){
                    document.addContributor(
                            new Contributor(contributorRS.getString("contributorName"))
                    );
                }

                //Retrieve cretors
                stGetCreator.setString(1, document.getDocumentID());
                ResultSet creatorsRS = stGetCreator.executeQuery();

                while (creatorsRS.next()){
                    document.addCreator(
                            new Creator(
                                    creatorsRS.getString("creatorName"),
                                    creatorsRS.getString("career"),
                                    creatorsRS.getString("degree")
                            )
                    );
                }

                documents.add(document);
            }

            return documents;

        } catch(SQLException e){
            e.printStackTrace();
        }

        return null;
    }
}
