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
    protected val isTileValidGoal: (GameTile) -> Boolean = { tile ->
        player.treasureFound && tile == player.homeTile || !player.treasureFound && tile.treasure == player.goal
    }

    override fun decideMove(board: Board, spareTile: GameTile): Action {
        return moveToGoalIfReachable(board)
            ?: findAllAlternativesThatAllowMove(board, spareTile)
            ?: Skip
    }

    protected fun moveToGoalIfReachable(board: Board): Action? {
        val reachablesTiles = board.getReachableTiles(player.currentPosition)
        for (reachable in reachablesTiles) {
            if (isTileValidGoal(board.getTile(reachable))) {
                return actionThatAllowsMoveTo(reachable)
            }
        }
        return null
    }

    /**
     * Creates a move action that allows the player to reach the given coordinates.
     * It does an arbitrary column/row slide that does not affect the path from the player to the
     * coordinate and that does not repeat the last action performed on the board.
     */
    protected fun actionThatAllowsMoveTo(coordinates: Coordinates): Action {
        // TODO: do!
        return SlideColumnRotateAndInsert(ColumnPosition(6), VerticalDirection.DOWN, Degree.ZERO, coordinates)
    }

    protected fun findAllAlternativesThatAllowMove(board: Board, spareTile: GameTile): Action? {
        val allCoordinates = getAllCoordinates().sortedWith(comparator)
        return allCoordinates.fold<Coordinates, Action?>(null) { answ, coord ->
            answ ?: tryAllCombinationsToReachThisTile(board, spareTile, board.getTile(coord))
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

    /**
     * Slides a row in the given direction with the provided spare tile rotation. Checks if the goal or
     * the alternate tile is reachable.
     */
    private fun slideRow(board: Board, spare: GameTile, position: RowPosition, direction: HorizontalDirection, degree: Degree, alternate: GameTile): Action? {
        return doSlideAndCheckReachable(board, spare, player.copy(), alternate,
            { state -> state.slideRowAndInsertSpare(position, direction, degree) },
            { SlideRowRotateAndInsert(position, direction, degree, it) })
    }

    /**
     * Slides a column in the given direction with the provided spare tile rotation. Checks if the goal or
     * the alternate tile is reachable.
     */
    private fun slideCol(board: Board, spare: GameTile, position: ColumnPosition, direction: VerticalDirection, degree: Degree, alternate: GameTile): Action? {
        return doSlideAndCheckReachable(board, spare, player.copy(), alternate,
            { state -> state.slideColumnAndInsertSpare(position, direction, degree) },
            { SlideColumnRotateAndInsert(position, direction, degree, it) })
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
        return reachablePositions.fold<Coordinates, Action?>(null) { answ, reachablePos ->
            val tileAtPos = newBoard.getTile(reachablePos)
            if (answ == null && (isTileValidGoal(tileAtPos) || tileAtPos == alternate)) {
                createAction(reachablePos)
            } else {
                answ
            }
        }
    }

    private fun forAllRowsDoWithAllDirections(performAction: (RowPosition, HorizontalDirection, Degree) -> Action?): Action? {
        return forAllCombinations(
            (Position.MIN_ROW_INDEX until Position.MAX_ROW_INDEX).map { RowPosition(it) },
            HorizontalDirection.values()
        ) { position, direction, degree ->
            performAction(position, direction, degree)
        }
    }

    private fun forAllColsDoWithAllDirections(performAction: (ColumnPosition, VerticalDirection, Degree) -> Action?): Action? {
        return forAllCombinations(
            (Position.MIN_COL_INDEX until Position.MAX_COL_INDEX).map { ColumnPosition(it) },
            VerticalDirection.values()
        ) { position, direction, degree ->
            performAction(position, direction, degree)
        }
    }


    private fun <P, D> forAllCombinations(
        positions: List<P>, directions: Array<D>,
        toPerform: (P, D, Degree) -> Action?
    ): Action? {
        for (position in positions) {
            for (direction in directions) {
                for (degree in Degree.values()) {
                    val maybeAction = toPerform(position, direction, degree)
                    if (maybeAction != null) {
                        return maybeAction
                    }
                }
            }
        }
        return null
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