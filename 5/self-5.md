**If you use GitHub permalinks, make sure your link points to the most recent commit before the milestone deadline.**

## Self-Evaluation Form for Milestone 5

Indicate below each bullet which file/unit takes care of each task:

The player should support five pieces of functionality: 

- `name`
https://github.khoury.northeastern.edu/CS4500-F22/yanda1928-josuma26/blob/bc852bab0d9d739930914141289ead1a7fce0099/Maze/Players/player.kt#L18
https://github.khoury.northeastern.edu/CS4500-F22/yanda1928-josuma26/blob/bc852bab0d9d739930914141289ead1a7fce0099/Maze/Players/RandomBoardRiemannPlayerMechanism.kt#L20 (note: override val: name, the constructor sets the name value. Additionally, a getName function is added by the Kotlin compiler)
- `propose board` (okay to be `void`)
https://github.khoury.northeastern.edu/CS4500-F22/yanda1928-josuma26/blob/bc852bab0d9d739930914141289ead1a7fce0099/Maze/Players/player.kt#L23
https://github.khoury.northeastern.edu/CS4500-F22/yanda1928-josuma26/blob/bc852bab0d9d739930914141289ead1a7fce0099/Maze/Players/RandomBoardRiemannPlayerMechanism.kt#L29-L42
- `setting up`
https://github.khoury.northeastern.edu/CS4500-F22/yanda1928-josuma26/blob/bc852bab0d9d739930914141289ead1a7fce0099/Maze/Players/player.kt#L30
https://github.khoury.northeastern.edu/CS4500-F22/yanda1928-josuma26/blob/bc852bab0d9d739930914141289ead1a7fce0099/Maze/Players/RandomBoardRiemannPlayerMechanism.kt#L51-L56
- `take a turn`
https://github.khoury.northeastern.edu/CS4500-F22/yanda1928-josuma26/blob/bc852bab0d9d739930914141289ead1a7fce0099/Maze/Players/player.kt#L35
https://github.khoury.northeastern.edu/CS4500-F22/yanda1928-josuma26/blob/bc852bab0d9d739930914141289ead1a7fce0099/Maze/Players/RandomBoardRiemannPlayerMechanism.kt#L62-L66
- `did I win`
https://github.khoury.northeastern.edu/CS4500-F22/yanda1928-josuma26/blob/bc852bab0d9d739930914141289ead1a7fce0099/Maze/Players/player.kt#L40
https://github.khoury.northeastern.edu/CS4500-F22/yanda1928-josuma26/blob/bc852bab0d9d739930914141289ead1a7fce0099/Maze/Players/RandomBoardRiemannPlayerMechanism.kt#L71-L73

Provide links. 

The referee functionality should compose at least four functions:

- setting up the player with initial information
https://github.khoury.northeastern.edu/CS4500-F22/yanda1928-josuma26/blob/bc852bab0d9d739930914141289ead1a7fce0099/Maze/Referee/referee.kt#L70-L77
- running rounds until termination
https://github.khoury.northeastern.edu/CS4500-F22/yanda1928-josuma26/blob/bc852bab0d9d739930914141289ead1a7fce0099/Maze/Referee/referee.kt#L83-L98
- running a single round (part of the preceding bullet)
https://github.khoury.northeastern.edu/CS4500-F22/yanda1928-josuma26/blob/bc852bab0d9d739930914141289ead1a7fce0099/Maze/Referee/referee.kt#L131-L135
- scoring the game
https://github.khoury.northeastern.edu/CS4500-F22/yanda1928-josuma26/blob/bc852bab0d9d739930914141289ead1a7fce0099/Maze/Referee/referee.kt#L159-L163

Point to two unit tests for the testing referee:

1. a unit test for the referee function that returns a unique winner
https://github.khoury.northeastern.edu/CS4500-F22/yanda1928-josuma26/blob/bc852bab0d9d739930914141289ead1a7fce0099/test/Players/RefereeTest.kt#L100-L114
2. a unit test for the scoring function that returns several winners  
https://github.khoury.northeastern.edu/CS4500-F22/yanda1928-josuma26/blob/bc852bab0d9d739930914141289ead1a7fce0099/test/Players/RefereeTest.kt#L169-L186

The ideal feedback for each of these points is a GitHub
perma-link to the range of lines in a specific file or a collection of
files -- in the last git-commit from Thursday evening. 

A lesser alternative is to specify paths to files and, if files are
longer than a laptop screen, positions within files are appropriate
responses.

You may wish to add a sentence that explains how you think the
specified code snippets answer the request.

If you did *not* realize these pieces of functionality, say so.

