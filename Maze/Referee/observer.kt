package Referee

import Common.GameState


interface ObserverMechanism {

    fun updateState(newState: GameState)

    fun gameOver()
}