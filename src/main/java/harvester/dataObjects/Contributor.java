package harvester.dataObjects;

import interfaces.IContributor;

/**
 * Created by Thagus on 30/04/17.
 */
public class Contributor implements IContributor {
    private String name;

    public Contributor(String name) {
        this.name = name;
    }

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}
}
