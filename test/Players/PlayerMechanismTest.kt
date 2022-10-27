package Players

import Common.TestData
import Common.tile.GameTile
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertFalse
import org.junit.jupiter.api.Test
import java.util.*



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

        val player1 = createPlayerMechanism(5L)
        val player2 = createPlayerMechanism(1L)
        val player3 = createPlayerMechanism(1L)

        val board1 = player1.proposeBoard0(5,5)
        val board2 = player1.proposeBoard0(5,5)
        val board3 = player1.proposeBoard0(5,5)

        assertArrayEquals(board2, board3)
        assertFalse(board1.contentEquals(board2))
    }

    @Test
    fun proposeBoardInvalid() {
        val player1 = createPlayerMechanism(5L)

       // assertThrows()c
    }

    @Test
    fun setupAndUpdateGoalHome() {

        val player = createPlayerMechanism()
        //player.setupAndUpdateGoal(PublicGameState, )
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
            return RandomBoardRiemannPlayerMechanism("Jose", TestData.createPlayer1().goalPosition)
        }

        fun createPlayerMechanismRandom(): PlayerMechanism {
            return RandomBoardRiemannPlayerMechanism("Jose", TestData.createPlayer1().goalPosition, Random().nextLong())
        }

        fun createPlayerMechanism(randomSeed: Long): PlayerMechanism {
            return RandomBoardRiemannPlayerMechanism("Jose", TestData.createPlayer1().goalPosition, randomSeed)
        }
    }
}