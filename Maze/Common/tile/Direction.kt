package Common.tile

interface Direction {

    fun getDegree(): Degree

    fun rotateBy(degree: Degree): Direction {
        return fromDegree(this.getDegree().add(degree))
    }

    fun reverse(): Direction

    companion object {
        fun fromDegree(degree: Degree): Direction {
            return when(degree) {
                Degree.ZERO -> HorizontalDirection.RIGHT
                Degree.NINETY -> VerticalDirection.UP
                Degree.ONE_EIGHTY -> HorizontalDirection.LEFT
                Degree.TWO_SEVENTY -> VerticalDirection.DOWN
            }
        }
    }

}

enum class HorizontalDirection: Direction {
    LEFT, RIGHT;

    override fun getDegree(): Degree {
        return when(this) {
            RIGHT -> Degree.ZERO
            LEFT -> Degree.ONE_EIGHTY
        }
    }

    override fun reverse(): Direction {
        return when(this) {
            LEFT -> RIGHT
            RIGHT -> LEFT
        }
    }
}

enum class VerticalDirection: Direction {
    UP, DOWN;

    override fun getDegree(): Degree {
        return when(this) {
            UP -> Degree.NINETY
            DOWN -> Degree.TWO_SEVENTY
        }
    }

    override fun reverse(): Direction {
        return when(this) {
            UP-> DOWN
            DOWN -> UP
        }
    }
}