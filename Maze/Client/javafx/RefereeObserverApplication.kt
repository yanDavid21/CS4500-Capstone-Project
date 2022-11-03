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


/**
 * To create a Graphical User Interface that observers a game being executed by a referee.
 * Applications will provide users with a "next" button to display the next state available, and
 * a "save" button to save the serialized state into a specified file.
 *
 * Implementing classes must specify how to construct a game and the player mechanisms.
 */
abstract class RefereeObserverApplication: Application() {
    private val SCREEN_WIDTH = 1000.0
    private val SCREEN_HEIGHT = 750.0

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
                file?.let {
                    controller.save(file)
                }
            }
            scene = Scene(parent, SCREEN_WIDTH, SCREEN_HEIGHT)
            show()
        }

        controller.updateState(initialState.toPublicState())
        controller.displayState(initialState.toPublicState())

        referee.playGame(initialState, players)
    }
}




