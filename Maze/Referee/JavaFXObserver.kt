package Referee

import Common.GameState
import Common.board.ColumnPosition
import Common.board.Coordinates
import Common.board.RowPosition
import Common.tile.Degree
import Common.tile.VerticalDirection
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import javafx.application.Application
import javafx.application.Application.launch
import javafx.application.Platform
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.stage.Stage
import testing.BoardTest
import testing.RefereeState
import testing.getRefereeState
import java.io.InputStreamReader

// launch -> create RunGui instance -> init { static variable -> this current instance } -> RunGui.getSingletonInstance()

class JavaFXObserver(
    val gameState: GameState
): Observer, MazeUserInterface {
    private val receivedStates = mutableListOf(gameState)
    private var willReceiveMore = true

    override fun start() {
        RunGui.setInitialParent(renderGameState(gameState))
        launch(RunGui::class.java)
    }

    override fun updateState(newState: GameState) {
        receivedStates.add(newState)
    }

    override fun gameOver() {
        willReceiveMore = false
    }

    override fun next() {
        if (receivedStates.isNotEmpty()) {
            val currentState = receivedStates.first()
            Platform.runLater { RunGui.singletonInstance.setParentNode(renderGameState(currentState)) }
        }
    }

    override fun save() {
        if (receivedStates.isNotEmpty()) {
            val currentState = receivedStates.first()

            val board = currentState.getBoard()
            val allTiles = RowPosition.getAll().map { row ->
                ColumnPosition.getAll().map { col ->
                    board.getTile(Coordinates(row, col))
                }
            }

            val serializedBoard = BoardTest(
                allTiles.map { row -> row.map { it.path.symbol } },
                allTiles.map { row -> row.map { listOf(it.treasure.gem1.toString(), it.treasure.gem2.toString()) } }
            )


            //val serialized = RefereeState(
              //  serializedBoard,
            //)
        }
        TODO("Not yet implemented")
    }
}


/*
 *
 * GUI:
 *
 * - Designing a JavaFX component that renders a game state (must be constructed with game state)
 *
 * - Making an application that reads a state and player mechanisms from stdin, creates the above component
 *   renders it, and takes user input for next and save
 */

fun main() {
    val jsonReader = JsonReader(InputStreamReader(System.`in`, "UTF-8"))
    val gson = Gson()

    val playerSpec = gson.fromJson<List<List<String>>>(jsonReader, List::class.java)
    val refereeState = gson.fromJson<RefereeState>(jsonReader, RefereeState::class.java)

    val state = getRefereeState(refereeState, playerSpec.map { it[0] })

    val observer = JavaFXObserver(state)

    observer.start()
    val newState = state.slideColumnAndInsertSpare(ColumnPosition(0), VerticalDirection.DOWN, Degree.ZERO, Coordinates.fromRowAndValue(1, 1))
    observer.updateState(newState)
    observer.next()
}

private fun renderGameState(gameState: GameState): Parent {
    val board = gameState.getBoard()
    val horizontal = Rectangle(70.0, 20.0)
    val vertical = Rectangle(20.0, 70.0)

    horizontal.fill = Color.BLACK
    vertical.fill = Color.BLACK

    val sp = StackPane(horizontal, vertical)
    return sp
    // TODO: draw game state
}

// TODO: read from std. in, return corresponding gamestate
private fun createParentFromStdIn(): Parent {
    val jsonReader = JsonReader(InputStreamReader(System.`in`, "UTF-8"))
    val gson = Gson()

    val playerSpec = gson.fromJson<List<List<String>>>(jsonReader, List::class.java)
    val refereeState = gson.fromJson<RefereeState>(jsonReader, RefereeState::class.java)

    val state = getRefereeState(refereeState, playerSpec.map { it[0] })
    //val playerMechanisms = getPlayerMechanisms(playerSpec, state)
    //val referee = TestableReferee()

    return renderGameState(state)
}

class RunGui: Application() {

    private var parentNode: Parent

    init {
        parentNode = initialParentNode
        singletonInstance = this
    }

    override fun start(primaryStage: Stage) {
        primaryStage.run {
            scene = Scene(parentNode)
            show()
        }
    }

    fun setParentNode(parent: Parent) {
        parentNode = parent
    }

    companion object {
        lateinit var singletonInstance: RunGui
        private lateinit var initialParentNode: Parent
        
        fun setInitialParent(node: Parent) {
            initialParentNode = node
        }


    }
}

