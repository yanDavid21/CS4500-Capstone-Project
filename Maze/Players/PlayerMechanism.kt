package Players

import Common.Action
import Common.PlayerState
import Common.board.Board
import Common.board.Coordinates

class PlayerMechanism(
    val name: String
) {

    fun proposeBoard(numRows: Int, numCols: Int): Board {
        // TODO: implement
    }

    fun setup(state: PlayerState?, goal: Coordinates) {
        // TODO: implement
    }

    fun takeTurn(state: PlayerState): Action {
        // TODO: implement
    }
}