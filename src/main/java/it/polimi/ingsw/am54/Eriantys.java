package it.polimi.ingsw.am54;

import it.polimi.ingsw.am54.network.Client;
import it.polimi.ingsw.am54.network.Server;

public class Eriantys {
    public static void main(String[] args) {
        if(args.length == 0){
            Client.main(new String[]{""});
        }
        if(args.length >= 1){
            if(args[0].equals("--server")){
                //System.out.println("arg[1](port): "+ Integer.parseInt(args[1]));
                Server.main(args);
            }
            if(args[0].equals("--client")){
                if(args.length > 1)
                    Client.main(new String[]{args[1]});
                else
                    Client.main(new String[]{""});
            }
        }
    }
}
