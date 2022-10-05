package Common

import Common.board.tile.Degree

interface Direction {

    fun transform(degree: Degree): Direction {
        return VerticalDirection.UP // TODO
    }

    fun reverse(): Direction

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