package Other

import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle

/**
 * All acceptable characters, can be one of: '┘', '┐', '┌', '└'.
 */
enum class AcceptableCharacter {
    LEFT_UP,
    RIGHT_UP,
    RIGHT_DOWN,
    LEFT_DOWN;

    companion object {
        /**
         * Parses a single character into its corresponding AcceptableCharacter.
         */
        fun fromChar(symbol: Char): AcceptableCharacter {
            return when (symbol) {
                '┘' -> LEFT_UP
                '┌' -> RIGHT_DOWN
                '┐' -> LEFT_DOWN
                '└' -> RIGHT_UP
                else -> throw java.lang.IllegalArgumentException("$symbol is not an acceptable character.")
            }
        }

        /**
         * Is this Char acceptable?
         */
        fun isAcceptable(symbol: Char): Boolean {
            return when (symbol) {
                '┘', '┐', '┌', '└' -> true
                else -> false
            }
        }
    }

    /**
     * Creates a JavaFX rectangle for this character.
     */
    fun toJavaFXRectangle(): Node {
        val stack = StackPane()
        val background = Rectangle()
        background.fill = Color.WHITE
        background.width = 100.0;
        background.height = 100.0;


        val alignment = when (this) {
            LEFT_UP -> Pos.BOTTOM_RIGHT
            RIGHT_UP -> Pos.BOTTOM_LEFT
            LEFT_DOWN -> Pos.TOP_RIGHT
            RIGHT_DOWN -> Pos.TOP_LEFT
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

        horizontal.fill = Color.BLACK
        vertical.fill = Color.BLACK

        val sp = StackPane(horizontal, vertical)
        sp.alignment = alignment
        return sp
    }
}