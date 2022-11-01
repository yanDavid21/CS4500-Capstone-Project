package Referee

import Common.GameState
import Common.board.Board
import Common.player.PlayerData
import Players.PlayerMechanism
import Players.Referee

class ObservableReferee(
    val observer: Observer
): Referee() {
    override fun createStateFromChosenBoard(suggestedBoards: List<Board>, players: List<PlayerMechanism>): GameState {
        TODO("Not yet implemented")
    }

    override fun runRoundSafely(currentPlayer: PlayerData, currentMechanism: PlayerMechanism, state: GameState): GameState {
        val newState = super.runRoundSafely(currentPlayer, currentMechanism, state)
        observer.updateState(newState)
        return newState
    }

}