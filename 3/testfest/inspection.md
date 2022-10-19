Pair: yanda1928-josuma26 \
Commit: [12e1085f53960be3c8bd8e37dc241cfbc098808b](https://github.khoury.northeastern.edu/CS4500-F22/yanda1928-josuma26/tree/12e1085f53960be3c8bd8e37dc241cfbc098808b) \
Self-eval: https://github.khoury.northeastern.edu/CS4500-F22/yanda1928-josuma26/blob/24148d260f5c377cbf6203a4703d7914b75e3484/3/self-3.md \
Score: 80/85 \
Grader: Rajat Keshri

Self Eval: 20/20 points

state.PP: 45/45
  - [5/5pt] - rotate the spare tile by some number of degrees
  - [5/5pt] - shift a row/column and insert the spare tile
  - [5/5pt] - move the avatar of the currently active player to a designated spot
  - [5/5pt] - check whether the active player's move has returned its avatar home
  - [5/5pt] - kick out the currently active player

Tests - 
  - [10/10pt] - test a row and a column insertion
  - [5/10pt] - how does a (the) unit test(s) confirm that a player (or several) move along.
        
    Think of including the tests for the following - 
    1) What should happen to a player at the edge, when its tile slides out?
    2) What should happen to a player on the row which is sliding?
    3) What should happen to a player not effected by the insertion of the new tile?


player.md: 20/20
  - [5/5pt] A player must have a `take turn` function/method; it may have more. 

A player must _know_ about: 
  - [5/5pt] its home,
  - [5/5pt] its current goal assignment,
  - [5/5pt] its current location

\<free text\>
