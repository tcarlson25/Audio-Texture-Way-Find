package edu.tamu.github.audiotextilewayfind;

import java.util.List;

/**
 * Created by JeffCor on 2/18/2017.
 */

public class Intersection {
    /*
    * Data Members:
    * 1) List of Paths
    * 2) Name of Intersection*/

    public List<Path> paths;   //List of Paths that extend from the intersection
    public String name;        //Name of the intersection on a map

    //Constructor
    public Intersection (String name, List<Path> paths){
        this.name   = name;
        this.paths  = paths;
    }

    //Copy Constructor
    public Intersection (Intersection newIntersection){
        this.paths  = newIntersection.paths;
        this.name   = newIntersection.name;
    }

    @Override
    public String toString() {
        return name;
    }
}
