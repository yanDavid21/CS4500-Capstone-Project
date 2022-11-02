package Client

import Client.javafx.renderGameState
import Common.GameState
import Common.board.ColumnPosition
import Common.board.Coordinates
import Common.board.RowPosition
import Common.getNext
import Referee.ObserverMechanism
import serialization.data.BoardDTO
import serialization.data.StateDTO
import serialization.data.TileDTO
import javafx.fxml.FXML
import javafx.scene.Parent
import javafx.scene.control.Button
import javafx.scene.layout.HBox
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.stage.FileChooser
import testing.BoardTest
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
            val currentState = gamestates.first().toPublicState()

            val board = currentState.board
            val allTiles = RowPosition.getAll().map { row ->
                ColumnPosition.getAll().map { col ->
                    board.getTile(Coordinates(row, col))
                }
            }
            val serializedBoard = BoardDTO(
                allTiles.map { row -> row.map { it.toString() } },
                allTiles.map { row -> row.map { listOf(it.treasure.gem1.toString(), it.treasure.gem2.toString()) } }
            )

            val spareTile = currentState.spareTile
            val serializedSpareTile = TileDTO(
                spareTile.toString(),
                spareTile.treasure.gem1.toString(),
                spareTile.treasure.gem2.toString()
            )



            val serializedState = StateDTO(
                serializedBoard, serializedSpareTile,
            )
        }
        TODO("Not yet implemented")
    }
}