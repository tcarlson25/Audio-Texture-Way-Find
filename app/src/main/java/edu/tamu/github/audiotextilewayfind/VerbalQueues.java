package edu.tamu.github.audiotextilewayfind;

/**
 * Created by Tyler on 2/18/17.
 */

public enum VerbalQueues {

    EntranceExit {
        String pathName = "Study areas path";
        String pathType = "ridged path";

        public String toString() {
            return pathName + " which is a " + pathType;
        }
    },

    StudySpace {
        String pathName = "Study areas path";
        String pathType = "ridged path";

        public String toString() {
            return pathName + " which is a " + pathType;
        }
    },

    BookStacks {
        String pathName = "Book stacks path";
        String pathType = "grid path";

        public String toString() {
            return pathName + " which is a " + pathType;
        }
    },

    RestRooms {
        String pathName = "Restrooms path";
        String pathType = "honey comb path";

        public String toString() {
            return pathName + " which is a " + pathType;
        }
    },

    Elevators {
        String pathName = "Elevators path";
        String pathType = "diamond plate path";

        public String toString() {
            return pathName + " which is a " + pathType;
        }
    },

    Nodes {
        String pathName = "Intersection";
        String pathType = "circle bumb path";

        public String toString() {
            return pathName + " which is a " + pathType;
        }
    }
}