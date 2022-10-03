# Board

## PlayableBoard Interface

### Methods

 - `slide(row || col, Direction) -> Unit`: Slides the specified row or column into the 
direction.
 - `insertSpareTile() -> Unit`: Inserts the spare tile into the empty space. 
Should be called in a state after a `slide` call.
 - `rotateSpareTile(Degree) -> Unit`: rotates the spare tile by the specified amount.
 - `getReachableTiles(x,y) -> List<Tile>`: gets the tiles reachable from `(x,y)`
 - `makeRandom()`: creates a random board.

### Fields

- Rows and columns: `List<Tile>`
- `spareTile: Tile`
- `dislogged: Tile?` only exists after a slide, before an insert
- `emptySlot: Pair<Int, Int>?` only exists after a slide, before an insert
