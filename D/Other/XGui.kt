package D.Other

import Other.AcceptableCharacter
import javafx.application.Application
import javafx.application.Application.launch
import javafx.application.Platform
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.input.MouseEvent
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.stage.Stage
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import java.io.InputStreamReader
import java.io.Reader

fun main() {
    GUI.initialize()


    launch(GUI::class.java)
}

class GUI : Application() {

    /**
     * JavaFX's API instantiates this class with no arguments.
     *
     * Static field instantiation is required to initialize the row data for the JavaFX GUI isntance to use.
     */
    companion object {
        private var rows = listOf<Row>()

        fun initialize() {
            rows = readFromConsole(InputStreamReader(System.`in`))
        }
    }

    override fun start(primaryStage: Stage) {
        val layout = createScene()

        layout.setOnMouseClicked(::handleClick)

        primaryStage.run {
            scene = Scene(layout)
            show()
        }
    }

    /**
     * Creates a rectangular box where every row of the scene is the graphical representation
     * of the input characters.
     */
    private fun createScene(): Parent {
        return VBox().apply {
            rows.forEach { row ->
                val listOfRectangles: Array<Node> = Array(row.characters.size) { index ->
                    row.characters.get(index).toJavaFXRectangle()
                }
                val hbox = HBox(*listOfRectangles)
                hbox.spacing = 5.0
                children.add(hbox)
            }
            this.spacing = 5.0
        }
    }

    /**
     * Print the x and y coordinates of the mouse as JSON and end the program.
     */
    private fun handleClick(event: MouseEvent) {
        val x = event.sceneX
        val y = event.sceneY
        println(Json.encodeToJsonElement(arrayOf(x,y)))
        Platform.exit()
    }
}

/**
 * Reads a series of well-formed JSON objects from the console and outputs a list of AcceptableCharacters
 */
fun readFromConsole(reader: Reader):List<Row> {
    val lines = reader.readText().split("\"")

    val rows = mutableListOf<Row>()
    lines.forEach { line ->
        val chars = mutableListOf<AcceptableCharacter>()
        line.forEach { char ->
            if (AcceptableCharacter.isAcceptable(char)) {
                chars.add(AcceptableCharacter.fromChar(char))
            }
        }
        // if line contained whitespace, don't add a row
        if (chars.isNotEmpty()) { rows.add(Row(chars)) }
    }

    return rows
}

class Row(val characters: List<AcceptableCharacter>)



