package Common

import Common.board.ColumnPosition
import Common.board.Coordinates
import Common.board.RowPosition
import Common.tile.Degree
import Common.tile.HorizontalDirection
import Common.tile.VerticalDirection

/**
 * An Action is one of:
 *  - Skip
 *  - MoveAction which can be either a row or a column move which includes a sliding position, direction, rotation and
 *    new position.
 */
sealed interface Action

/**
 * An action that modifies the game state by moving the board.
 */
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
            return (other.rowPosition == this.rowPosition) && other.direction == this.direction.reverse()
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
            return (other.columnPosition == this.columnPosition) && other.direction == this.direction.reverse()
        }
        return false
    }
}

/**
 * To represent a skipping player action.
 */
object Skip: Action