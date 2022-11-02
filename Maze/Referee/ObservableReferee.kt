package Referee

import Common.GameState
import Common.board.Board
import Common.player.PlayerData
import Players.PlayerMechanism
import Players.Referee

class ObservableReferee(
    private val listOfObserver: List<ObserverMechanism>
): Referee() {
    override fun createStateFromChosenBoard(suggestedBoards: List<Board>, players: List<PlayerMechanism>): GameState {
        TODO("Not yet implemented")
    }

    override fun runRoundSafely(currentPlayer: PlayerData, currentMechanism: PlayerMechanism, state: GameState): GameState {
        val newState = super.runRoundSafely(currentPlayer, currentMechanism, state)
        listOfObserver.forEach {
            observerMechanism ->  observerMechanism.updateState(newState)
        }
        return newState
    }

}