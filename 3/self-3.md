## Self-Evaluation Form for Milestone 3

Indicate below each bullet which file/unit takes care of each task:

1. rotate the spare tile by some number of degrees
We did forgot to include a degree to rotate the spare tile when shifting and inserting. The functionality exists and it is a matter of making a call
to `this.spareTile.rotate(degrees)` in `Referee:slideInsertAndDealWithPlayers`
https://github.khoury.northeastern.edu/CS4500-F22/yanda1928-josuma26/blob/12e1085f53960be3c8bd8e37dc241cfbc098808b/Maze/Common/tile/Tile.kt#L16

2. shift a row/column and insert the spare tile
   - plus its unit tests

https://github.khoury.northeastern.edu/CS4500-F22/yanda1928-josuma26/blob/12e1085f53960be3c8bd8e37dc241cfbc098808b/Maze/Common/state.kt#L43-L54
   
Tests:
https://github.khoury.northeastern.edu/CS4500-F22/yanda1928-josuma26/blob/12e1085f53960be3c8bd8e37dc241cfbc098808b/test/Common/referee/RefereeTest.kt#L65-L102

3. move the avatar of the currently active player to a designated spot
https://github.khoury.northeastern.edu/CS4500-F22/yanda1928-josuma26/blob/12e1085f53960be3c8bd8e37dc241cfbc098808b/Maze/Common/state.kt#L25

4. check whether the active player's move has returned its avatar home
https://github.khoury.northeastern.edu/CS4500-F22/yanda1928-josuma26/blob/12e1085f53960be3c8bd8e37dc241cfbc098808b/Maze/Common/state.kt#L113-L117

5. kick out the currently active player
https://github.khoury.northeastern.edu/CS4500-F22/yanda1928-josuma26/blob/12e1085f53960be3c8bd8e37dc241cfbc098808b/Maze/Common/state.kt#L57-L65

The ideal feedback for each of these points is a GitHub
perma-link to the range of lines in a specific file or a collection of
files.

A lesser alternative is to specify paths to files and, if files are
longer than a laptop screen, positions within files are appropriate
responses.

You may wish to add a sentence that explains how you think the
specified code snippets answer the request.

If you did *not* realize these pieces of functionality, say so.

