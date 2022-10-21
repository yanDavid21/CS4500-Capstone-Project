**If you use GitHub permalinks, make sure your links points to the most recent commit before the milestone deadline.**

## Self-Evaluation Form for Milestone 4

The milestone asks for a function that performs six identifiable
separate tasks. We are looking for four of them and will overlook that
some of you may have written deep loop nests (which are in all
likelihood difficult to understand for anyone who is to maintain this
code.)

Indicate below each bullet which file/unit takes care of each task:

1. the "top-level" function/method, which composes tasks 2 and 3
   https://github.khoury.northeastern.edu/CS4500-F22/yanda1928-josuma26/blob/5c8a869cd30913216b9db03865f0f9fa2ae8f2d7/Maze/Players/AbstractOrderingStrategy.kt#L28
2. a method that `generates` the sequence of spots the player may wish to move to
   https://github.khoury.northeastern.edu/CS4500-F22/yanda1928-josuma26/blob/5c8a869cd30913216b9db03865f0f9fa2ae8f2d7/Maze/Players/AbstractOrderingStrategy.kt#L57


3. a method that `searches` rows,  columns, etcetc. 

https://github.khoury.northeastern.edu/CS4500-F22/yanda1928-josuma26/blob/b203e91a0efb9817836a5e88f90baeeb07a6a027/Maze/Players/AbstractOrderingStrategy.kt#L64-L67

4. a method that ensure that the location of the avatar _after_ the
   insertion and rotation is a good one and makes the target reachable

ALSO point to

- the data def. for what the strategy returns

https://github.khoury.northeastern.edu/CS4500-F22/yanda1928-josuma26/blob/b203e91a0efb9817836a5e88f90baeeb07a6a027/Maze/Common/Action.kt#L10

- unit tests for the strategy

For both:
https://github.khoury.northeastern.edu/CS4500-F22/yanda1928-josuma26/blob/b203e91a0efb9817836a5e88f90baeeb07a6a027/test/Players/AbstractOrderingStrategyTests.kt#L14

Euclid specific:
https://github.khoury.northeastern.edu/CS4500-F22/yanda1928-josuma26/blob/b203e91a0efb9817836a5e88f90baeeb07a6a027/test/Players/EuclidTest.kt#L15

Riemmann:
https://github.khoury.northeastern.edu/CS4500-F22/yanda1928-josuma26/blob/b203e91a0efb9817836a5e88f90baeeb07a6a027/test/Players/RiemannTest.kt#L14

The ideal feedback for each of these points is a GitHub
perma-link to the range of lines in a specific file or a collection of
files.

A lesser alternative is to specify paths to files and, if files are
longer than a laptop screen, positions within files are appropriate
responses.

You may wish to add a sentence that explains how you think the
specified code snippets answer the request.

If you did *not* realize these pieces of functionality or realized
them differently, say so and explain yourself.


