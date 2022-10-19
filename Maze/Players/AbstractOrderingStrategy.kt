package Players

import Common.*
import Common.board.*
import Common.player.Player
import Common.tile.Degree
import Common.tile.GameTile
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
    override fun decideMove(board: Board, spareTile: GameTile): Action {
        return moveToGoalIfReachable(board, spareTile)
            ?: tryToReachAllAlternativeTiles(board, spareTile)
            ?: Skip
    }

    private fun moveToGoalIfReachable(board: Board, spareTile: GameTile): MovingAction? {
        val isTileValidGoal: (GameTile) -> Boolean = { tile ->
            player.treasureFound && tile == player.homeTile || !player.treasureFound && tile.treasure == player.goal
        }
        return tryAllCombinationsToReachDesiredTile(board, spareTile, isTileValidGoal)
    }
    

    private fun tryToReachAllAlternativeTiles(board: Board, spareTile: GameTile): MovingAction? {
        val allCoordinatesInDesiredOrder = getAllCoordinates().sortedWith(comparator)
        
        return allCoordinatesInDesiredOrder.fold(null as MovingAction?) { action, coord ->
            action ?: tryAllCombinationsToReachDesiredTile(board, spareTile,
                isTileWeWant = { tile -> tile == board.getTile(coord)})
        }
    }

    protected fun tryAllCombinationsToReachThisTile(board: Board, spare: GameTile, alternate: GameTile): Action? {
        return forAllRowsDoWithAllDirections { position, direction, degree ->
            slideRow(board, spare, position, direction, degree, alternate)
        }
            ?: forAllColsDoWithAllDirections { position, direction, degree ->
                slideCol(board, spare, position, direction, degree, alternate)
            }
    }

    private fun findFirstRowSlideActionIfAny(board: Board, spare: GameTile, isTileWeWant: (GameTile) -> Boolean): MovingAction? {
        val allRows = getAllRows()
        val allRowActionCombinations= getAllCombinations(allRows, HorizontalDirection.values())
        return allRowActionCombinations.fold(null as MovingAction?) { action, (rowPosition, direction, degree) ->
            action ?: slideRow(board, spare, rowPosition, direction, degree, isTileWeWant)
        }
    }

    private fun findFirstColumnSlideActionIfAny(board: Board, spare: GameTile, isTileWeWant: (GameTile) -> Boolean): MovingAction? {
        val allCols = getAllCols()
        val allColumnActionCombinations = getAllCombinations(allCols, VerticalDirection.values())
        return allColumnActionCombinations.fold(null as MovingAction?) { answ, (colPosition, direction, degree) ->
            answ ?: slideCol(board, spare, colPosition, direction, degree, isTileWeWant)
        }
    }

    /**
     * Slides a row in the given direction with the provided spare tile rotation. Checks if the goal or
     * the alternate tile is reachable.
     */
    private fun slideRow(board: Board, spare: GameTile, position: RowPosition, direction: HorizontalDirection, degree: Degree, isTileWeWant: (GameTile) -> Boolean): MovingAction? {
        return doSlideAndCheckReachable(board, spare, player.copy(), 
            doSlide = { state -> state.slideRowAndInsertSpare(position, direction, degree) },
            createAction = { RowAction(position, direction, degree, it) }, isTileWeWant)
    }

    /**
     * Slides a column in the given direction with the provided spare tile rotation. Checks if the goal or
     * the alternate tile is reachable.
     */
    private fun slideCol(board: Board, spare: GameTile, position: ColumnPosition, direction: VerticalDirection, degree: Degree, isTileWeWant: (GameTile) -> Boolean): MovingAction? {
        return doSlideAndCheckReachable(board, spare, player.copy(), 
            doSlide = { state -> state.slideColumnAndInsertSpare(position, direction, degree) },
            createAction = { ColumnAction(position, direction, degree, it) }, isTileWeWant)
    }

    private fun doSlideAndCheckReachable(
        board: Board,
        spareTile: GameTile,
        player: Player,
        alternate: GameTile,
        doSlide: (GameState) -> Unit,
        createAction: (Coordinates) -> Action
    ): Action? {
        val state = GameState(board, spareTile, listOf(player))
        doSlide(state)
        val newBoard = state.getBoard()
        val reachablePositions = newBoard.getReachableTiles(player.currentPosition)
        return reachablePositions.fold(null as MovingAction?) { action, reachablePos ->
            action ?: getActionToReachTile(board, reachablePos, isTileWeWant, createAction)
        }
    }

    private fun getActionToReachTile(board: Board, pos: Coordinates, isTileWeWant: (GameTile) -> Boolean,
                                     createAction: (Coordinates) -> MovingAction): MovingAction? {
        val tile = board.getTile(pos)
        return if (isTileWeWant(tile)) createAction(pos) else null
    }

    private fun getAllRows():List<RowPosition> {
        return (Position.MIN_ROW_INDEX until Position.MAX_ROW_INDEX).map { RowPosition(it) }
    }

    private fun getAllCols():List<ColumnPosition> {
        return (Position.MIN_COL_INDEX until Position.MAX_COL_INDEX).map { ColumnPosition(it) }
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