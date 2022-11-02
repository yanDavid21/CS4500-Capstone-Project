package Client.javafx

import Common.GameState

interface MazeObserverInterface {
    fun next(): GameState?

    fun save(filepath: String)
}

interface MazePlayerInterface: MazeObserverInterface {
    fun submitMove()
}