package Players

import Common.Action
import Common.board.Board
import Common.tile.GameTile

interface MazeStrategy {
    fun decideMove(board: Board, spareTile: GameTile): Action
}