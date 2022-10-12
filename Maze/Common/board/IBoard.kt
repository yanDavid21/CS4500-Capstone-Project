package Common.board

import Common.tile.HorizontalDirection
import Common.tile.Tile
import Common.tile.VerticalDirection

/**
 * A Maze board that supports player operations.
 */
interface IBoard {
    fun getReachableTiles(startingPosition: Coordinates): Set<Tile>

    fun slide(rowPosition: RowPosition, direction: HorizontalDirection): Tile

    fun slide(columnPosition: ColumnPosition, direction: VerticalDirection): Tile

    fun insertTileIntoEmptySlot(tile: Tile)
}