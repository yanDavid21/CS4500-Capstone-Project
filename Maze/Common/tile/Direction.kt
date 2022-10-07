package Common

import Common.board.tile.Degree

interface Direction {

    fun rotateBy(degree: Degree): Direction {
        return fromDegree(this.getDegree().add(degree))
    }

    fun getDegree(): Degree {
        return when(this) {
            HorizontalDirection.RIGHT -> Degree.ZERO
            VerticalDirection.UP -> Degree.NINETY
            HorizontalDirection.LEFT -> Degree.ONE_EIGHTY
            VerticalDirection.DOWN -> Degree.TWO_SEVENTY
            else -> throw IllegalArgumentException("Invalid direction")
        }
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

    override fun reverse(): Direction {
        return when(this) {
            LEFT -> RIGHT
            RIGHT -> LEFT
        }
    }
}

enum class VerticalDirection: Direction {
    UP, DOWN;

    override fun reverse(): Direction {
        return when(this) {
            UP-> DOWN
            DOWN -> UP
        }
    }
}