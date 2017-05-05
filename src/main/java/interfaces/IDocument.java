package interfaces;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Thagus on 30/04/17.
 */
public interface IDocument {
    String getIdentifier();
    String getLocationURL();
    String getTitle();
    String getDescription();
    Date getDate();
    String getLanguage();
    String getPublisher();
    ArrayList<String> getTypes();
    ArrayList<String> getFormats();

    ArrayList<ICreator> getCreators();
    ArrayList<IContributor> getContributors();


    void setIdentifier(String identifier);
    void setLocationURL(String locationURL);
    void setTitle(String title);
    void setDescription(String description);
    void setDate(Date date);
    void setLanguage(String language);
    void setPublisher(String publisher);
    void setTypes(ArrayList<String> types);
    void addType(String type);
    void setFormats(ArrayList<String> formats);
    void addFormat(String format);

    void setCreators(ArrayList<ICreator> creators);
    void addCreator(ICreator creator);
    void setContributors(ArrayList<IContributor> contributors);
    void addContributor(IContributor contributor);

}
