package harvester.model;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Thagus on 30/04/17.
 */
public class ModelDatabase {
    private static ModelDatabase uniqueInstance;
    private Statement st;
    private Connection con;

    public ModelOperations dbOps;
    public DocumentOperations docOps;

    /**
     * Singleton instancing
     * @return The single instance of the class
     */
    public static synchronized ModelDatabase instance(){
        if (uniqueInstance==null) {
            uniqueInstance = new ModelDatabase();   //Create connection to the database
            uniqueInstance.createOperations();      //Create operations
            return uniqueInstance;
        }  else {
            return uniqueInstance;
        }
    }

    private ModelDatabase() {
        try {
            Class.forName("org.h2.Driver");

            con = DriverManager.getConnection("jdbc:h2:./database/udlap_oai", "udlap", "udlap");
            st = con.createStatement();

            //Create the schema and tables if they don't already exists
            createSchema();
            createTables();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.toString(), "Error, database driver not found", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.toString(), "Error creating connection to database. Please, restart the program and ensure that there's no other instance running", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Create the operations objects to handle requests
     */
    private void createOperations(){
        try {
            docOps = new DocumentOperations(con);
            dbOps = new ModelOperations(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create database schema
     */
    private void createSchema(){
        try {
            st.execute("CREATE SCHEMA UDLAPOAI");
        } catch (SQLException e) {
            //System.out.println("Error creating schema:");
            //e.printStackTrace();
        }
    }

    /**
     * Create database tables
     */
    public void createTables(){
        //Create Documents table
        try {
            st.execute("CREATE TABLE UDLAPOAI.DOCUMENT(" +
                    "documentID VARCHAR NOT NULL," +
                    "locationURL VARCHAR NOT NULL," +
                    "title VARCHAR NOT NULL," +
                    "description VARCHAR," +
                    "publisher VARCHAR NOT NULL," +
                    "date DATE NOT NULL," +
                    "language CHAR(2) NOT NULL," +         //Detected language has 2 letter
                    "PRIMARY KEY (documentID)" +
                    ")");
        } catch (SQLException e) {
            //e.printStackTrace();
        }

        //Create DocumentType table
        try {
            st.execute("CREATE TABLE UDLAPOAI.DOCUMENTTYPE(" +
                    "documentID VARCHAR NOT NULL," +
                    "documentType VARCHAR NOT NULL," +
                    "FOREIGN KEY(documentID) REFERENCES DOCUMENT(documentID) ON DELETE CASCADE," +
                    "PRIMARY KEY (documentID,documentType)" +
                    ")");
        } catch (SQLException e) {
            //e.printStackTrace();
        }

        //Create DocumentFormat table
        try {
            st.execute("CREATE TABLE UDLAPOAI.DOCUMENTFORMAT(" +
                    "documentID VARCHAR NOT NULL," +
                    "documentFormat VARCHAR NOT NULL," +
                    "FOREIGN KEY(documentID) REFERENCES DOCUMENT(documentID) ON DELETE CASCADE," +
                    "PRIMARY KEY (documentID,documentFormat)" +
                    ")");
        } catch (SQLException e) {
            //e.printStackTrace();
        }

        //Create Terms table
        try {
            st.execute("CREATE TABLE UDLAPOAI.CREATOR(" +
                    "documentID VARCHAR NOT NULL," +
                    "creatorName VARCHAR NOT NULL," +
                    "career VARCHAR NOT NULL," +
                    "degree VARCHAR NOT NULL," +
                    "FOREIGN KEY(documentID) REFERENCES DOCUMENT(documentID) ON DELETE CASCADE," +
                    "PRIMARY KEY (documentID,creatorName)" +
                    ")");
        } catch (SQLException e) {
            //e.printStackTrace();
        }

        //Create Terms table
        try {
            st.execute("CREATE TABLE UDLAPOAI.CONTRIBUTOR(" +
                    "documentID VARCHAR NOT NULL," +
                    "contributorName VARCHAR NOT NULL," +
                    "FOREIGN KEY(documentID) REFERENCES DOCUMENT(documentID) ON DELETE CASCADE," +
                    "PRIMARY KEY (documentID,contributorName)" +
                    ")");
        } catch (SQLException e) {
            //e.printStackTrace();
        }
    }

    /**
     * Delete the database tables
     */
    public void clearDB(){
        try {
            st.execute("DROP TABLE UDLAPOAI.DOCUMENT");
            st.execute("DROP TABLE UDLAPOAI.DOCUMENTTYPE");
            st.execute("DROP TABLE UDLAPOAI.DOCUMENTFORMAT");
            st.execute("DROP TABLE UDLAPOAI.CREATOR");
            st.execute("DROP TABLE UDLAPOAI.CONTRIBUTOR");
        } catch (SQLException e) {
            //System.out.println("Error cleaning DB:");
            //e.printStackTrace();
        }
    }

    /**
     * Close the connection to the database
     */
    public void close() throws SQLException {
        st.close();
        con.close();
    }
}
