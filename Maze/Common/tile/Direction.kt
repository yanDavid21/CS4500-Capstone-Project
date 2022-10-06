package Common

import Common.board.tile.Degree

interface Direction {

    val degree: Degree

    fun rotateBy(degree: Degree): Direction {
        return fromDegree(this.degree.add(degree))
    }


    fun reverse(): Direction

    companion object {
        fun fromDegree(degree: Degree): Direction {

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