package harvester.dataObjects;

import interfaces.IContributor;
import interfaces.ICreator;
import interfaces.IDocument;

import java.util.ArrayList;
import java.sql.Date;

/**
 * Created by Thagus on 30/04/17.
 */
public class Document implements IDocument {
    private String documentID;
    private String locationURL;
    private String title;
    private String description;
    private Date date;
    private String language;
    private String publisher;
    private ArrayList<String> types;
    private ArrayList<String> formats;
    private ArrayList<ICreator> creators;
    private ArrayList<IContributor> contributors;

    public Document(String documentID){
        this.documentID = documentID;
    }

    /** GETTERS */
    public String getDocumentID() {return documentID;}
    public String getLocationURL() {return locationURL;}
    public String getTitle() {return title;}
    public String getDescription() {return description;}
    public Date getDate() {return date;}
    public String getLanguage() {return language;}
    public String getPublisher() {return publisher;}
    public ArrayList<String> getTypes() {return types;}
    public ArrayList<String> getFormats() {return formats;}
    public ArrayList<ICreator> getCreators() {return creators;}
    public ArrayList<IContributor> getContributors() {return contributors;}

    /** SETTERS */
    public void setDocumentID(String documentID) {this.documentID = documentID;}
    public void setLocationURL(String locationURL) {this.locationURL = locationURL;}
    public void setTitle(String title) {this.title = title;}
    public void setDescription(String description) {this.description = description;}
    public void setDate(Date date) {this.date = date;}
    public void setLanguage(String language) {this.language = language;}
    public void setPublisher(String publisher) {this.publisher = publisher;}
    public void setTypes(ArrayList<String> types) {this.types = types;}
    public void setFormats(ArrayList<String> formats) {this.formats = formats;}
    public void setCreators(ArrayList<ICreator> creators) {this.creators = creators;}
    public void setContributors(ArrayList<IContributor> contributors) {this.contributors = contributors;}

    public void addType(String type) {
        if(types == null){
            types = new ArrayList<>();
        }

        types.add(type);
    }

    public void addFormat(String format) {
        if(formats == null){
            formats = new ArrayList<>();
        }

        formats.add(format);
    }

    public void addCreator(ICreator creator) {
        if(creators == null){
            creators = new ArrayList<>();
        }

        creators.add(creator);
    }

    public void addContributor(IContributor contributor) {
        if(contributors == null){
            contributors = new ArrayList<>();
        }

        contributors.add(contributor);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
