# MEMORANDUM

TO: CS4500 Staff

FROM: David Yan, Jose Sulaiman Manzur

DATE: Oct 27, 2022

Subject: Interactive player mechanism

Dear CEOs,  
We are now ready to expand to customers who do not wish to use the command line 
interface/custom programs to play Maze. Here is our proposal for a client program with a graphical user 
interface:

## Maze Client
This client shall be split into three sections: Model, View, Controller

### Model
Keeps track of the known public game state and the private game knowledge specific
to the client/user. The model will have methods to act upon the data, such as reading, updating,
deletion, etc. 
- getPlayerHome(): Coordinates
- updatePlayerHome(coords: Coordinates): Unit
- updateBoardKnowledge(state: PublicGameState): Unit
- getCurrentLocation(): 
