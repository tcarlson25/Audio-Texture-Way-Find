package edu.tamu.github.audiotextilewayfind;

import java.util.Arrays;

import static edu.tamu.github.audiotextilewayfind.VerbalQueues.Nodes;
import static java.sql.Types.NULL;

/**
 * Created by JeffCor on 2/18/2017.
 */

public class WhereButtonHandle {
    private Path previousPath;
    private Intersection previousIntersection;

    WhereButtonHandle (){
        previousPath = new Path (Nodes, "");
        previousIntersection = new Intersection("", Arrays.asList(previousPath));
    }

    public void setPreviousPath(Path previousPath) {
//        this.previousPath.pathType = previousPath.pathType;
//        this.previousPath.directDestination = previousPath.directDestination;
        this.previousPath = previousPath;
    }

    public void setPreviousIntersection(Intersection previousIntersection) {
        this.previousIntersection = previousIntersection;
    }

    public Path getPreviousPath() {
        return previousPath;
    }

    public Intersection getPreviousIntersection() {
        return previousIntersection;
    }
}
