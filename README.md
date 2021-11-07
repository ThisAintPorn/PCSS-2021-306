# Introduction
Tilted Towers is a competitive game for 3 players, involving building towers by stacking towerpieces on top of each other. Each player must have the client software running on their own device, while one player must also be running the server. Since this Repo contains both the Client and the Server, these must be run as separate projects in your IDE of choice.

Tilted Towers has a Server that consists of different classes which contains functions using both networking and multithreading. This is done on a basic level in order to make a functioning game. The game will be running using a server which will be able to be joined by clients. When three clients have typed ip address and port, the game will then start.

# How to install and run the Server
1. Every player must download or clone the repository locally on your computer as a zip file, and unzip it to a folder by the same name.
2. Each player then opens and the Client folder as a project in IntelliJ or Eclipse. The host needs to open the Server folder as a project as well separately from the Client folder.
3. The host should then run the Server first before the players run the Client.
4. The players are connected to the server after they run the Client where they are required to provide the same IP address of the Server host, followed by the specific port of the Server (in this case "2345").
6. When all three players are connected to the Server and have joined with the same IP address and port number, the game will then start automatically.


# Gameplay instructions
The goal of Tilted Towers is to get the most points which is achieved by building the highest tower. It is played by three people at a time, and each user will be able to spectate the others but will only be able to interact with the middle part of the screen. **_Note: The game will not start if only one or two players join and try to play._**

The players will each start with one pre-built towerpiece where they need to place the next towerpiece. A towerpiece will be swinging on the screen, which will fall down when the user presses “space”. The towerpiece has to be dropped so it lands on top of the tower, if a towerpiece is more than half of the width of a towerpiece off the center of the last towerpiece it will fall down and the player will lose a life. Everytime a towerpiece is placed correctly the player will score a point.

Each player will start with three lives and when all three lives are lost the game ends. The player with the most points after losing all lives wins.

Good luck!  

# UML diagrams
## Use case diagram
![use_case_pcss](https://user-images.githubusercontent.com/33686482/140653342-3a60bb05-c502-4e2a-9c95-ba094bf96384.PNG)

## Sequence diagram
![sequence](https://user-images.githubusercontent.com/33686482/140653550-f440114d-7bad-406f-af2d-15258c2ccac7.png)

## Class diagram

