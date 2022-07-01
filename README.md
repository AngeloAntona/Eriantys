# Prova Finale di Ingegneria del Software - AA 2021-2022
Implementation of the game [Eriantys](https://craniointernational.com/products/eriantys/).
The project allows users to create a server which will host one or multiple games, and will be in charge of game logic. Besides that the project contains also client side (number of clients per game are from 2 to 4) which communicates with the server using sockets.
The game is implemented using the MVC (Model-View-Controller) patter and allows players to use either command line (CLI) or graphical interface (GUI).

![alt text](https://shop.asmodee.com/product/image/large/cc292-1.jpg "Game Cover")


## Documentation 
UML diagramm:
* [Initial UML](https://github.com/AngeloAntona/ingsw2022-AM54/blob/main/Deliveries/UML/UML%201.0.pdf)
* [Final UML](https://github.com/AngeloAntona/ingsw2022-AM54/blob/main/Deliveries/UML/UML_final.pdf)
* [Communication Protocol](https://github.com/AngeloAntona/ingsw2022-AM54/blob/main/Deliveries/Communication_protocol/Communication%20protocol%25a%20AM54%25a.pdf)
* Peer Reviews:
  * [UML](https://github.com/AngeloAntona/ingsw2022-AM54/blob/main/Deliveries/Peer_Review/Peer%20Review%20UML.pdf)
  * [Communication Protocol](https://github.com/AngeloAntona/ingsw2022-AM54/blob/main/Deliveries/Peer_Review/Peer%20Review%20Communication.PDF)


## Status
| Functionality         | State           |
| --------------------- |:---------------:|
| Basic Rules           | :green_circle:  | 
| Complete Rules        | :green_circle:  |
| CLI                   | :green_circle:  |
| GUI                   | :green_circle:  |
| Socket                | :green_circle:  |
| All Personality Cards | :green_circle:  |
| Game for 4 players    | :green_circle:  |
| Multiple games        | :green_circle:  |

### Legend
- :green_circle: Implemented
- :yellow_circle: Implementing
- :red_circle: Not Implemented


## Running
Both Serer and Client sides are implemented in singl jar file called EriantysAM54.jar
### Server
To run Server enter next line in terminal/cmd:

`java -jar EriantysAM54.jar --server [portnumber]`
### Client
To run Client:
  * with graphical interface: 
   `java -jar EriantysAM54.jar --client`
 
  * without graphical interface: 
  `java -jar EriantysAM54.jar --client cli`

## Download
[JAR](https://polimi365-my.sharepoint.com/:f:/g/personal/10692719_polimi_it/EoW1IBvM7HxDoKL3YCAny-gBKLlXHj_VfuvMQQmV_JEOlQ?e=2HhAj4)

NOTE: File too big for github
## Members of group
| Name                                                  | Email                         |
| ----------------------------------------------------- |:-----------------------------:|
| [Angelo Antona](https://github.com/AngeloAntona)      | angelo.antona@mail.polimi.it  | 
| [Rocco Brunelli](https://github.com/RoccoBrunelli)    | rocco.brunelli@mail.polimi.it |
| [Vukašin Berić](https://github.com/vberic)            | vukasin.beric@mail.polimi.it  |
