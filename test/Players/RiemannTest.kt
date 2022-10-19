package Players

import Common.ColumnAction
import Common.RowAction
import Common.TestData
import Common.board.ColumnPosition
import Common.board.Coordinates
import Common.board.RowPosition
import Common.tile.Degree
import Common.tile.HorizontalDirection
import Common.tile.VerticalDirection
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class RiemannTest {
    /**
     * Test cases to consider:
     *
     *  - get right move when goal is immediatly reachable (right next to; or through some path)
     *  - get the right move when goal is one slide away
     *  - when goal can be reached after player is popped off
     *
     *  - alternate tile is reachable on first possibility
     *  -
     */

    @Test
    fun testFindMoveTreasurelIsImmediatlyReachable() {
        val board = TestData.createBoard()

        val player = TestData.createPlayer(
            Coordinates.fromRowAndValue(0, 1),
            board.getTile(Coordinates.fromRowAndValue(0, 2)).treasure,
            board.getTile(Coordinates.fromRowAndValue(1,1))
        )
        val strategy = Riemann(player)

        val move = strategy.decideMove(board, TestData.createSpareTile())

        val expectedMove = RowAction(RowPosition(0), HorizontalDirection.LEFT, Degree.ZERO,
            Coordinates.fromRowAndValue(0, 1))

        assertEquals(expectedMove, move)
    }

    @Test
    fun testFindHomeIsImmediatlyReachable() {
        val board = TestData.createBoard()

        // when player slides left, it can reach home
        val player = TestData.createPlayer(
            Coordinates.fromRowAndValue(0, 2),
            board.getTile(Coordinates.fromRowAndValue(0, 2)).treasure,
            board.getTile(Coordinates.fromRowAndValue(1,1))
        )
        player.treasureFound = true

        val strategy = Riemann(player)

        val move = strategy.decideMove(board, TestData.createSpareTile())

        val expectedMove = RowAction(RowPosition(0), HorizontalDirection.LEFT, Degree.ZERO,
            Coordinates.fromRowAndValue(1, 1))

        assertEquals(expectedMove, move)
    }

    @Test
    fun testFindTreasureIsReachableThroughPath() {
        val board = TestData.createBoard()
        val player = TestData.createPlayer(
            Coordinates.fromRowAndValue(2,3),
            board.getTile(Coordinates.fromRowAndValue(2,2)).treasure,
            board.getTile(Coordinates.fromRowAndValue(1,1))
        )

        val strategy = Riemann(player)

        val move = strategy.decideMove(board, TestData.createSpareTile())

        val expected = ColumnAction(ColumnPosition(2), VerticalDirection.DOWN, Degree.ZERO,
            Coordinates.fromRowAndValue(3,2))

        assertEquals(expected, move)
    }
}