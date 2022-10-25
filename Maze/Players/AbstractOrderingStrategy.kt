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

typealias TileIdentifier = (Coordinates) -> Boolean
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
) : MazeStrategy {
    

    override fun decideMove(playerState: PlayerState): Action {
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
    private fun moveToGoalIfReachable(playerState: PlayerState): MovingAction? {
        val isTileValidGoal: TileIdentifier= { tilePosition ->
            tilePosition == player.getGoal()
        }
        return tryAllCombinationsToReachDesiredTile(playerState, isTileValidGoal)
    }

    /**
     * Finds all possible alternatives in an ordering specified by the concrete tile.
     * For every tile, tries all possibilities to try to reach it,
     *
     * Returns the first action that leads to the alternate tile.
     */
    private fun tryToReachAllAlternativeTiles(playerState: PlayerState): MovingAction? {
        val allCoordinatesInDesiredOrder = getAllCoordinates().sortedWith(comparator)
        return allCoordinatesInDesiredOrder.fold(null as MovingAction?) { action, coord ->
            action ?: tryAllCombinationsToReachDesiredTile(playerState,
                isTileWeWant = { tilePos -> tilePos == coord })
        }
    }

    private fun tryAllCombinationsToReachDesiredTile(playerState: PlayerState, isTileWeWant: TileIdentifier): MovingAction? {
        return findFirstRowSlideActionIfAny(playerState, isTileWeWant)
            ?: findFirstColumnSlideActionIfAny(playerState, isTileWeWant)
    }

    private fun findFirstRowSlideActionIfAny(playerState: PlayerState, isTileWeWant: TileIdentifier): MovingAction? {
        val allRows = getAllRows()
        val allRowActionCombinations= getAllCombinations(allRows, HorizontalDirection.values())
        return allRowActionCombinations.fold(null as MovingAction?) { action, (rowPosition, direction, degree) ->
            action ?: slideRow(playerState, rowPosition, direction, degree, isTileWeWant)
        }
    }

    private fun findFirstColumnSlideActionIfAny(playerState: PlayerState, isTileWeWant: TileIdentifier): MovingAction? {
        val allCols = getAllCols()
        val allColumnActionCombinations = getAllCombinations(allCols, VerticalDirection.values())
        return allColumnActionCombinations.fold(null as MovingAction?) { answ, (colPosition, direction, degree) ->
            answ ?: slideCol(playerState, colPosition, direction, degree, isTileWeWant)
        }
    }

    /**
     * Slides a row in the given direction with the provided spare tile rotation. Checks if the goal or
     * the alternate tile is reachable.
     */
    private fun slideRow(playerState: PlayerState, position: RowPosition, direction: HorizontalDirection, degree: Degree, isTileWeWant: TileIdentifier): MovingAction? {
        return doSlideAndCheckReachable(playerState, player.copy(),
            doSlide = { state -> state.slideRowAndInsertSpare(position, direction, degree) },
            createAction = { RowAction(position, direction, degree, it) }, isTileWeWant)
    }

    /**
     * Slides a column in the given direction with the provided spare tile rotation. Checks if the goal or
     * the alternate tile is reachable.
     */
    private fun slideCol(playerState: PlayerState, position: ColumnPosition, direction: VerticalDirection, degree: Degree, isTileWeWant: TileIdentifier): MovingAction? {
        return doSlideAndCheckReachable(playerState, player.copy(),
            doSlide = { state -> state.slideColumnAndInsertSpare(position, direction, degree) },
            createAction = { ColumnAction(position, direction, degree, it) }, isTileWeWant)
    }

    /**
     * Performs the given sliding action, gets all reachable positions from the player's current positions,
     * finds if any of these tiles are the desired goal, if so returns an action to reach it, otherwise returns
     * null (MovingAction?)
     */
    private fun doSlideAndCheckReachable(
        playerState: PlayerState,
        player: Player,
        doSlide: (GameState) -> Unit,
        createAction: (Coordinates) -> MovingAction,
        isTileWeWant: TileIdentifier
    ): MovingAction? {
        val (board, spareTile, lastAction) = playerState
        val state = GameState(board, spareTile, listOf(player))
        doSlide(state)
        val newBoard = state.getBoard()
        val reachablePositions = newBoard.getReachableTiles(player.currentPosition)
        return reachablePositions.fold(null as MovingAction?) { action, reachablePos ->
            action ?: getActionToReachTile(lastAction, reachablePos, isTileWeWant, createAction)
        }
    }

    // TODO: check if action (including goal tile) is valid instead
    private fun getActionToReachTile(lastAction: MovingAction?, pos: Coordinates, isTileWeWant: TileIdentifier,
                                     createAction: (Coordinates) -> MovingAction): MovingAction? {
        if (!isTileWeWant(pos)) {
            return null
        }
        val action = createAction(pos)
        return if (lastAction != null && action.isUndoingAction(lastAction)) null else action
    }

    private fun getAllRows():List<RowPosition> {
        return (Position.MIN_ROW_INDEX .. Position.MAX_ROW_INDEX step 2).map { RowPosition(it) }
    }

    private fun getAllCols():List<ColumnPosition> {
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