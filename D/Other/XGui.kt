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
import java.io.Reader


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
        if (chars.isNotEmpty()) { rows.add(Row(chars)) }
    }

    return rows
}

fun main() {

    val rows = readFromConsole(InputStreamReader(System.`in`))

    launch(GUI::class.java)
    // TODO: draw(rows)
    // print gui mouse click event coordinates to STD out
}

class GUI : Application() {

    private var rows: List<Row>

    init {
     rows = readFromConsole(InputStreamReader(System.`in`))
    }

    override fun start(primaryStage: Stage) {

        val layout = VBox().apply {
            rows.forEach { row ->
                val listOfLabels: Array<Node> = Array(row.characters.size) {
                    Label(row.characters.get(it).toString())
                }
                children.add(HBox(*listOfLabels))
            }
        }
        primaryStage.run {
            scene = Scene(layout)
            show()
        }
    }
}