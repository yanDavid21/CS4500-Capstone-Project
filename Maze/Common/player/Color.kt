package Common.player

/**
 * Represents an avatar's unique color.
 */
interface Color {
    companion object {
        private val currentColors = mutableSetOf<Color>()

        /**
         * Gets the Color from a string. Throws IllegalArgumentException
         */
        fun valueOf(color: String): Color {
            val color = if (color.matches(Regex("^[A-F|\\d][A-F|\\d][A-F|\\d][A-F|\\d][A-F|\\d][A-F|\\d]$")))
                HexColor(color) else BaseColor.valueOf(color.toUpperCase())

            if (currentColors.contains(color)) {
                throw IllegalArgumentException("Must be unique color.")
            }
            currentColors.add(color)
            return color
        }
    }
}

/**
 * Represents a color from hexadecimal values.
 */
data class HexColor(val hexcode: String) : Color


//TODO: add hex values here for future use?
/**
 * Base colors denoted by a string.
 */
enum class BaseColor : Color {
    PURPLE,
    PINK,
    ORANGE,
    RED,
    BLUE,
    GREEN,
    YEllOW,
    BLACK,
    WHITE
}