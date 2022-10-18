package Players

import Common.Action
import Common.GameState
import Common.SlideRowRotateAndInsert
import Common.board.*
import Common.player.Player
import Common.tile.Degree
import Common.tile.GameTile
import Common.tile.HorizontalDirection

/**
 * To instantiate different comparator strategies.
 *
 * These types of strategies specify an ordering of checking alternative goal tiles.
 * For all of these tiles, they explore every sliding and inserting combination
 * (left then right sliding for all rows; then up then down for all columns) and, if the goal tile or the
 * alternate goal is reachable, they move to this goal.
 */
abstract class AbstractOrderingStrategy(
    private val comparator: Comparator<Coordinates>,
    private val player: Player
): MazeStrategy {
    protected val allCoordinates = mutableListOf<Coordinates>()
    protected val isTileValidGoal: (GameTile) -> Boolean = { tile ->
        player.treasureFound && tile == player.homeTile || !player.treasureFound && tile.treasure == player.goal
    }

    override fun decideMove(board: Board, spareTile: GameTile): Action {
        val reachablesTiles = board.getReachableTiles(player.currentPosition)

        for (reachable in reachablesTiles) {
            if (isTileValidGoal(board.getTile(reachable))) {
                return  // TODO: construct move with a slide that allows us to move
            }
        }

        // try all possible combinations using riemann

        val allCoordinates = getAllCoordinatesFromBoard(board)
        val coordinatesInDesiredOrder = allCoordinates.sortedWith(comparator)
        for (coord in coordinatesInDesiredOrder) {
            tryAllCombinationsToReachThisTile(board, spareTile, isTileValidGoal, board.getTile(coord))
        }
    }

    protected fun tryAllCombinationsToReachThisTile(board: Board, spare: GameTile, isTileGoal: (GameTile) -> Boolean, alternate: GameTile): Action? {

        (Position.MIN_ROW_INDEX until Position.MAX_ROW_INDEX).forEach { rowIndex ->
            val rowPosition = RowPosition(rowIndex)
            HorizontalDirection.values().forEach { direction ->
                Degree.values().forEach { degree ->
                    val state = GameState(board, spare, listOf(player))
                    val reachables = slideAndGetReachableTiles(state, rowPosition, direction, degree)
                    for (pos in reachables) {
                        val tile = state.getBoard().getTile(pos)
                        if (isTileGoal(tile) || tile == alternate) {
                            return SlideRowRotateAndInsert(rowPosition, direction, degree, pos)
                        }
                    }
                }
            }
        }
        return null
    }

    private fun slideAndGetReachableTiles(state: GameState, rowPosition: RowPosition, direction: HorizontalDirection, degree: Degree): Set<Coordinates> {
        state.slideRowAndInsertSpare(rowPosition, direction, degree)
        val newBoard = state.getBoard()
        return newBoard.getReachableTiles(player.currentPosition)
    }

    private fun getAllCoordinatesFromBoard(board: Board): List<Coordinates> {
        val allCoordinates = mutableListOf<Coordinates>()
        for (rowIndex in Position.MIN_ROW_INDEX until Position.MAX_ROW_INDEX) {
            for (colIndex in Position.MIN_COL_INDEX until Position.MAX_COL_INDEX) {
                allCoordinates.add(Coordinates(RowPosition(rowIndex), ColumnPosition((colIndex))))
            }
        }
        return allCoordinates
    }


}