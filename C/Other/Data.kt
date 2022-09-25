package Other

/**
 * A JSON representation of characters, contains horizontal and vertical information.
 */
@Serializable
data class CardinalCharacters(
    val horizontal: Horizontal,
    val vertical: Vertical,
)

@Serializable
enum class Horizontal {
    LEFT, RIGHT
}

@Serializable
enum class Vertical {
    UP, DOWN
}