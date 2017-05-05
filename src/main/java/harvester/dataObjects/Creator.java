package harvester.dataObjects;

import interfaces.ICreator;

/**
 * Created by Thagus on 30/04/17.
 */
public class Creator implements ICreator {
    private String name;
    private String career;
    private String degree;

    public Creator(String name, String career, String degree) {
        this.name = name;
        this.career = career;
        this.degree = degree;
    }

    /** GETTERS */
    public String getName() {return name;}
    public String getCareer() {return career;}
    public String getDegree() {return degree;}

    /** SETTERS */
    public void setName(String name) {this.name = name;}
    public void setCareer(String career) {this.career = career;}
    public void setDegree(String degree) {this.degree = degree;}
}
