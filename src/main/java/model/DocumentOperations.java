package model;

import dataObjects.Document;
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
    private PreparedStatement stDeleteDocument;
    private PreparedStatement stGetDocument;
    private PreparedStatement stCountDocuments;

    public DocumentOperations(Connection con) throws SQLException {
        stAddDocument = con.prepareStatement("");
        stDeleteDocument = con.prepareStatement("");
        stGetDocument = con.prepareStatement("");
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

        return false;
    }
}
