package Referee

import Client.ClientController
import Client.LocalStateController
import Client.javafx.JavaFXObserverView
import Common.GameState

class LocalStateObserver(initialGameState: GameState): ObserverMechanism {
    private val client: LocalStateController = LocalStateController(initialGameState, JavaFXObserverView())

    override fun updateState(newState: GameState) {
        client.updateQueueOfGameStates(newState)
    }

    override fun gameOver() {
        client.notifyGameOver()
    }
}