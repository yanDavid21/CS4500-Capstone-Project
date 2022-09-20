package D.Other

import javafx.application.Application
import javafx.application.Application.launch
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.stage.Stage
import java.io.InputStreamReader


enum class AcceptableCharacter {
    LEFT_UP,
    RIGHT_UP,
    RIGHT_DOWN,
    LEFT_DOWN;

    companion object {
        fun fromChar(symbol: Char): AcceptableCharacter {
            return when(symbol) {
                '┘' -> LEFT_UP
                '┌' -> RIGHT_DOWN
                '┐' -> LEFT_DOWN
                '└' -> RIGHT_UP
                else -> throw java.lang.IllegalArgumentException("$symbol is not an acceptable character.")
            }
        }

        fun isAcceptable(symbol: Char): Boolean {
            return when(symbol) {
                '┘', '┐','┌','└' -> true
                else -> false
            }
        }
    }

}

class Row(val characters: List<AcceptableCharacter>) {

    override fun toString(): String {
        return characters.fold("") { a, b -> "$a $b"}
    }
}


fun main() {

    val text = InputStreamReader(System.`in`).readText()

    launch(GUI::class.java, text)
}

class GUI: Application() {

    private var rows = listOf<Row>()

    override fun `init`() {
        rows = readFromConsole(parameters.raw[0])
    }

    override fun start(primaryStage: Stage) {

        val layout = VBox().apply {
            rows.forEach { row ->
                val listOfLabels: Array<Node> = Array(row.characters.size) {
                    drawCharacter(row.characters[it])
                }
                children.add(HBox(*listOfLabels))
            }
        }
        primaryStage.run {
            scene = Scene(layout)
            show()
        }
    }

    private fun drawCharacter(char: AcceptableCharacter): Node {
        return Label(char.toString())
    }

    /**
     * Reads a series of well-formed JSON objects and outputs a list of Rows, containing all acceptable characters
     * for that row.
     */
    private fun readFromConsole(text: String):List<Row> {
        val lines = text.split("\"")

        val rows = mutableListOf<Row>()
        lines.forEach { line ->
            val chars = mutableListOf<AcceptableCharacter>()
            line.forEach { char ->
                if (AcceptableCharacter.isAcceptable(char)) {
                    chars.add(AcceptableCharacter.fromChar(char))
                }
            }
            if (chars.isNotEmpty()) { rows.add(Row(chars)) }
        }

        return rows
    }
}