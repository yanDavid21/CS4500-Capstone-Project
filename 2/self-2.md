## Self-Evaluation Form for Milestone 2

Indicate below each bullet which file/unit takes care of each task:

1. point to the functinality for determining reachable tiles 

   - a data representation for "reachable tiles"
      - we represented this as a Set since this collection has no particular order and no duplicates, we found there is no need for another data representation other than Set and Tile. https://github.khoury.northeastern.edu/CS4500-F22/yanda1928-josuma26/blob/f5da4a5dd44055864b694ede57b38b69d9494b33/Maze/Common/board/BoardTiles.kt#L42
   - its signature and purpose statement  https://github.khoury.northeastern.edu/CS4500-F22/yanda1928-josuma26/blob/f5da4a5dd44055864b694ede57b38b69d9494b33/Maze/Common/board/Board.kt#L37-L41
   - its "cycle detection" coding (accumulator)  
   https://github.khoury.northeastern.edu/CS4500-F22/yanda1928-josuma26/blob/f5da4a5dd44055864b694ede57b38b69d9494b33/Maze/Common/board/BoardTiles.kt#L42  https://github.khoury.northeastern.edu/CS4500-F22/yanda1928-josuma26/blob/f5da4a5dd44055864b694ede57b38b69d9494b33/Maze/Common/board/BoardTiles.kt#L48
   - its unit test(s)  https://github.khoury.northeastern.edu/CS4500-F22/yanda1928-josuma26/blob/f5da4a5dd44055864b694ede57b38b69d9494b33/test/Common/board/BoardTest.kt#L99-L100  https://github.khoury.northeastern.edu/CS4500-F22/yanda1928-josuma26/blob/f5da4a5dd44055864b694ede57b38b69d9494b33/test/Common/board/BoardTest.kt#L112-L113

2. point to the functinality for shifting a row or column 

   - its signature and purpose statement  
      - We forgot a purpose statement here. https://github.khoury.northeastern.edu/CS4500-F22/yanda1928-josuma26/blob/f5da4a5dd44055864b694ede57b38b69d9494b33/Maze/Common/board/BoardTiles.kt#L22  
   - unit tests for rows and columns  https://github.khoury.northeastern.edu/CS4500-F22/yanda1928-josuma26/blob/f5da4a5dd44055864b694ede57b38b69d9494b33/test/Common/board/BoardTest.kt#L14-L15  https://github.khoury.northeastern.edu/CS4500-F22/yanda1928-josuma26/blob/f5da4a5dd44055864b694ede57b38b69d9494b33/test/Common/board/BoardTest.kt#L36-L37  https://github.khoury.northeastern.edu/CS4500-F22/yanda1928-josuma26/blob/f5da4a5dd44055864b694ede57b38b69d9494b33/test/Common/board/BoardTest.kt#L41-L42  https://github.khoury.northeastern.edu/CS4500-F22/yanda1928-josuma26/blob/f5da4a5dd44055864b694ede57b38b69d9494b33/test/Common/board/BoardTest.kt#L48-L49

3. point to the functinality for inserting a tile into the open space

   - its signature and purpose statement
      - we argue that insertTileIntoEmptySlot is a descriptive and obvious name such that a purpose statement is unneeded based on its signature and the class that it resides in.  
  https://github.khoury.northeastern.edu/CS4500-F22/yanda1928-josuma26/blob/f5da4a5dd44055864b694ede57b38b69d9494b33/Maze/Common/board/BoardTiles.kt#L14
   - unit test(s)  https://github.khoury.northeastern.edu/CS4500-F22/yanda1928-josuma26/blob/f5da4a5dd44055864b694ede57b38b69d9494b33/test/Common/board/BoardTest.kt#L55-L56  https://github.khoury.northeastern.edu/CS4500-F22/yanda1928-josuma26/blob/f5da4a5dd44055864b694ede57b38b69d9494b33/test/Common/board/BoardTest.kt#L65-L66  https://github.khoury.northeastern.edu/CS4500-F22/yanda1928-josuma26/blob/f5da4a5dd44055864b694ede57b38b69d9494b33/test/Common/board/BoardTest.kt#L77-L78

If you combined pieces of functionality or separated them, explain.

If you think the name of a method/function is _totally obvious_,
there is no need for a purpose statement. 

The ideal feedback for each of these points is a GitHub
perma-link to the range of lines in a specific file or a collection of
files.

A lesser alternative is to specify paths to files and, if files are
longer than a laptop screen, positions within files are appropriate
responses.

You may wish to add a sentence that explains how you think the
specified code snippets answer the request.

If you did *not* realize these pieces of functionality, say so.
