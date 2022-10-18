package Common

import Common.board.ColumnPosition
import Common.board.Coordinates
import Common.board.RowPosition
import Common.tile.Degree
import Common.tile.HorizontalDirection
import Common.tile.VerticalDirection

interface Action

data class SlideRowRotateAndInsert(
    val rowPosition: RowPosition,
    val direction: HorizontalDirection,
    val rotation: Degree,
    val newPosition: Coordinates
): Action

data class SlideColumnRotateAndInsert(
    val columnPosition: ColumnPosition,
    val direction: VerticalDirection,
    val rotation: Degree,
    val newPosition: Coordinates
): Action

object Skip: Action