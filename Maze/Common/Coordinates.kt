package Common

interface Coordinate

data class ColumnPosition(
    val y: Int
): Coordinate {

    init {
        if (y > MAX_VALUE) {
            throw IllegalArgumentException("Y-coordinate should not exceed $MAX_VALUE, given $y")
        }
    }

    companion object {
        var MAX_VALUE = 7
    }
}

data class RowPosition(
    val x: Int
):Coordinate {
    init {
        if (x > ColumnPosition.MAX_VALUE) {
            throw IllegalArgumentException("X-coordinate should not exceed ${MAX_VALUE}, given $x")
        }
    }

    companion object {
        var MAX_VALUE = 7
    }
}
