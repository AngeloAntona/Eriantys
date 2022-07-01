package it.polimi.ingsw.am54;

import it.polimi.ingsw.am54.network.Client;
import it.polimi.ingsw.am54.network.Server;

public class Eriantys {
    public static void main(String[] args) {
        if(args.length >= 1){
            if(args[0].equals("--server")){
                //to se if we pass
                System.out.println("arg[1](port): "+ Integer.parseInt(args[1]));
                Server.main(args);
            }
            if(args[0].equals("--client")){
                Client.main(new String[]{args[1]});
            }
        }
    }
}
