package Common

interface Position {
    fun getValue(): Int
}

data class ColumnPosition(
    val y: Int
): Position {

    init {
        if (y > MAX_VALUE) {
            throw IllegalArgumentException("Y-coordinate should not exceed $MAX_VALUE, given $y")
        }
    }

    override fun getValue(): Int {
        return y
    }

    companion object {
        var MAX_VALUE = 7
    }
}

data class RowPosition(
    val x: Int
):Position {
    init {
        if (x > ColumnPosition.MAX_VALUE) {
            throw IllegalArgumentException("X-coordinate should not exceed ${MAX_VALUE}, given $x")
        }
    }

    override fun getValue(): Int {
        return x
    }

    companion object {
        var MAX_VALUE = 7
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
