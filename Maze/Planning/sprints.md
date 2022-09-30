Memorandum

TO: CS4500 Course Staff (CEOs of our soon-to-be unicorn)

FROM: David Yan, Jose Sulaiman Manzur

DATE: Sep 30, 2022

SUBJECT: Three Sprint Planning

After deliberating with the (two-man) team, we have come up with the following plan for our first three sprints. We estimate each 
sprint will take about 16 hours of work. We will begin by designing the first game you want to implement: Maze. We know we
aim at having support for multiple games, but we want to design a concrete game first so we can develop a better "abstract" 
design for games and then implement the server.

### Sprint 1 (ends 10/8)
#### Target state:
* Design document for the game components have been written. (2 hours)
* Design document for (player/referee) protocol has been written. (2 hours)
* Data representations of Maze components (board, tiles, treasure,gems) are implemented. (3 hours)
  * The game components we wish to design this spring are the game board and tiles, the gems, the players, and the referee.
* Game logic implementation is in progress. (9 hours)
  * Unit tests exist for game logic.
  * Sliding
  * Rotating tile
  * Placing tile
  * Assigning home and treasure location
#### Reach goals
* Have a basic UI for easy visual testing (read from model and display)

### Sprint 2 (ends 10/15)
#### Current state
Game design has been documented and the model is being developed.

### Target state


### Sprint 3 (ends 10/22)
#### Current state
Game logic (model) and static UI (view) is finished.
#### Target state
* Design document for server finished. (3 hours)
* First iteration of MVC interfaces are completed (2 hours)
* Implement a single-player controller.  (2 hours)
* Implement a responsive view integrated with the controller. (3 hours)
* Begin implementing the server. (6 hours)

#### Reach goals
* Connect a CLI client to the server