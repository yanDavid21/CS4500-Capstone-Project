package Client

import Common.GameState
import Common.getNext
import Referee.ObserverMechanism
import com.google.gson.Gson
import serialization.converters.GameStateConverter


class LocalStateObserver(state: GameState): ObserverMechanism {
    private var gamestates = listOf(state)
    private var isGameOver = false

    override fun updateState(newState: GameState) {
        gamestates = gamestates + listOf(newState)
    }

    override fun gameOver() {
        isGameOver = true
    }

    // CLIENT REQUESTS
    fun next(): GameState? {
        if (gamestates.isNotEmpty()) {
            val first = gamestates.first()
            gamestates = gamestates.getNext()
            return first
        }
        return null
    }


    fun save(filepath: String) {
        if (gamestates.isNotEmpty()) {
            val currentState = gamestates.first()

            val serializedState = GameStateConverter.serializeGameState(currentState)

            val stateAsJson = Gson().toJson(serializedState)
            //TODO("Not yet implemented")  save to path
        }
    }
}