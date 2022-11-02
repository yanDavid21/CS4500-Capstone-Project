Pair: yanda1928-josuma26 \
Commit: [8a2d3972c74610283957cd3b8fd88c7719a5809c](https://github.khoury.northeastern.edu/CS4500-F22/yanda1928-josuma26/tree/8a2d3972c74610283957cd3b8fd88c7719a5809c) \
Self-eval: https://github.khoury.northeastern.edu/CS4500-F22/yanda1928-josuma26/blob/c03f905358bc7fce2bc4cc0c8e9f7b92f4c11294/5/self-5.md \
Score: 118/160
Grader: Somtoo Chukwurah

20/20: Self-eval

98/110 `player.PP and referee.PP`

`player.PP`: Player class should have the following methods (as specified in Logical Interactions) with good names, signatures/types, and purpose statements:

- [4/10pt] `name`
   - not a function
- [10/10pt] `propose board`
  - 
- [10/10pt] `setting up`
- [10/10pt] `take a turn`
- [10/10pt] `did I win`

`referee.PP` must have following functions with good names, signatures/types, and purpose statements:

- [10/10pt] setting up the player with initial information
- [10/10pt] running rounds until the game is over
- [4/10pt] running a round, which must have functionality for
  - checking for "all passes"
  - checking for a player that returned to its home (winner)
     - cannot ascertain checking for all passes
- [10/10pt] score the game


Unit tests for running a game:

- [10/10pt] a unit test for the referee function that returns a unique winner
- [10/10pt] a unit test for the scoring function that returns several winners

0/30 `interactive-player.md` 
 - Not a design doc.

Description of gestures that

- [0/10pt] rotates the tile before insertion 
- [0/10pt] selects a row or column to be shifted and in which direction
- [0/10pt] selects the next place for the player's avatar

