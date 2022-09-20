package D.Other

import javafx.application.Application
import javafx.application.Application.launch
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.layout.HBox
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.stage.Stage
import java.io.Reader
import java.io.StringReader


enum class AcceptableCharacter {
    LEFT_UP,
    RIGHT_UP,
    RIGHT_DOWN,
    LEFT_DOWN;

    companion object {
        fun fromChar(symbol: Char): AcceptableCharacter {
            return when (symbol) {
                '┘' -> LEFT_UP
                '┌' -> RIGHT_DOWN
                '┐' -> LEFT_DOWN
                '└' -> RIGHT_UP
                else -> throw java.lang.IllegalArgumentException("$symbol is not an acceptable character.")
            }
        }

        fun isAcceptable(symbol: Char): Boolean {
            return when (symbol) {
                '┘', '┐', '┌', '└' -> true
                else -> false
            }
        }
    }


    fun toJavaFXRectangle(): Node {
        val stack = StackPane()
        val background = Rectangle()
        background.width = 100.0;
        background.height = 100.0;


        val alignment = when (this) {
            LEFT_UP -> Pos.BOTTOM_RIGHT
            RIGHT_UP -> Pos.BOTTOM_LEFT
            LEFT_DOWN -> Pos.TOP_RIGHT
            RIGHT_DOWN -> Pos.TOP_LEFT
            else -> throw java.lang.IllegalArgumentException("$this is not an acceptable character.")
        }



        stack.children.addAll(background, makeRectangleShape(alignment))

        return stack
    }

    /**
     * Makes a Right Down shape, can be transformed into any other shape.
     */
    private fun makeRectangleShape(alignment: Pos): Node {
        val horizontal = Rectangle(70.0, 20.0)
        val vertical = Rectangle(20.0, 70.0)

        horizontal.fill = Color.WHITE
        vertical.fill = Color.WHITE

        val sp = StackPane(horizontal, vertical)
        sp.alignment = alignment
        return sp
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

    //val rows = readFromConsole(InputStreamReader(System.`in`))

    launch(GUI::class.java)
    // TODO: draw(rows)
    // print gui mouse click event coordinates to STD out
}

class GUI : Application() {

    private var rows: List<Row>

    init {
        rows = readFromConsole(StringReader("\"┌┌┐\"\n" +
                "\n" +
                "    \"└└┘\""))
    }

    override fun start(primaryStage: Stage) {

        val layout = VBox().apply {
            rows.forEach { row ->
                val listOfRectangles: Array<Node> = Array(row.characters.size) { index ->
                    row.characters.get(index).toJavaFXRectangle()
                }
                children.add(HBox(*listOfRectangles))
            }
        }
        primaryStage.run {
            scene = Scene(layout)
            show()
        }
    }
}