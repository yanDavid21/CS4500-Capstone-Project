package Common.board

import Common.Direction
import Common.HorizontalDirection
import Common.VerticalDirection
import Common.board.tile.Degree

interface Tile {
    fun getOutgoingDirections(): Set<Direction>

    fun getIncomingDirections(): Set<Direction>

    fun rotate(degree: Degree)


    fun canBeReachedFrom(incomingDirection: Direction): Boolean
}

class EmptyTile : Tile {
    override fun getOutgoingDirections(): Set<Direction> {
        return emptySet()
    }

    override fun rotate(degree: Degree) {
        return
    }

    override fun getIncomingDirections(): Set<Direction> {
        return emptySet()
    }

    override fun canBeReachedFrom(incomingDirection: Direction): Boolean {
        return false
    }

    override fun hashCode(): Int {
        return 0
    }

    override fun equals(other: Any?): Boolean {
        return other is EmptyTile
    }
}

data class GameTile(val path: Path, var degree: Degree): Tile {
    private lateinit var incomingDirections: Set<Direction>
    private lateinit var outgoingDirections: Set<Direction>

    init {
        recomputeDirections()
    }

    override fun rotate(degree: Degree) {
        this.degree = this.degree.add(degree)
        recomputeDirections()
    }

    private fun recomputeDirections() {
        outgoingDirections = path.getDefaultOutgoingDirections().mapTo(mutableSetOf()) { it.rotateBy(degree) }
        incomingDirections = outgoingDirections.mapTo(mutableSetOf()) { it.reverse() }
    }

    override fun getOutgoingDirections(): Set<Direction> {
        return outgoingDirections
    }

    override fun getIncomingDirections(): Set<Direction> {
        return incomingDirections
    }


    override fun canBeReachedFrom(incomingDirection: Direction): Boolean {
        return this.incomingDirections.contains(incomingDirection)
    }

    override fun toString(): String {
        return "($path, $degree)"
    }
}

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