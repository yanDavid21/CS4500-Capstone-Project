package Common.board

import kotlin.math.pow
import kotlin.math.sqrt

/**
 * Represents a valid coordinate in the board. Rows and columns are indexed 0..6 and (0,0) is
 * top-left.
 */
data class Coordinates(
    val row: RowPosition,
    val col: ColumnPosition
) {

    /**
     * Returns a copy of this with a new row value.
     */
    fun copyWithNewRow(rowValue: Int): Coordinates {
        return this.copy(row = RowPosition(rowValue))
    }

    /**
     * Computes the Euclidian distance between two coordinates.
     */
    fun distanceTo(other: Coordinates): Double {
        val deltaX= (this.row.value - other.row.value).toDouble()
        val deltaY = (this.col.value - other.col.value).toDouble()
        return sqrt(deltaX.pow(2) + deltaY.pow(2))
    }

    /**
     * Returns a copy of this with a new column value.
     */
    fun copyWithNewCol(colValue: Int): Coordinates {
        return this.copy(col = ColumnPosition(colValue))
    }

    override fun toString(): String {
        return "($row, $col)"
    }
    companion object {
        /**
         * Utility function to construct a Coordinates object from a pair of Ints.
         * Throws IllegalArgumentException if the Ints are out of bounds.
         */
        fun fromRowAndValue(rowPos: Int, colPos: Int): Coordinates {
            return Coordinates(RowPosition(rowPos), ColumnPosition(colPos))
        }
    }
}




