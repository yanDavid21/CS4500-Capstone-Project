package Common.board.tile

enum class Degree(private val value: Int) {
    ZERO(0), NINETY(90), ONE_EIGHTY(180), TWO_SEVENTY(270);

    fun add(other: Degree): Degree {
        return Degree.valueOf((this.value + other.value) % 360)
    }

    companion object {
        fun valueOf(int: Int): Degree {
            return when (int) {
                0 -> ZERO
                90 -> NINETY
                180 -> ONE_EIGHTY
                270 -> TWO_SEVENTY
                else -> throw IllegalArgumentException("Only multiples of 90 are supported degree values.")
            }
        }
    }
}