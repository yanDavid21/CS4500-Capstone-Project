package Common.player

import Common.board.Coordinates
import java.util.*

/**
 * Represents a Maze player, every player has a treasure goal, position and color for home tile.
 */
data class Player(val id: UUID,
                  var currentPosition: Coordinates,
                  val goalPosition: Coordinates,
                  val homePosition: Coordinates,
                  val color: Color,
                  var treasureFound: Boolean = false) {

    fun getGoal(): Coordinates {
        return if (treasureFound) homePosition else goalPosition
    }

    override fun equals(other: Any?): Boolean {
        return other is Player && other.id == this.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
