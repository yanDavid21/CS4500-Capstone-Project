package Common.board

import Common.tile.HorizontalDirection
import Common.tile.VerticalDirection

/**
 * A Position is a valid index in the Maze board. The value of position must exist in the specified range. All boards
 * constructed needs its size to abide by the MIN and MAX INDEX constants.
 */
interface Position {
    val value: Int

    val min: Int

    val max: Int

    /**
     * Checks whether a Position's value is out of bounds, if not throws the IllegalArgumentException.
     * All implementations must call this on initialization.
     */
    fun checkBounds() {
        if (value > max || value < min) {
            throw IllegalArgumentException("Position should be in the interval [$min, $max], given: $value")
        }
    }

    companion object {
        const val MIN_COL_INDEX = 0
        const val MIN_ROW_INDEX = 0
        const val MAX_COL_INDEX = 6
        const val MAX_ROW_INDEX = 6
    }
}

/**
 * Represents a valid column index on a Maze board.
 */
data class ColumnPosition(override val value: Int): Position {
    init { checkBounds() }

    /**
     * Gets the next column position based on a horizontal direction. Returns null if out of bounds.
     */
    fun nextPosition(direction: HorizontalDirection): ColumnPosition? {
        val newVal = when (direction) {
            HorizontalDirection.LEFT-> value - 1
            HorizontalDirection.RIGHT -> value + 1
        }
        if (newVal < min || newVal > max) {
            return null
        }
        return ColumnPosition(newVal)
    }

    override val min: Int
        get() = Position.MIN_COL_INDEX
    override val max: Int
        get() = Position.MAX_COL_INDEX

    override fun toString(): String {
        return value.toString()
    }
}

/**
 * Represents a valid row index on a Maze board.
 */
data class RowPosition(override val value: Int): Position {
    init { checkBounds() }

    /**
     * Gets the next row position based on a vertical direction. Returns null if it is out of bounds.
     */
    fun nextPosition(direction: VerticalDirection): RowPosition? {
        val newVal = when (direction) {
            VerticalDirection.DOWN -> value + 1
            VerticalDirection.UP -> value - 1
        }
        if (newVal < min || newVal > max) {
            return null
        }
        return RowPosition(newVal)
    }

    override val min: Int
        get() = Position.MIN_ROW_INDEX
    override val max: Int
        get() = Position.MAX_ROW_INDEX

    override fun toString(): String {
        return value.toString()
    }
}