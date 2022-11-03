package Client

import Client.javafx.renderGameState
import Common.PublicGameState
import Common.getNext
import Referee.ObserverMechanism
import com.google.gson.GsonBuilder
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.layout.HBox
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import serialization.converters.PublicGameStateConverter
import java.io.File
import java.io.FileWriter

/**
 * A JavaFX implementation of an observer that observes a local game.
 */
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
    private var gamestates = listOf<PublicGameState>()
    private var isGameOver = false

    override fun updateState(newState: PublicGameState) {
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
            displayState(first)
        }
    }

    fun displayState(gameState: PublicGameState) {
        val (board, spareTile) = renderGameState(gameState)
        this.board.children.clear()
        this.board.children.addAll(board.children)
        this.spareTile.children.clear()
        this.spareTile.children.addAll(spareTile.children)
    }

    /**
     * Serializes the currently displayed state and saves it to the specified file.
     */
    fun save(file: File) {
        if (gamestates.isNotEmpty()) {
            val currentState = gamestates.first()

            val serializedState = PublicGameStateConverter.serializeGameState(currentState)

            val fileWriter = FileWriter(file)
            val gson = GsonBuilder()
                .serializeNulls()
                .create()
            fileWriter.write(gson.toJson(serializedState))
            fileWriter.close()
        }
    }

}