# MEMORANDUM

TO: CS4500 Staff

FROM: David Yan, Jose Sulaiman Manzur

DATE: Oct 13, 2022

Subject: Player Interface

Dear CEOs, we are now proposing a design for the data representation and interface that we will provide our customers
so they can implement their game AIs.

### Data Representation for a Player's knowledge
- A player must know about itself: It should know its unique identifier, its home tile, its goal treasure,
whether or not it has found a treasure, and its current position on the board.
- The player must also know the current state of the board. Including the tiles's connector, treasure, player home's, 
and players currently in the tile. The player will know whose turn it currently is, what the spare tile is. 
- If they or any other player have won, and if they game has ended.

### Player Interface
- decide whether to make a turn or pass
- decide how to make a turn
  - row/col to slide
  - in which direction to slide
  - how much to rotate the spare tile
  - where to move to
- get the state of the game: receives the board, whose turn it is, positions, etc.. (everything outlined above)
- join the game
- leave the game
- enter money details
- send ready command