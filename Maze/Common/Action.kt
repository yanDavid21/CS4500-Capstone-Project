package Common

import Common.board.ColumnPosition
import Common.board.Coordinates
import Common.board.RowPosition
import Common.tile.Degree
import Common.tile.HorizontalDirection
import Common.tile.VerticalDirection

interface Action

interface MovingAction: Action
data class RowAction(
    val rowPosition: RowPosition,
    val direction: HorizontalDirection,
    val rotation: Degree,
    val newPosition: Coordinates
): MovingAction

data class ColumnAction(
    val columnPosition: ColumnPosition,
    val direction: VerticalDirection,
    val rotation: Degree,
    val newPosition: Coordinates
): MovingAction

object Skip: Action