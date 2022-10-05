package Common.board

import Common.*
import Common.board.tile.Degree

/**
 * A Maze board that supports player operations.
 */
interface PlayableBoard {

    fun slide(rowPosition: RowPosition, direction: HorizontalDirection)

    fun slide(columnPosition: ColumnPosition, direction: VerticalDirection)

    fun insertSpareTile()

    fun rotateSpareTile(degree: Degree)

    fun getReachableTiles(rowPosition: RowPosition, columnPosition: ColumnPosition): Set<Tile>

}