package harvester.controller;

import harvester.model.DCHarvester;
import harvester.model.ModelDatabase;

import java.util.ArrayList;

/**
 * Created by Thagus on 30/04/17.
 */
public class ControllerHarvest {
    public static void main(String[] args){
        ModelDatabase db = ModelDatabase.instance();
        ArrayList<String> seeds = new ArrayList<>();
        DCHarvester dcHarvester = new DCHarvester();

        seeds.add("http://catarina.udlap.mx/tales/oai/");

        try {
            for(String site : seeds)
                dcHarvester.harvest(site);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //A message to alert the user about the number of read documents
        System.out.println("Read " + db.docOps.countDocuments() + " documents");
    }
}
