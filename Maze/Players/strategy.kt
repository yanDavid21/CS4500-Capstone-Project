package Players

import Common.Action
import Common.board.Board
import Common.player.Player

interface MazeStrategy {
    fun decideMove(board: Board, player: Player): Action
}