package Common.board

import Common.Direction
import Common.HorizontalDirection
import Common.VerticalDirection
import Common.board.tile.Degree


class Tile(val path: Path, var degree: Degree) {
    private lateinit var incomingDirections: Set<Direction>
    private lateinit var outgoingDirections: Set<Direction>

    init {
        recomputeDirections()
    }

    fun rotate(degree: Degree) {
        this.degree = this.degree.add(degree)
        recomputeDirections()
    }

    private fun recomputeDirections() {
        outgoingDirections = path.getDefaultOutgoingDirections().mapTo(mutableSetOf()) { it.transform(degree) }
        incomingDirections = outgoingDirections.mapTo(mutableSetOf()) { it.reverse() }
    }

    fun getOutgoingDirections(): Set<Direction> {
        return outgoingDirections
    }

    fun getIncomingDirections(): Set<Direction> {
        return incomingDirections
    }


    fun canBeReachedFrom(incomingDirection: Direction): Boolean {
        return this.incomingDirections.contains(incomingDirection)
    }

}

enum class Path(symbol: String) {
    VERTICAL("│"),UP_RIGHT("└"),T("┬"),CROSS("┼");

    fun getDefaultOutgoingDirections(): Set<Direction> {
        return when (this) {
            VERTICAL -> setOf(VerticalDirection.UP, VerticalDirection.DOWN)
            CROSS -> setOf(VerticalDirection.UP, VerticalDirection.DOWN, HorizontalDirection.LEFT, HorizontalDirection.RIGHT)
            UP_RIGHT -> setOf(VerticalDirection.UP, HorizontalDirection.RIGHT)
            T -> setOf(HorizontalDirection.LEFT, HorizontalDirection.RIGHT, VerticalDirection.DOWN)
        }
    }
}