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
 * (left then right sliding for all rows; then up then down for all columns) and, if the goal tile or the
 * alternate goal is reachable, they move to this goal.
 */
abstract class AbstractOrderingStrategy(
    private val comparator: Comparator<Coordinates>,
    private val player: Player
) : MazeStrategy {
    

    //TODO: GAMESTATE instead ? need past action
    override fun decideMove(playerState: PlayerState): Action {
        return moveToGoalIfReachable(playerState)
            ?: tryToReachAllAlternativeTiles(playerState)
            ?: Skip
    }

    private fun moveToGoalIfReachable(playerState: PlayerState): MovingAction? {
        val isTileValidGoal: (Coordinates) -> Boolean = { coords ->
            (player.treasureFound && player.homePosition == coords)
                    || (!player.treasureFound && player.goalPosition == coords)
        }
        return tryAllCombinationsToReachDesiredTile(playerState, isTileValidGoal)
    }
    

    private fun tryToReachAllAlternativeTiles(playerState: PlayerState): MovingAction? {
        val allCoordinatesInDesiredOrder = getAllCoordinates().sortedWith(comparator)
        
        return allCoordinatesInDesiredOrder.fold(null as MovingAction?) { action, coord ->
            action ?: tryAllCombinationsToReachDesiredTile(playerState,
                isTileWeWant = { coords -> coords == coord })
        }
    }

    private fun tryAllCombinationsToReachDesiredTile(playerState: PlayerState, isTileWeWant: (Coordinates) -> Boolean): MovingAction? {
        return findFirstRowSlideActionIfAny(playerState, isTileWeWant)
            ?: findFirstColumnSlideActionIfAny(playerState, isTileWeWant)
    }

    private fun findFirstRowSlideActionIfAny(playerState: PlayerState, isTileWeWant: (Coordinates) -> Boolean): MovingAction? {
        val allRows = getAllRows()
        val allRowActionCombinations= getAllCombinations(allRows, HorizontalDirection.values())
        return allRowActionCombinations.fold(null as MovingAction?) { action, (rowPosition, direction, degree) ->
            action ?: slideRow(playerState, rowPosition, direction, degree, isTileWeWant)
        }
    }

    private fun findFirstColumnSlideActionIfAny(playerState: PlayerState, isTileWeWant: (Coordinates) -> Boolean): MovingAction? {
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
    private fun slideRow(playerState: PlayerState, position: RowPosition, direction: HorizontalDirection, degree: Degree, isTileWeWant: (Coordinates) -> Boolean): MovingAction? {
        return doSlideAndCheckReachable(playerState, player.copy(),
            doSlide = { state -> state.slideRowAndInsertSpare(position, direction, degree) },
            createAction = { RowAction(position, direction, degree, it) }, isTileWeWant)
    }

    /**
     * Slides a column in the given direction with the provided spare tile rotation. Checks if the goal or
     * the alternate tile is reachable.
     */
    private fun slideCol(playerState: PlayerState, position: ColumnPosition, direction: VerticalDirection, degree: Degree, isTileWeWant: (Coordinates) -> Boolean): MovingAction? {
        return doSlideAndCheckReachable(playerState, player.copy(),
            doSlide = { state -> state.slideColumnAndInsertSpare(position, direction, degree) },
            createAction = { ColumnAction(position, direction, degree, it) }, isTileWeWant)
    }

    /**
     * TODO: java doc, note game state
     */
    private fun doSlideAndCheckReachable(
        playerState: PlayerState,
        player: Player,
        doSlide: (GameState) -> Unit,
        createAction: (Coordinates) -> MovingAction,
        isTileWeWant: (Coordinates) -> Boolean
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

    private fun getActionToReachTile(lastAction: MovingAction?, pos: Coordinates, isTileWeWant: (Coordinates) -> Boolean,
                                     createAction: (Coordinates) -> MovingAction): MovingAction? {
        if (!isTileWeWant(pos)) {
            return null
        }
        val action = createAction(pos)
        if (lastAction != null && action.isUndoingAction(lastAction)) {
            return null
        }
        return action
    }

    private fun getAllRows():List<RowPosition> {
        return (Position.MIN_ROW_INDEX until Position.MAX_ROW_INDEX step 2).map { RowPosition(it) }
    }

    private fun getAllCols():List<ColumnPosition> {
        return (Position.MIN_COL_INDEX until Position.MAX_COL_INDEX step 2).map { ColumnPosition(it) }
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
        for (rowIndex in Position.MIN_ROW_INDEX until Position.MAX_ROW_INDEX) {
            for (colIndex in Position.MIN_COL_INDEX until Position.MAX_COL_INDEX) {
                allCoordinates.add(Coordinates(RowPosition(rowIndex), ColumnPosition((colIndex))))
            }
        }
        return allCoordinates
    }
}