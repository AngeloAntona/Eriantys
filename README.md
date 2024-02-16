# Software Engineering Final Project - Evaluation of 30 cum laude.

This repository hosts the implementation of the board game [Eriantys](https://craniointernational.com/products/eriantys/), developed as the final project for the Software Engineering course of Politecnico di Milano in the academic year 2021-2022. Our software allows for hosting and managing Eriantys games through a server that handles game logic and supports 2 to 4 clients per game. Clients can connect to the server via sockets and have the option to interact with the game using either a Command Line Interface (CLI) or a Graphical User Interface (GUI), implementing the Model-View-Controller (MVC) design pattern.

![Eriantys Game Cover](https://shop.asmodee.com/product/image/large/cc292-1.jpg "Eriantys Game Cover")

## Features

- **Game Server**: Hosts and manages game sessions.
- **Client Support**: Supports 2-4 players per game session.
- **User Interfaces**: Offers both CLI and GUI for game interaction.
- **Design Pattern**: Utilizes the MVC pattern for clear separation of concerns.
- **Communication**: Uses sockets for efficient client-server communication.

## Documentation

Explore our project documentation to understand the architecture and communication protocols:

- [Initial UML Diagram](https://github.com/AngeloAntona/ingsw2022-AM54/blob/main/Deliveries/UML/UML%201.0.pdf)
- [Final UML Diagram](https://github.com/AngeloAntona/ingsw2022-AM54/blob/main/Deliveries/UML/UML_final.pdf)
- [Communication Protocol](https://github.com/AngeloAntona/ingsw2022-AM54/blob/main/Deliveries/Communication_protocol/Communication%20protocol%25a%20AM54%25a.pdf)
- Peer Reviews:
  - [UML](https://github.com/AngeloAntona/ingsw2022-AM54/blob/main/Deliveries/Peer_Review/Peer%20Review%20UML.pdf)
  - [Communication Protocol](https://github.com/AngeloAntona/ingsw2022-AM54/blob/main/Deliveries/Peer_Review/Peer%20Review%20Communication.PDF)

## Project Status

Our project implements all the core features necessary for a complete gaming experience:

| Feature                | Status          |
| ---------------------- |:---------------:|
| Basic Rules            | ✔️ Implemented  | 
| Complete Rules         | ✔️ Implemented  |
| CLI                    | ✔️ Implemented  |
| GUI                    | ✔️ Implemented  |
| Socket Communication   | ✔️ Implemented  |
| All Personality Cards  | ✔️ Implemented  |
| Game for 4 Players     | ✔️ Implemented  |
| Multiple Game Sessions | ✔️ Implemented  |

### Legend
- ✔️ Implemented
- 🔶 In Progress
- ❌ Not Implemented

## Getting Started

### Server Setup

Run the server using the following command in your terminal or command prompt:

```bash
java -jar EriantysAM54.jar --server [portnumber]
```
### Client Setup
To start a client session, use:
- For GUI:
```bash
java -jar EriantysAM54.jar --client
```

- For CLI:
```bash
java -jar EriantysAM54.jar --client cli
```
### Download
You can download the latest release of the game from [here](https://drive.google.com/file/d/1s4VOuXkdRfnDrYwXwimRt0yCjt4QD_9N/view?usp=drive_link).

Note: The JAR file is too large to be hosted directly on GitHub.
