package Client.javafx

import Client.LocalStateObserver
import Common.GameState
import Players.PlayerMechanism
import Referee.ObservableReferee
import javafx.application.Application
import javafx.event.EventHandler
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.FileChooser
import javafx.stage.Stage


abstract class RefereeObserverApplication: Application() {
    private val SCREEN_WIDTH = 1000.0
    private val SCREEN_HEIGHT = 1000.0

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
            scene
            scene = Scene(parent, SCREEN_WIDTH, SCREEN_HEIGHT)
            show()
        }
        referee.playGame(initialState, players)
    }
}




