package Players

import Common.GameState
import Common.TestData
import Common.board.Board
import Common.board.Position
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.*

internal class RefereeTest {

    @Mock
    val playerMechanism1 = mock(PlayerMechanism::class.java)

    @Mock
    val playerMechanism2 = mock(PlayerMechanism::class.java)

    @Mock
    val playerMechanism3 = mock(PlayerMechanism::class.java)

    val playerMechanisms = listOf(playerMechanism1, playerMechanism2, playerMechanism3)

    val player1 = TestData.createPlayer1()
    val player2 = TestData.createPlayer2()
    val player3 = TestData.createPlayer3()
    val state = TestData.createRefereeWithPlayers(player1, player2, player3)
    val initialPublicState = state.toPublicState()

    val referee = TestableReferee()

    init {
        `when`(playerMechanism1.name).thenReturn(player1.id)
        `when`(playerMechanism2.name).thenReturn(player2.id)
        `when`(playerMechanism3.name).thenReturn(player3.id)
    }

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
        // TODO: should we even do?
        `when`(playerMechanism1.proposeBoard0(Position.WIDTH, Position.HEIGHT)).thenReturn(TestData.createTiles())
        `when`(playerMechanism2.proposeBoard0(Position.WIDTH, Position.HEIGHT)).thenReturn(TestData.createTiles())
        `when`(playerMechanism3.proposeBoard0(Position.WIDTH, Position.HEIGHT)).thenReturn(TestData.createTiles())

        referee.startGame(playerMechanisms)

        verify(playerMechanism1, times(1)).proposeBoard0(Position.WIDTH, Position.HEIGHT)
        verify(playerMechanism1, times(1))
            .setupAndUpdateGoal(initialPublicState, player1.goalPosition)

        verify(playerMechanism2, times(1)).proposeBoard0(Position.WIDTH, Position.HEIGHT)
        verify(playerMechanism2, times(1))
            .setupAndUpdateGoal(initialPublicState, player2.goalPosition)

        verify(playerMechanism3, times(1)).proposeBoard0(Position.WIDTH, Position.HEIGHT)
        verify(playerMechanism3, times(1))
            .setupAndUpdateGoal(initialPublicState, player3.goalPosition)
    }
}


class TestableReferee: Referee() {
    override fun createStateFromChosenBoard(suggestedBoards: List<Board>, players: List<PlayerMechanism>): GameState {
        val board = if (suggestedBoards.isEmpty()) suggestedBoards.first() else TestData.createBoard()
        return TestData.createReferee(board)

    }
}