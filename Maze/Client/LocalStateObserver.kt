package Client

import Client.javafx.renderGameState
import Common.GameState
import Common.getNext
import Referee.ObserverMechanism
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.layout.HBox
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import serialization.converters.GameStateConverter
import java.io.File


class LocalStateObserver: ObserverMechanism {

    @FXML
    lateinit var board: VBox
    @FXML
    lateinit var parent: HBox
    @FXML
    lateinit var controlPanel: VBox
    @FXML
    lateinit var spareTile: StackPane
    @FXML
    lateinit var saveButton: Button
    @FXML
    lateinit var nextButton: Button
    private var gamestates = listOf<GameState>()
    private var isGameOver = false

    override fun updateState(newState: GameState) {
        gamestates = gamestates + listOf(newState)
    }

    override fun gameOver() {
        isGameOver = true
    }

    // CLIENT REQUESTS
    fun next() {
        if (gamestates.isNotEmpty()) {
            val first = gamestates.first()
            gamestates = gamestates.getNext()
            val (board, spareTile) = renderGameState(first)
            this.board = board
            this.spareTile = spareTile
        }
    }

    fun save(file: File) {
        if (gamestates.isNotEmpty()) {
            val currentState = gamestates.first()

            val serializedState = GameStateConverter.serializeGameState(currentState)


        }

    }
}