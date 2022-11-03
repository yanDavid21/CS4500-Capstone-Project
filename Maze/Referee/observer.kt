package Referee

import Common.PublicGameState


/**
 * Represents a Maze observer. A referee will notify it of the latest state so that it can display on demand.
 */
interface ObserverMechanism {

    fun updateState(newState: PublicGameState)

    fun gameOver()
}