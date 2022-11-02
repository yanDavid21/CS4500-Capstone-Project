package Client.javafx

import Client.LocalStateObserver
import Common.GameState
import Players.PlayerMechanism
import Referee.ObservableReferee
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import javafx.application.Application
import javafx.application.Application.launch
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.layout.HBox
import javafx.stage.Stage
import serialization.converters.GameStateConverter
import serialization.data.RefereeStateDTO
import testing.getPlayerMechanisms
import java.io.InputStreamReader

fun main() {
    launch(CommandLineRefereeApp::class.java)
}

abstract class RefereeObserverApplication: Application() {

    abstract fun getStateAndPlayers(): Pair<GameState, List<PlayerMechanism>>

    override fun start(primaryStage: Stage?) {
        val (initialState, players) = getStateAndPlayers()
        val observer = LocalStateObserver(initialState)
        val controller = ObservableReferee(listOf(observer))

        val button = Button("Next")

        primaryStage?.run {
            val view = HBox().apply{
                children.add(renderGameState(initialState))

                button.onAction = EventHandler {
                    val nextStateOpt = observer.next()
                    nextStateOpt?.let {nextState ->
                        children[0] = renderGameState(nextState)
                    }
                }

                children.add(button)
            }

            scene = Scene(view)

            show()
        }
        controller.playGame(initialState, players)
    }
}

class CommandLineRefereeApp: RefereeObserverApplication() {
    override fun getStateAndPlayers(): Pair<GameState, List<PlayerMechanism>> {
        val jsonReader = JsonReader(InputStreamReader(System.`in`, "UTF-8"))
        val gson = Gson()

        val playerSpec = gson.fromJson<List<List<String>>>(jsonReader, List::class.java)
        val refereeState = gson.fromJson<RefereeStateDTO>(jsonReader, RefereeStateDTO::class.java)

        val state = GameStateConverter.getRefereeStateFromDTO(refereeState, playerSpec.map { it[0] })

        val mechanisms = getPlayerMechanisms(playerSpec, state)
        return Pair(state, mechanisms)
    }

}




