package Common.board

import Common.tile.Degree
import Common.tile.HorizontalDirection
import Common.tile.Tile
import Common.tile.VerticalDirection

/**
 * A Maze board that supports player operations.
 */
interface PlayableBoard {

    fun slide(rowPosition: RowPosition, direction: HorizontalDirection)

    fun slide(columnPosition: ColumnPosition, direction: VerticalDirection)

    fun insertSpareTile()

    fun rotateSpareTile(degree: Degree)

    fun getReachableTiles(startingPosition: Coordinates): Set<Tile>

}