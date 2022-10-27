package Players

import Common.GameState
import Common.TestData
import Common.board.Board
import Players.SamplePlayerMechanisms.MisbehavingOnBoardRequest
import Players.SamplePlayerMechanisms.MisbehavingOnRound
import Players.SamplePlayerMechanisms.MisbehavingOnSetup
import Players.SamplePlayerMechanisms.PassingPlayerMechanism
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import kotlin.test.assertEquals

internal class RefereeTest {


    val player1 = TestData.createPlayer1()
    val player2 = TestData.createPlayer2()
    val player3 = TestData.createPlayer3()
    val state = TestData.createRefereeWithPlayers(player1, player2, player3)
    val initialPublicState = state.toPublicState()

    val referee = TestableReferee()


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
        val willAlwaysPassPlayers = listOf(
            PassingPlayerMechanism("player1"), PassingPlayerMechanism("player2"), PassingPlayerMechanism("player3")
        )

        referee.startGame(willAlwaysPassPlayers)

        assertEquals(listOf(initialPublicState, initialPublicState, initialPublicState), willAlwaysPassPlayers.map { it.receivedState })
    }

    @Test
    fun testPlayersWhoThrowExceptionDuringBoardRequestNotIncluded() {
        val playerMechanisms = listOf(
            PassingPlayerMechanism("player1"), MisbehavingOnBoardRequest("player2"), PassingPlayerMechanism("player3")
        )

        referee.startGame(playerMechanisms)

        val expectedState = TestData.createRefereeWithPlayers(player1, player3).toPublicState()
        // second player never received message
        assertEquals(
            listOf(expectedState, null, expectedState),
            playerMechanisms.map { it.receivedState }
        )
    }

    @Test
    fun testPlayersWhoThrowExceptionDuringInitialStateTransmissionDoNotPlay() {
        val playerMechanisms = listOf(
            PassingPlayerMechanism("player1"), PassingPlayerMechanism("player2"), MisbehavingOnSetup("player3")
        )

        referee.startGame(playerMechanisms)

        val expectedState = TestData.createRefereeWithPlayers(player1, player2, player3).toPublicState()

        assertEquals(
            listOf(expectedState, expectedState, null),
            playerMechanisms.map { it.receivedState }
        )
    }

    @Test
    fun testPlayersWhoThrowExceptionDuringGameIsKickedOut() {
        val playerMechanism = listOf(
            MisbehavingOnRound("player1"), PassingPlayerMechanism("player2"), PassingPlayerMechanism("player3")
        )

        assertDoesNotThrow { referee.startGame(playerMechanism) }
    }
}


class TestableReferee: Referee() {
    override fun createStateFromChosenBoard(suggestedBoards: List<Board>, players: List<PlayerMechanism>): GameState {
        val testPlayers = listOf(TestData.createPlayer1(), TestData.createPlayer2(), TestData.createPlayer3())
            .associateBy { it.id }
        val playerData = players.map { testPlayers[it.name] ?: throw IllegalStateException("Need test data for $it.name") }

        return TestData.createRefereeWithPlayers(playerData)
    }
}

