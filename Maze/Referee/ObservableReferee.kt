package Referee

import Common.GameState
import Common.board.Board
import Common.player.PlayerData
import Players.PlayerMechanism
import Players.Referee

/**
 * A referee implementation that has multiple subscribed observers. After evert player turn, it will notify observers
 * of the new game state. After a game is over, it will notify observers that the game is over.
 */
class ObservableReferee(
    private val listOfObserver: List<ObserverMechanism>
): Referee() {
    override fun createStateFromChosenBoard(suggestedBoards: List<Board>, players: List<PlayerMechanism>): GameState {
        TODO("Not yet implemented")
    }

    override fun runRoundSafely(currentPlayer: PlayerData, currentMechanism: PlayerMechanism, state: GameState): GameState {
        val newState = super.runRoundSafely(currentPlayer, currentMechanism, state)
        listOfObserver.forEach {
            observerMechanism ->  observerMechanism.updateState(newState.toPublicState())
        }
        return newState
    }

    override fun playGame(initialState: GameState, players: List<PlayerMechanism>): Map<String, Boolean> {
        val endGameData= super.playGame(initialState, players)
        listOfObserver.forEach {
            observerMechanism -> observerMechanism.gameOver()
        }
        return endGameData
    }

}