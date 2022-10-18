package Players

import Common.Action
import Common.board.Board
import Common.board.Coordinates
import Common.player.Player

/**
 * To instantiate different comparator strategies.
 *
 * These types of strategies specify an ordering of checking alternative goal tiles.
 * For all of these tiles, they explore every sliding and inserting combination
 * (left then right sliding for all rows; then up then down for all columns) and, if the goal tile or the
 * alternate goal is reachable, they move to this goal.
 */
abstract class AbstractOrderingStrategy(
    private val comparator: Comparator<Coordinates>
): MazeStrategy {

    override fun decideMove(board: Board, player: Player): Action {
        TODO("Not yet implemented")
    }
}