package Common

import Common.board.RowPosition
import Common.tile.Degree
import Common.tile.HorizontalDirection

interface Action {
}

data class SlideRowRotateAndInsert(
    val rowPosition: RowPosition,
    val direction: HorizontalDirection,
    val rotation: Degree
): Action

enum class Skip: Action