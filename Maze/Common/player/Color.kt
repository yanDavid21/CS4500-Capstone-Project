package Common.player

interface Color {
    companion object {
        fun valueOf(color: String): Color {
            if (color.matches(Regex("^[A-F|\\d][A-F|\\d][A-F|\\d][A-F|\\d][A-F|\\d][A-F|\\d]$"))) {
                return HexColor(color)
            }
            return BaseColor.valueOf(color.toUpperCase())
        }
    }
}

data class HexColor(val hexcode: String): Color

// TODO: add hex values here for future use?
enum class BaseColor: Color {
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