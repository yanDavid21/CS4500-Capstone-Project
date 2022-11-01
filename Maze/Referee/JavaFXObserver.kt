package Referee

import Common.GameState
import Common.board.ColumnPosition
import Common.board.Coordinates
import Common.board.RowPosition
import Players.PlayerMechanism
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import javafx.application.Application
import javafx.application.Application.launch
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.layout.HBox
import javafx.stage.Stage
import testing.BoardTest
import testing.RefereeState
import testing.getPlayerMechanisms
import testing.getRefereeState
import java.io.InputStreamReader

fun main() {
    launch(CommandLineRefereeApp::class.java)
}

fun getStateFromStdIn(): Pair<GameState, List<PlayerMechanism>> {
    val jsonReader = JsonReader(InputStreamReader(System.`in`, "UTF-8"))
    val gson = Gson()

    val playerSpec = gson.fromJson<List<List<String>>>(jsonReader, List::class.java)
    val refereeState = gson.fromJson<RefereeState>(jsonReader, RefereeState::class.java)

    val state = getRefereeState(refereeState, playerSpec.map { it[0] })

    val mechanisms = getPlayerMechanisms(playerSpec, state)
    return Pair(state, mechanisms)
}

class JavaFXObserver(
    gameState: GameState,
    private val stage: Stage
): Observer, MazeUserInterface {
    private val receivedStates = mutableListOf(gameState)
    private var willReceiveMore = true

    override fun start() {
    }

    override fun updateState(newState: GameState) {
        receivedStates.add(newState)
    }

    override fun gameOver() {
        willReceiveMore = false
    }

    override fun next(): GameState {
        if (receivedStates.isNotEmpty()) {
            return receivedStates.removeAt(0)
        }
        throw IllegalStateException("No more states")
    }

    override fun save(filename: String) {
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

class CommandLineRefereeApp: Application() {

    override fun start(primaryStage: Stage?) {

        primaryStage?.run {
            val (initialState, players) = getStateFromStdIn()
            val observer = JavaFXObserver(initialState, primaryStage)
            val controller = ObservableReferee(observer)

            val view = HBox().apply{
                children.add(renderGameState(initialState))

                val button = Button("Next")

                button.onAction = EventHandler { children[0] = renderGameState(observer.next()) }
                children.add(button)
            }

            scene = Scene(view)

            show()

            println(controller.playGame(initialState, players))
        }
    }
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

