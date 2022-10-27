package Players

import Common.*
import Common.TestData.createBoard
import Common.board.ColumnPosition
import Common.board.Coordinates
import Common.board.RowPosition
import Common.player.BaseColor
import Common.player.PlayerData
import Common.player.PublicPlayerData
import Common.tile.*
import java.util.*
import org.junit.jupiter.api.Test
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertFalse
import Common.tile.treasure.Gem
import Common.tile.treasure.Treasure
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalArgumentException
import kotlin.test.assertEquals
import kotlin.test.assertTrue


internal class PlayerMechanismTest {


    //checks all tiles are unique
    @Test
    fun proposeBoardValid() {
        val player = createPlayerMechanismRandom()
        val board = player.proposeBoard0(7,7)
        val seenBefore = mutableSetOf<GameTile>()
        for (element in board) {
            for (col in 0 until board[0].size) {
                val tile = element[col]
                if (seenBefore.contains(tile)) {
                    throw IllegalStateException("All tiles must be unique.")
                } else {
                    seenBefore.add(tile)
                }
            }
        }
        return
    }

    // tests that boards are dependent on the random seed
    @Test
    fun proposeBoardRandom() {

        val player1 = createPlayerMechanismWithSeed(5L)
        val player2 = createPlayerMechanismWithSeed(1L)
        val player3 = createPlayerMechanismWithSeed(1L)

        val board1 = player1.proposeBoard0(5,5)
        val board2 = player1.proposeBoard0(5,5)
        val board3 = player1.proposeBoard0(5,5)

        assertArrayEquals(board2, board3)
        assertFalse(board1.contentEquals(board2))
    }

    @Test
    fun proposeBoardInvalid() {
        val player1 = createPlayerMechanismWithSeed(5L)
        val player2 = createPlayerMechanismWithSeed(5L)
        assertThrows<IllegalArgumentException> {
            player1.proposeBoard0(-1, -1)
        }
        assertThrows<IllegalArgumentException> {
            player2.proposeBoard0(0, -12412)
        }
    }

    @Test
    fun setupAndUpdateGoalHome() {
        val player = RandomBoardRiemannPlayerMechanism("Jose")
        player.setupAndUpdateGoal(gamestate.toPublicState(), Coordinates.fromRowAndValue(0,2))
        val newGamestate = gamestate.slideColumnAndInsertSpare(ColumnPosition(6), VerticalDirection.DOWN,Degree.TWO_SEVENTY, Coordinates.fromRowAndValue(0,2))
        assertEquals(newGamestate.getActivePlayer().getGoal(), player.nextGoal)
    }

    @Test
    fun setupAndUpdateGoalTreasure() {
        val player = RandomBoardRiemannPlayerMechanism("Jose")
        player.setupAndUpdateGoal(PublicGameState(
            createBoard(), tile1,
            null, mapOf(Pair("Jose", publicPlayerData))), Coordinates.fromRowAndValue(0,2))
        assertEquals(player.nextGoal, Coordinates.fromRowAndValue(0,2))
    }

    @Test
    fun testTakeTurn() {
        val player1 = createPlayerMechanism()
        assertEquals(player1.takeTurn(gamestate.toPublicState()),
            RowAction(RowPosition(1), HorizontalDirection.LEFT, Degree.ZERO, Coordinates.fromRowAndValue(0,2)))
    }

    @Test
    fun testTakeTurnPass() {
        val player1 = createPlayerMechanism()
        val playerData1 = PlayerData("Jose", Coordinates.fromRowAndValue(0,6),
            Coordinates.fromRowAndValue(0,1), Coordinates.fromRowAndValue(0,1),BaseColor.PURPLE)
        val gamestate = GameState(createBoard(), tile1, listOf(playerData1, playerData),
            null)

        assertEquals(player1.takeTurn(gamestate.toPublicState()), Skip)
    }

    @Test
    fun testWon() {
        val player = createRandomPlayerMechanism()
        player.won(true)
        assertTrue(player.hasWon)
        player.won(false)
        assertFalse(player.hasWon)
    }

    companion object {
        fun createPlayerMechanism(): PlayerMechanism {
            return RandomBoardRiemannPlayerMechanism("Jose", 0L)
        }

        fun createPlayerMechanismRandom(): PlayerMechanism {
            return RandomBoardRiemannPlayerMechanism("Jose", Random().nextLong())
        }

        fun createPlayerMechanismWithSeed(randomSeed: Long): PlayerMechanism {
            return RandomBoardRiemannPlayerMechanism("Jose", randomSeed)
        }

        fun createRandomPlayerMechanism(): RandomBoardRiemannPlayerMechanism {
            return RandomBoardRiemannPlayerMechanism("Jose", 0L)
        }

        val tile1 = GameTile(Path.T, Degree.TWO_SEVENTY, Treasure(Gem.RAW_BERYL, Gem.ZIRCON))
        val publicPlayerData = PublicPlayerData("Jose", Coordinates.fromRowAndValue(0,1),
            Coordinates.fromRowAndValue(0,1), BaseColor.PURPLE)
        val playerData = PlayerData("Jose", Coordinates.fromRowAndValue(0,1),
            Coordinates.fromRowAndValue(0,1), Coordinates.fromRowAndValue(0,1),BaseColor.PURPLE)
        val gamestate = GameState(createBoard(), tile1, listOf(playerData),
            null)
    }
}