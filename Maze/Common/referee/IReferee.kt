package Common.referee

import Common.board.ColumnPosition
import Common.board.RowPosition
import Common.tile.Degree
import Common.tile.HorizontalDirection
import Common.tile.Tile
import Common.tile.VerticalDirection


interface IReferee {

    fun slideRow(rowPosition: RowPosition, direction: HorizontalDirection)

    fun slideColumn(columnPosition: ColumnPosition, direction: VerticalDirection)

    fun insertSpareTile()

    fun rotateSpareTile(degree: Degree)

    fun kickOutActivePlayer()

    fun activePlayerCanReachTile(tile: Tile): Boolean

}

