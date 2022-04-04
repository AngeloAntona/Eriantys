package it.polimi.ingsw.am54.model;

/**
 * Enumerator that contains possible colors of towers
 */
public enum TColor {
    /**
     * Used black towers
     */
    BLACK{
        @Override
        public String toString() {
            return "BLACK";
        }
    },
    /**
     * Used for gray towers
     */
    GRAY{
        @Override
        public String toString() {
            return "GRAY";
        }
    },
    /**
     * Used for with towers
     */
    WHITE{
        @Override
        public String toString() {
            return "WHITE";
        }
    }
}
