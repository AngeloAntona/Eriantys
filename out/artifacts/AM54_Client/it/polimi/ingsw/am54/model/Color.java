package it.polimi.ingsw.am54.model;
/**
 * Enumerator Color is used to represent students, because the only important property of every student is its color.<br>
 * It is also used to set color of professors.<br>
 * The method toString is overridden to make its later use easier.
 */
public enum Color {

    /**
     * Yellow students/professor.
     */
    YELLOW{
        @Override
        public String toString() {
            return "YELLOW";
        }
    },
    /**
     * Blue students/professor.
     */
    BLUE{
        @Override
        public String toString() {
            return "BLUE";
        }
    },
    /**
     * Green students/professor.
     */
    GREEN{
        @Override
        public String toString() {
            return "GREEN";
        }
    },
    /**
     * Red students/professor.
     */
    RED{
        @Override
        public String toString() {
            return "RED";
        }
    },
    /**
     * Pink students/professor.
     */
    PINK{
        @Override
        public String toString() {
            return "PINK";
        }
    }
}
