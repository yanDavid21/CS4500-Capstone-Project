package Common

import Common.board.Board
import Common.tile.GameTile

/**
 * Holds the data a particular player will know about the game.
 */
data class PlayerState(
    val board: Board, val spareTile: GameTile, val lastAction: MovingAction?
)