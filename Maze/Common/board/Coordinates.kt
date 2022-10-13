package Common.board

/**
 * Represents a valid coordinate in the board. Rows and columns are indexed 0..6 and (0,0) is
 * top-left.
 */
data class Coordinates(
    val row: RowPosition,
    val col: ColumnPosition
) {
    fun copyWithNewRow(rowValue: Int): Coordinates {
        return this.copy(row = RowPosition(rowValue))
    }

    fun copyWithNewCol(colValue: Int): Coordinates {
        return this.copy(col = ColumnPosition(colValue))
    }

    companion object {
        fun fromRowAndValue(rowPos: Int, colPos: Int): Coordinates {
            return Coordinates(RowPosition(rowPos), ColumnPosition(colPos))
        }
    }
}




