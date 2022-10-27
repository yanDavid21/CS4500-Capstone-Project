package Players

import Common.Action
import Common.PublicGameState
import Common.board.Coordinates
import Common.player.Player
import Common.tile.Degree
import Common.tile.GameTile
import Common.tile.Path
import Common.tile.treasure.Gem
import Common.tile.treasure.Treasure
import java.util.*
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import Players.player
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertFalse



internal class PlayerMechanismTest {
    private val randomObj: Random = Random(randomSeed)
    private var hasWon: Boolean = false
    private var hasFoundTreasure: Boolean = false
    private lateinit var nextGoal: Coordinates
    private lateinit var playerData: Player


    //checks all tiles are unique
    @Test
    fun proposeBoardValid() {
        val player = createPlayerMechanismRandom()
        val board = player.proposeBoard0(7,7)
        val seenBefore = mutableSetOf()
        for (row in 0 until board.size) {
            for (col in 0 until board[0].size) {
                val tile = board[row][col]
                if (seenBefore.has(tile)) {
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

        val player1 = createPlayerMechanism(5L)
        val player2 = createPlayerMechanism(1L)
        val player3 = createPlayerMechanism()(1L)

        val board1 = player1.proposeBoard0(5,5)
        val board2 = player1.proposeBoard0(5,5)
        val board3 = player1.proposeBoard0(5,5)

        assertArrayEquals(board2, board3)
        assertFalse(Arrays.equals(board1, board2))
    }

    @Test
    fun proposeBoardInvalid() {
        val player1 = createPlayerMechanism(5L)

        assertThrows()c
    }

    @Test
    fun setupAndUpdateGoalHome() {

        val player = createPlayerMechanism()
        player.setupAndUpdateGoal(PublicGameState, )
    }

    @Test
    fun setupAndUpdateGoalTreasure() {

    }

    @Test
    fun testTakeTurn() {

    }

    @Test
    fun testTakeTurnPass() {

    }

    @Test
    fun testWon() {

    }

    companion object {
        fun createPlayerMechanism(): PlayerMechanism {
            return PlayerMechanismImpl("Jose", 0L)
        }

        fun createPlayerMechanismRandom(): PlayerMechanism {
            return PlayerMechanismImpl("Jose", Random().nextLong())
        }

        fun createPlayerMechanism(randomSeed: Long): PlayerMechanism {
            return PlayerMechanismImpl("Jose", randomSeed)
        }
    }
}