package Client

import Client.javafx.JavaFXObserverView
import Client.javafx.MazeObserverInterface
import Common.GameState
import Referee.ObserverMechanism
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.io.OutputStreamWriter

class StreamController(input: InputStream, output: OutputStream, view: JavaFXObserverView):
    ClientController,
    MazeObserverInterface {

    override fun run() {
//        val reader = InputStreamReader(input)
//        val writer = OutputStreamWriter(output)
//
//        while (reader.hasNext()) {
//            // parse json
//            // get the state
//            if (state is gameover) {
//                view.gameOver()
//            } else {
//                view.updateState(state)
//            }
//
//        }
    }

    override fun next(): GameState {
        TODO("Not yet implemented")
    }

    override fun save(filepath: String) {
        TODO("Not yet implemented")
    }

}