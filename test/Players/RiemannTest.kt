package Players

import Common.Action
import Common.ColumnAction
import Common.RowAction
import Common.TestData
import Common.board.ColumnPosition
import Common.board.Coordinates
import Common.board.RowPosition
import Common.tile.*
import Common.tile.treasure.Gem
import Common.tile.treasure.Treasure
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
        val move = getMoveWithPlayerAndTreasureAtPosition(Coordinates.fromRowAndValue(0, 1),
            Coordinates.fromRowAndValue(0, 2), TestData.createSpareTile())

        val expectedMove = RowAction(RowPosition(0), HorizontalDirection.LEFT, Degree.ZERO,
            Coordinates.fromRowAndValue(0, 1))

        assertEquals(expectedMove, move)
    }

    @Test
    fun testFindHomeIsImmediatlyReachable() {
        val board = TestData.createBoard()

        // when player slides left, it can reach home
        val player = TestData.createPlayer(
            Coordinates.fromRowAndValue(1, 2),
            Coordinates.fromRowAndValue(1, 2),
            Coordinates.fromRowAndValue(2,1)
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
        val move = getMoveWithPlayerAndTreasureAtPosition(Coordinates.fromRowAndValue(2, 3),
            Coordinates.fromRowAndValue(2, 2), TestData.createSpareTile())

        val expected = ColumnAction(ColumnPosition(2), VerticalDirection.DOWN, Degree.ZERO,
            Coordinates.fromRowAndValue(3,2))

        assertEquals(expected, move)
    }

    @Test
    fun testFindTreasureIsReachableAfterPlayerKnockedOff() {
        val move = getMoveWithPlayerAndTreasureAtPosition(
            Coordinates.fromRowAndValue(2,6),
            Coordinates.fromRowAndValue(3, 0),
            GameTile(Path.UP_RIGHT, Degree.ZERO, Treasure(Gem.AMETHYST, Gem.AMETRINE))
        )

        assertEquals(
            RowAction(RowPosition(2), HorizontalDirection.RIGHT, Degree.ONE_EIGHTY, Coordinates.fromRowAndValue(3,0)),
            move
        )
    }

    @Test
    fun testGetToTopLeftWhenTreasureIsNotReachable() {
        val board = TestData.createBoard(tiles)

        val player = TestData.createPlayer(
            Coordinates.fromRowAndValue(0, 3),
            Coordinates.fromRowAndValue(5,5),
            Coordinates.fromRowAndValue(5,5)
        )

        val strategy = Riemann(player)

        val move = strategy.decideMove(board, GameTile(Path.VERTICAL,Degree.ZERO, Treasure(Gem.AMETRINE, Gem.GOLDSTONE)))

        val expected = ColumnAction(ColumnPosition(2), VerticalDirection.DOWN, Degree.NINETY, Coordinates.fromRowAndValue(0, 1))
        assertEquals(expected, move)
    }

    @Test
    fun testStrategyDoesNotUnmoveAction() {

    }

    private fun getMoveWithPlayerAndTreasureAtPosition(playerPosition: Coordinates,
                                                       treasurePosition: Coordinates,
                                                       spare: GameTile): Action {
        val board = TestData.createBoard()
        val player = TestData.createPlayer(
            playerPosition,
            board.getTile(treasurePosition).treasure,
            board.getTile(Coordinates.fromRowAndValue(1,1))
        )

        val strategy = Riemann(player)

        return strategy.decideMove(board, spare)
    }

    private val tiles = listOf(
        listOf("┐","└","│","─","┐","└","┌"),
        listOf("─","┘","│","┘","┬","├","┴"),
        listOf("┐","─","│","┤","┼","│","─"),
        listOf("┤","└","┌","┘","┬","├","┴"),
        listOf("┘","┼","│","─","┐","└","┌"),
        listOf("─","┬","├","┴","┤","┼","│"),
        listOf("┤","┐","└","┌","┘","┬","├"))
}