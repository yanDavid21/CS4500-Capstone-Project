package Players

import Common.*
import Common.board.Board
import Common.board.ColumnPosition
import Common.board.Coordinates
import Common.board.RowPosition
import Common.player.Player
import Common.tile.*
import org.junit.jupiter.api.Test
import testing.TestUtils
import kotlin.test.assertEquals

abstract class AbstractOrderingStrategyTests {

    abstract fun createStrategy(player: Player): MazeStrategy


    @Test
    fun testFindMoveTreasurelIsImmediatlyReachable() {
        /*
        val move = getMoveWithPlayerAndTreasureAtPosition(Coordinates.fromRowAndValue(0, 1),
            Coordinates.fromRowAndValue(0, 2), TestData.createSpareTile())

        val expectedMove = RowAction(RowPosition(0), HorizontalDirection.LEFT, Degree.ZERO,
            Coordinates.fromRowAndValue(0, 1))

        assertEquals(expectedMove, move)

         */
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

        val move = strategy.decideMove(PlayerState(board, TestData.createSpareTile(), null))

        val expectedMove = RowAction(RowPosition(0), HorizontalDirection.LEFT, Degree.ZERO,
            Coordinates.fromRowAndValue(1, 1))

        assertEquals(expectedMove, move)
    }


    @Test
    fun testFindTreasureIsReachableThroughPath() {
        val move = getMoveWithPlayerAtPosition(
            playerPosition = Coordinates.fromRowAndValue(3, 2),
            treasurePosition = Coordinates.fromRowAndValue(5,5),
            homePosition = Coordinates.fromRowAndValue(5,5),
            makeBoard(),
            TestData.createSpareTile(),
            null
        )

        val expected = RowAction(
            RowPosition(4), HorizontalDirection.RIGHT, Degree.ZERO,
            Coordinates.fromRowAndValue(5, 5))

        assertEquals(expected, move)
    }


    @Test
    fun testFindTreasureIsReachableAfterPlayerKnockedOff() {
        val move = getMoveWithPlayerAtPosition(
            playerPosition = Coordinates.fromRowAndValue(0, 6),
            treasurePosition = Coordinates.fromRowAndValue(5,5),
            homePosition = Coordinates.fromRowAndValue(5,5),
            makeBoard(),
            TestData.createSpareTile(Path.UP_RIGHT),
            null
        )

        val expected = ColumnAction(
            ColumnPosition(6),
            VerticalDirection.UP,
            Degree.NINETY,
            Coordinates.fromRowAndValue(5, 5)
        )

        assertEquals(
            expected,
            move
        )
    }

    @Test
    fun testStrategyDoesNotUnmoveAction() {
        val move = getMoveWithPlayerAtPosition(
            playerPosition = Coordinates.fromRowAndValue(1,0),
            treasurePosition = Coordinates.fromRowAndValue(1,1),
            homePosition = Coordinates.fromRowAndValue(5, 5),
            makeBoard(),
            TestData.createSpareTile(),
            RowAction(RowPosition(0), HorizontalDirection.RIGHT, Degree.ZERO, Coordinates.fromRowAndValue(5, 4))
        )

        // left shift would undo previous action
        val expected = RowAction(
            RowPosition(0), HorizontalDirection.RIGHT, Degree.ZERO, Coordinates.fromRowAndValue(1, 1)
        )

        assertEquals(
            expected,
            move
        )
    }

    protected fun getMoveWithPlayerAtPosition(
        playerPosition: Coordinates,
        treasurePosition: Coordinates,
        homePosition: Coordinates,
        board: Board,
        spare: GameTile,
        lastAction: MovingAction?
    ): Action {
        val state = PlayerState(board, spare, lastAction)
        val player = TestData.createPlayer(
            playerPosition,
            treasurePosition,
            homePosition
        )

        val strategy = createStrategy(player)

        return strategy.decideMove(state)
    }

    protected fun makeBoard(): Board {
        val treasures = TestUtils.getTreasuresFromStrings(TestData.treasureStrings)
        return Board(TestUtils.getTilesFromConnectorsAndTreasures(tiles, treasures))
    }

    protected val tiles = listOf(
        listOf("┐","└","│","─","┐","└","┌"),
        listOf("└","─","│","┘","┬","├","┴"),
        listOf("┐","─","│","┤","┼","│","─"),
        listOf("┤","└","┌","┘","┬","├","┴"),
        listOf("┘","┼","─","─","┐","└","┌"),
        listOf("─","┬","├","┴","┤","┼","│"),
        listOf("┤","┐","└","┌","┘","┴","├"))
}