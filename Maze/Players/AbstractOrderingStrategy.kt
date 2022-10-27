package Players

import Common.*
import Common.board.ColumnPosition
import Common.board.Coordinates
import Common.board.Position
import Common.board.RowPosition
import Common.player.Player
import Common.tile.Degree
import Common.tile.HorizontalDirection
import Common.tile.VerticalDirection

/**
 * To instantiate different comparator strategies.
 *
 * These types of strategies specify an ordering of checking alternative goal tiles.
 * For all of these tiles, they explore every sliding and inserting combination
 * (left then right sliding for all rows; then up, then down for all columns) and, if
 * the goal tile or the alternate goal is reachable, they move to this goal.
 */
abstract class AbstractOrderingStrategy(
    private val comparator: Comparator<Coordinates>,
    private val player: Player
) : MazeStrategy {

    override fun decideMove(playerState: PublicGameState): Action {
        return moveToGoalIfReachable(playerState)
            ?: tryToReachAllAlternativeTiles(playerState)
            ?: Skip
    }

    /**
     * Tries all combinations to reach the player's goal.
     *
     * The player's goal is the treasure tile, if it has not yet found the treasure; or
     * the home tile if it has.
     *
     * Returns the first action that leads to the goal.
     */
    private fun moveToGoalIfReachable(playerState: PublicGameState): MovingAction? {
        return tryAllCombinationsToReachDesiredTile(playerState, player.getGoal())
    }

    /**
     * Finds all possible alternatives in an ordering specified by the concrete tile.
     * For every tile, tries all possibilities to try to reach it,
     *
     * Returns the first action that leads to the alternate tile.
     */
    private fun tryToReachAllAlternativeTiles(playerState: PublicGameState): MovingAction? {
        val allCoordinatesInDesiredOrder = getAllCoordinates().sortedWith(comparator)
        return allCoordinatesInDesiredOrder.fold(null as MovingAction?) { action, coord ->
            action ?: tryAllCombinationsToReachDesiredTile(playerState, coord)
        }
    }

    private fun tryAllCombinationsToReachDesiredTile(playerState: PublicGameState,
                                                     goalTile: Coordinates): MovingAction? {
        return findFirstRowSlideActionIfAny(playerState, goalTile)
            ?: findFirstColumnSlideActionIfAny(playerState, goalTile)
    }

    private fun findFirstRowSlideActionIfAny(playerState: PublicGameState,
                                             goalTile: Coordinates): MovingAction? {
        val allRows = getAllMovableRows()
        val allRowActionCombinations= getAllCombinations(allRows, HorizontalDirection.values())
        return allRowActionCombinations.fold(null as MovingAction?) {
                action, (rowPosition, direction, degree) ->
            action ?: isRowSlideValidMove(playerState, rowPosition, direction, degree, goalTile)
        }
    }

    private fun findFirstColumnSlideActionIfAny(playerState: PublicGameState,
                                                goalTile: Coordinates): MovingAction? {
        val allCols = getAllMovableColumns()
        val allColumnActionCombinations = getAllCombinations(allCols, VerticalDirection.values())
        return allColumnActionCombinations.fold(null as MovingAction?) {
                answ, (colPosition, direction, degree) ->
            answ ?: isColumnSlideValidMove(playerState, colPosition, direction, degree, goalTile)
        }
    }

    /**
     * Checks if doing a slide in the specified row direction is valid, returns null otherwise.
     */
    private fun isRowSlideValidMove(playerState: PublicGameState, position: RowPosition,
                                    direction: HorizontalDirection, degree: Degree,
                                    goalPosition: Coordinates): MovingAction? {
        return checkSlideAction(playerState, player.copy(),
            checkAction = { state -> state.isValidRowMove(position, direction, degree, goalPosition) },
            RowAction(position, direction, degree, goalPosition))
    }

    /**
     * Checks if doing a slide in the specified row direction is valid, returns null otherwise.
     */
    private fun isColumnSlideValidMove(playerState: PublicGameState, position: ColumnPosition,
                                       direction: VerticalDirection, degree: Degree,
                                       goalPosition: Coordinates): MovingAction? {
        return checkSlideAction(playerState, player.copy(),
            checkAction = { state -> state.isValidColumnMove(position, direction, degree, goalPosition) },
            ColumnAction(position, direction, degree, goalPosition))
    }

    /**
     * Checks if this sliding action is valid and does not undo move.
     */
    private fun checkSlideAction(
        playerState: PublicGameState,
        player: Player,
        checkAction: (GameState) -> Boolean,
        action: MovingAction,
    ): MovingAction? {
        val (board, spareTile, lastAction) = playerState
        val state = GameState(board, spareTile, listOf(player))
        val willUndoLastAction = lastAction?.isUndoingAction(action) ?: false
        return if (checkAction(state) && !willUndoLastAction) action else null
    }

    private fun getAllMovableRows():List<RowPosition> {
        return (Position.MIN_ROW_INDEX .. Position.MAX_ROW_INDEX step 2).map { RowPosition(it) }
    }

    private fun getAllMovableColumns():List<ColumnPosition> {
        return (Position.MIN_COL_INDEX .. Position.MAX_COL_INDEX step 2).map { ColumnPosition(it) }
    }
    
    private fun <P, D> getAllCombinations(
        allPositions: List<P>, directions: Array<D>
    ): List<Triple<P, D, Degree>> {
        val combos = mutableListOf<Triple<P, D, Degree>>()
        for (position in allPositions) {
            for (direction in directions) {
                for (degree in Degree.values()) {
                    combos.add(Triple(position, direction, degree))
                }
            }
        }
        return combos
    }

    private fun getAllCoordinates(): List<Coordinates> {
        val allCoordinates = mutableListOf<Coordinates>()
        for (rowIndex in Position.MIN_ROW_INDEX .. Position.MAX_ROW_INDEX) {
            for (colIndex in Position.MIN_COL_INDEX .. Position.MAX_COL_INDEX) {
                allCoordinates.add(Coordinates(RowPosition(rowIndex), ColumnPosition((colIndex))))
            }
        }
        return allCoordinates
    }
}