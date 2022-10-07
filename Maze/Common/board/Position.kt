package Common.board

interface Position {
    val value: Int

    val min: Int

    val max: Int

    fun checkBounds() {
        if (value > max || value < min) {
            throw IllegalArgumentException("Position should be in the interval [$min, $max], given: $value")
        }
    }


    companion object {
        const val MIN_X_INDEX = 0
        const val MIN_Y_INDEX = 0
        const val MAX_X_INDEX = 6
        const val MAX_Y_INDEX = 6
    }
}

data class ColumnPosition(override val value: Int): Position {
    init { checkBounds() }

    override val min: Int
        get() = Position.MIN_X_INDEX
    override val max: Int
        get() = Position.MAX_X_INDEX


}


data class RowPosition(override val value: Int): Position {
    init { checkBounds() }

    override val min: Int
        get() = Position.MIN_Y_INDEX
    override val max: Int
        get() = Position.MAX_Y_INDEX
}