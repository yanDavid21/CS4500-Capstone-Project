package Common

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
}




