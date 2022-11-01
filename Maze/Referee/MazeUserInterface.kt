package Referee

import Common.GameState

interface MazeUserInterface {
    fun next(): GameState

    fun save(filepath: String)
}