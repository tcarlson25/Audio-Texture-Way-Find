package edu.tamu.github.audiotextilewayfind;

import java.util.List;

/**
 * Created by JeffCor on 2/18/2017.
 */


public class Path {
    /*Data Members:
    * 1) Enum of Path types
    * 2) List of destination off path
    * 3) Where directly travels to*/

    public VerbalQueues pathType;

    public List<String> destinations;  //List of destinations that can be reached following the path
    // (destinations directly off the path between two intersections)
    public String directDestination;   //Most commonly accessed destination off the path

    public Path (VerbalQueues pathType, String directDestination){
        this.pathType           = pathType;
        this.directDestination  = directDestination;
    }

    //Copy Constructor
    public Path (Path newPath){
        this.pathType           = newPath.pathType;
        this.directDestination  = newPath.directDestination;
    }

    public boolean isEqual (Path comparePath){
        boolean ret = true;
        if (this.pathType != comparePath.pathType) ret = false;
        if (this.directDestination != comparePath.directDestination) ret = false;

        return ret;
    }

    @Override
    public String toString() {
        return pathType.toString();
    }
}
