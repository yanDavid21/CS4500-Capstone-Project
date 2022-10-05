package Common


interface Position {
    val value: Int

    companion object {
        const val MAX_X_VALUE = 7
        const val MAX_Y_VALUE = 7
    }
}

data class ColumnPosition(override val value: Int): Position {
    init {
        if (value > Position.MAX_X_VALUE) {
            throw IllegalArgumentException("Y-coordinate should not exceed $Position.MAX_X_VALUE, given $value")
        }
    }
}


data class RowPosition(override val value: Int): Position {
    init {
        if (value > Position.MAX_Y_VALUE) {
            throw IllegalArgumentException("X-coordinate should not exceed ${Position.MAX_Y_VALUE}, given $value")
        }
    }

}

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
