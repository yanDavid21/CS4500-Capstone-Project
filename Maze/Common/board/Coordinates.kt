package Common


interface Position {
    val value: Int

    val min: Int

    val max: Int

    fun checkBounds(given: Int) {
        if (value > max || value < min) {
            throw IllegalArgumentException("Position should be in the interval [$min, $max], given: $given")
        }
    }

    companion object {
        const val MIN_X_INDEX = 0
        const val MIN_Y_INDEX = 0
        const val MAX_X_INDEX = 6
        const val MAX_Y_VALUE = 6
    }
}

data class ColumnPosition(override val value: Int): Position {
    init { checkBounds(value) }

    override val min = Position.MIN_X_INDEX
    override val max = Position.MAX_X_INDEX
}


data class RowPosition(override val value: Int): Position {
    init { checkBounds(value) }

    override val min  = Position.MIN_Y_INDEX
    override val max  = Position.MAX_Y_VALUE
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
