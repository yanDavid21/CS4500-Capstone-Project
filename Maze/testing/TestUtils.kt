package testing

import Common.ColumnAction
import Common.MovingAction
import Common.RowAction
import Common.board.Board
import Common.board.ColumnPosition
import Common.board.Coordinates
import Common.board.RowPosition
import Common.tile.Degree
import Common.tile.GameTile
import Common.tile.HorizontalDirection
import Common.tile.VerticalDirection
import serialization.data.DirectionDTO

object TestUtils {

    fun getTilesInRow(rowIndex: Int, board: Board): Array<GameTile> {
        return (0 .. 6)
            .map { board.getTile(Coordinates.fromRowAndValue(rowIndex, it)) }.toTypedArray()
    }

    fun getTilesInCol(colIndex: Int, board: Board): Array<GameTile> {
        return (0 .. 6)
            .map { board.getTile(Coordinates.fromRowAndValue(it, colIndex)) }.toTypedArray()
    }


}