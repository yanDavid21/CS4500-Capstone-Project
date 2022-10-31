package Referee

import Common.GameState


interface Observer {

    fun start()

    fun updateState(newState: GameState)

    fun gameOver()
}