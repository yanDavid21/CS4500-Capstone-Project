package Players

import Common.Action
import Common.GameState
import Common.Skip
import Common.SlideRowRotateAndInsert
import Common.board.*
import Common.player.Player
import Common.tile.*

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
        return moveToGoalIfReachable(board, spareTile)
            ?: findAllAlternativesThatAllowMove(board, spareTile)
            ?: Skip
    }

    protected fun moveToGoalIfReachable(board: Board, spare: GameTile): Action? {
        val reachablesTiles = board.getReachableTiles(player.currentPosition)
        for (reachable in reachablesTiles) {
            if (isTileValidGoal(board.getTile(reachable))) {
                return actionThatAllowsMoveTo(reachable)
            }
        }
        return null
    }

    protected fun findAllAlternativesThatAllowMove(board: Board, spareTile: GameTile): Action? {
        val allCoordinates = getAllCoordinates().sortedWith(comparator)
        for (coord in allCoordinates) {
            val maybeAction = tryAllCombinationsToReachThisTile(board, spareTile, isTileValidGoal, board.getTile(coord))
            if (maybeAction != null) {
                return maybeAction
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

    }

    protected fun tryAllCombinationsToReachThisTile(board: Board, spare: GameTile, alternate: GameTile): Action? {
        return forAllRows { position, direction, degree ->
            val state = GameState(board, spare, listOf(player)) // TODO: state must be immutable, currently changes player pos
            doSlideAndCheckReachable(state, position, direction, degree, alternate)
        }
            ?: forAllCols { position, direction, degree ->
                val state = GameState(board, spare, listOf(player))
                doSlideAndCheckReachable(state, position, direction, degree, alternate)
            }
    }


    private fun doSlideAndCheckReachable(
        state: GameState,
        position: Position,
        direction: Direction,
        degree: Degree,
        alternate: GameTile
    ): Action? {
        state.slideRowAndInsertSpare(position, direction, degree)
        val newBoard = state.getBoard()
        val reachablePositions = newBoard.getReachableTiles(player.currentPosition)
        return reachablePositions.fold<Coordinates, Action?>(null) { answ, reachablePos ->
            val tileAtPos = newBoard.getTile(reachablePos)
            if (isTileValidGoal(tileAtPos) || tileAtPos == alternate) {
                SlideRowRotateAndInsert(position, direction, degree, reachablePos)
            } else {
                answ
            }
        }
    }

    private fun forAllRows(performAction: (Position, Direction, Degree) -> Action?): Action? {
        val rowPositions = (Position.MIN_ROW_INDEX until Position.MAX_ROW_INDEX).map { RowPosition(it) }
        return forAllCombinations(
            rowPositions,
            arrayOf(HorizontalDirection.LEFT, HorizontalDirection.RIGHT)
        ) { position, direction, degree ->
            performAction(position, direction, degree)
        }
    }

    private fun forAllCols(performAction: (Position, Direction, Degree) -> Action?): Action? {
        val columnPositions = (Position.MIN_COL_INDEX until Position.MAX_COL_INDEX).map { ColumnPosition(it) }
        return forAllCombinations(
            columnPositions,
            arrayOf(VerticalDirection.UP, VerticalDirection.DOWN)
        ) { position, direction, degree ->
            performAction(position, direction, degree)
        }
    }


    private fun forAllCombinations(
        positions: List<Position>, directions: Array<Direction>,
        toPerform: (Position, Direction, Degree) -> Action?
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