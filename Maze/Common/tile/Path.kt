package Common.tile


/**
 * Represents a canonical path on a tile.g
 */
enum class Path(val symbol: String) {
    VERTICAL("│"),UP_RIGHT("└"),T("┬"),CROSS("┼");

    fun getDefaultOutgoingDirections(): Set<Direction> {
        return when (this) {
            VERTICAL -> setOf(VerticalDirection.UP, VerticalDirection.DOWN)
            CROSS -> setOf(VerticalDirection.UP, VerticalDirection.DOWN, HorizontalDirection.LEFT, HorizontalDirection.RIGHT)
            UP_RIGHT -> setOf(VerticalDirection.UP, HorizontalDirection.RIGHT)
            T -> setOf(HorizontalDirection.LEFT, HorizontalDirection.RIGHT, VerticalDirection.DOWN)
        }
    }

    override fun toString(): String {
        return symbol
    }
}