package Common

import Common.board.ColumnPosition
import Common.board.Coordinates
import Common.board.RowPosition
import Common.tile.Degree
import Common.tile.HorizontalDirection
import Common.tile.VerticalDirection

interface Action

sealed interface MovingAction: Action {

    fun isUndoingAction(other: MovingAction): Boolean
}

data class RowAction(
    val rowPosition: RowPosition,
    val direction: HorizontalDirection,
    val rotation: Degree,
    val newPosition: Coordinates
): MovingAction {

    override fun isUndoingAction(other: MovingAction): Boolean {
        if (other is RowAction) {
            return (other.rowPosition == rowPosition) && other.direction == direction.reverse()
        }
        return false
    }
}

data class ColumnAction(
    val columnPosition: ColumnPosition,
    val direction: VerticalDirection,
    val rotation: Degree,
    val newPosition: Coordinates
): MovingAction {

    override fun isUndoingAction(other: MovingAction): Boolean {
        if (other is ColumnAction) {
            return (other.columnPosition == columnPosition) && other.direction == direction.reverse()
        }
        return false
    }
}

object Skip: Action