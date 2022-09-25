package Other.C

import kotlinx.serialization.Serializable

/**
 * A JSON representation of an acceptable character, contains horizontal and vertical information.
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