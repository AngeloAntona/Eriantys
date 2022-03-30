package it.polimi.ingsw.am54.model;

public enum Color {

    /* I override the toString method to make it more convenient to use later */
    YELLOW{
        @Override
        public String toString() {
            return "YELLOW";
        }
    },
    BLUE{
        @Override
        public String toString() {
            return "BLUE";
        }
    },
    GREEN{
        @Override
        public String toString() {
            return "GREEN";
        }
    },
    RED{
        @Override
        public String toString() {
            return "RED";
        }
    },
    PINK{
        @Override
        public String toString() {
            return "PINK";
        }
    }
}
