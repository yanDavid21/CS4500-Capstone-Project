package Players

import Common.GameState
import Common.TestData
import Common.board.Board
import Common.board.Coordinates
import Common.player.BaseColor
import Common.player.PlayerData
import Common.tile.Degree
import Common.tile.GameTile
import Common.tile.Path
import Common.tile.treasure.Gem
import Common.tile.treasure.Treasure
import Players.SamplePlayerMechanisms.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import kotlin.test.assertEquals
import kotlin.test.assertFalse

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

    @Test
    fun testPlayer1WinsEasily() {
        val riemannPlayers = createRiemannPlayers()
        val winabbleBoard = TestData.createBoard(TestData.easyToWinBoard)
        val gameState = GameState(winabbleBoard,
            TestData.createSpareTile(),
            listOf(player1, player2, player3),
        )

        val winningData = referee.playGame(gameState, riemannPlayers)

        assertEquals(
            mapOf("player1" to true, "player2" to false, "player3" to false),
            winningData
        )
    }

    @Test
    fun testPlayersAllPassNoOneFoundTreasure() {
        val stuckPlayers = listOf(
            PassingPlayerMechanism("player1"), PassingPlayerMechanism("player2"), PassingPlayerMechanism("player3")
        )
        val impossibleBoard = TestData.createBoard(TestData.impossibleBoard)

        val gameState = GameState(impossibleBoard, GameTile(Path.UP_RIGHT, Degree.NINETY,
            Treasure(Gem.GROSSULAR_GARNET, Gem.GOLDSTONE)),
            createStuckPlayersClosestToTreasure()
        )

        val endgameData = referee.playGame(gameState, stuckPlayers)

        assertEquals(
            mapOf("player1" to true, "player2" to false, "player3" to false),
            endgameData
        )
    }

    @Test
    fun testEveryPlayerGetsKickedOut() {
        val badPlayers = listOf(
            MisbehavingOnRound("player1"), MisbehavingOnRound("player2"), MisbehavingOnRound("player3")
        )

        val noWinners = referee.playGame(state, badPlayers)

        assertEquals(mapOf(), noWinners)
    }

    @Test
    fun testOnePlayerGetsKickedOut() {
        val players = listOf(
            RandomBoardRiemannPlayerMechanism("player1", player1.getGoal()),
            MisbehavingOnRound("player2"),
            PassingPlayerMechanism("player3")
        )

        val easyBoard = TestData.createBoard(TestData.easyToWinBoard)
        val state = GameState(easyBoard,
            TestData.createSpareTile(),
            listOf(player1, player2, player3))

        val endgame = referee.playGame(state, players)

        assertEquals(
            mapOf("player1" to true, "player3" to false),
            endgame
        )
    }

    @Test
    fun testAllPlayersPassWinnerFoundTreasureTied() {
        val stuckPlayers = listOf(
            PassingPlayerMechanism("player1"), PassingPlayerMechanism("player2"), PassingPlayerMechanism("player3")
        )
        val impossibleBoard = TestData.createBoard(TestData.impossibleBoard)

        val gameState = GameState(impossibleBoard, GameTile(Path.UP_RIGHT, Degree.NINETY,
            Treasure(Gem.GROSSULAR_GARNET, Gem.GOLDSTONE)),
            createStuckPlayersClosestToHome()
        )

        val endgameData = referee.playGame(gameState, stuckPlayers)

        assertEquals(
            mapOf("player3" to true, "player2" to true, "player1" to false),
            endgameData
        )
    }

    @Test
    fun testGracefulWhenPlayerMisbehavesOnWon() {
        val players = listOf(
            PassingPlayerMechanism("player1"), MisbehavingOnWon("player2"), MisbehavingOnWon("player3")
        )

        assertDoesNotThrow { referee.startGame(players) }
    }

    @Test
    fun playerTakesTooLongIsRemoved() {
        val players = listOf(
            PassingPlayerMechanism("david"), PlayerDoesNotReturn("Matthias"), PassingPlayerMechanism("Megan")
        )

        val endgame = referee.playGame(state, players)

        assertFalse(endgame.containsKey("Matthias"))

    }

    fun createRiemannPlayers(): List<RandomBoardRiemannPlayerMechanism> {
        return listOf(
            RandomBoardRiemannPlayerMechanism("player1", player1.getGoal()),
            RandomBoardRiemannPlayerMechanism("player2", player2.getGoal()),
            RandomBoardRiemannPlayerMechanism("player3", player3.getGoal()),
        )
    }

    private fun createStuckPlayersClosestToTreasure(): List<PlayerData> {
        return listOf(
            PlayerData("player1",
                Coordinates.fromRowAndValue(1, 1),
                Coordinates.fromRowAndValue(3, 3),
                Coordinates.fromRowAndValue(1, 1),
                BaseColor.BLACK
            ),
            PlayerData(
                "player2",
                Coordinates.fromRowAndValue(1, 5),
                Coordinates.fromRowAndValue(1, 1),
                Coordinates.fromRowAndValue(1, 5),
                BaseColor.PURPLE
            ),
            PlayerData(
                "player3",
                Coordinates.fromRowAndValue(5, 5),
                Coordinates.fromRowAndValue(1, 1),
                Coordinates.fromRowAndValue(5, 5),
                BaseColor.RED
            )
        )
    }

    private fun createStuckPlayersClosestToHome(): List<PlayerData> {
        return listOf(
            PlayerData("player3",
                Coordinates.fromRowAndValue(1, 1),
                Coordinates.fromRowAndValue(1, 1),
                Coordinates.fromRowAndValue(3, 3),
                BaseColor.BLACK,
                treasureFound = true
            ),
            PlayerData(
                "player1",
                Coordinates.fromRowAndValue(1, 5),
                Coordinates.fromRowAndValue(1, 1),
                Coordinates.fromRowAndValue(1, 5),
                BaseColor.PURPLE
            ),
            PlayerData(
                "player2",
                Coordinates.fromRowAndValue(3, 3),
                Coordinates.fromRowAndValue(5, 5),
                Coordinates.fromRowAndValue(1, 1),
                BaseColor.RED,
                treasureFound = true
            )
        )
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


