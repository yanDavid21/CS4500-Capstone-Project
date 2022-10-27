package Referee

import Common.GameState
import Common.TestData
import Common.board.Board
import Players.PlayerMechanism
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.mock

internal class RefereeTest {

    @Mock
    val player1 = mock(PlayerMechanism::class.java)

    @Mock
    val player2 = mock(PlayerMechanism::class.java)

    @Mock
    val player3 = mock(PlayerMechanism::class.java)

    /**
     * Test cases:
     *  - Sends propose board in order to player mechanisms
     *  - Does not include player that throws exception when requesting board
     *  - Validates boards correctly
     *  - Runs a game where player 1 can find treasure then goal and win
     *  - Runs a game where all players pass, find winner
     *  - Runs a game where a player gets kicked out (does not receive data)
     *  - Player throws exception
     *  - Player times out
     */


    @Test
    fun testSendProposeBoard() {

    }
}

class TestableReferee: Referee() {
    override fun createStateFromChosenBoard(suggestedBoards: List<Board>, players: List<PlayerMechanism>): GameState {
        val board = if (suggestedBoards.isEmpty()) suggestedBoards.first() else TestData.createBoard()
        return TestData.createReferee(board)

    }
}