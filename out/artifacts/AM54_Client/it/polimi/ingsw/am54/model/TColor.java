package it.polimi.ingsw.am54.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


/**
 * Enumerator that contains possible colors of towers
 */
public enum TColor  implements Serializable {

    /**
     * Used black towers.
     */
    @SerializedName("BLACK")
    BLACK {
        @Override
        public String toString() {
            return "BLACK";
        }
    },
    /**
     * Used for gray towers.
     */
    @SerializedName("GREY")
    GREY {
        @Override
        public String toString() {
            return "GREY";
        }
    },
    /**
     * Used for with towers.
     */
    @SerializedName("WHITE")
    WHITE {
        @Override
        public String toString() {
            return "WHITE";
        }
    }

}
