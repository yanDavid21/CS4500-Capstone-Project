package Referee

import java.io.OutputStream
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import Common.GameState

interface IObserverController {
    fun run()
}


class StateObserverController(state:GameState, view: Observer): IObserverController, MazeUserInterface {
    private var gamestates = listOf<GameState>(state)

    init {
        view.giveFeatures(this)
    }

    override fun run() {
        view.updateState(state
    }

    override fun next() {

    }

    fun updateQueueOfGameStates(gameState: GameState) {
        gamestates = gamestates + listOf(state)
    }

}



class StreamObserverController(input: InputStream, output: OutputStream, view: Observer): IObserverController, MazeUserInterface {

    init {
        view.giveFeatures(this)
    }

    override fun run() {
        val reader = InputStreamReader(input)
        val writer = OutputStreamWriter(output)

        while (reader.hasNext()) {
            // parse json
            // get the state
            if (state is gameover) {
                view.gameOver()
            } else {
                view.updateState(state)
            }

        }
    }

    override fun next() {

    }

}