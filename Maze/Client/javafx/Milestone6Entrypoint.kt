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
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.FileChooser
import javafx.stage.Stage
import serialization.data.RefereeStateDTO
import testing.getPlayerMechanisms
import testing.getRefereeState
import java.io.InputStreamReader


fun main() {
    launch(CommandLineRefereeApp::class.java)
}

abstract class RefereeObserverApplication: Application() {

    abstract fun getStateAndPlayers(): Pair<GameState, List<PlayerMechanism>>

    override fun start(primaryStage: Stage?) {
        val (initialState, players) = getStateAndPlayers()
        val fxmlLoader = FXMLLoader(javaClass.getResource("/JavaFXObserver.fxml"))
        val parent = fxmlLoader.load<Parent>()
        val controller = fxmlLoader.getController<LocalStateObserver>()
        val referee = ObservableReferee(listOf(controller))

        primaryStage?.run {
            val fileChooser = FileChooser()
            controller.saveButton.onAction = EventHandler {
                fileChooser.extensionFilters.addAll(FileChooser.ExtensionFilter("All Files", "*.*"))
                val file = fileChooser.showSaveDialog(this)
                controller.save(file)
            }
            scene = Scene(parent)
            show()
        }
        referee.playGame(initialState, players)
    }
}

class CommandLineRefereeApp: RefereeObserverApplication() {
    override fun getStateAndPlayers(): Pair<GameState, List<PlayerMechanism>> {
        val jsonReader = JsonReader(InputStreamReader(System.`in`, "UTF-8"))
        val gson = Gson()

        val playerSpec = gson.fromJson<List<List<String>>>(jsonReader, List::class.java)
        val refereeState = gson.fromJson<RefereeStateDTO>(jsonReader, RefereeStateDTO::class.java)

        val state = getRefereeState(refereeState, playerSpec.map { it[0] })

        val mechanisms = getPlayerMechanisms(playerSpec, state)
        return Pair(state, mechanisms)
    }

}




