package Referee

import Common.GameState


interface Observer {

    fun updateState(newState: GameState)

    fun gameOver()

    fun giveFeatures(controller: IObserverController)
}