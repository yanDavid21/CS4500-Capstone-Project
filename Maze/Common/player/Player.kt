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

    init {
        if (goalPosition.row.isSlideable() || goalPosition.col.isSlideable() ||
                    homePosition.row.isSlideable() || homePosition.col.isSlideable()) {
            throw IllegalArgumentException("A player's home and treasure tiles should not be slideable. " +
                    "Given [treasure: $goalPosition], [home: $goalPosition].")
        }

    }

    override fun equals(other: Any?): Boolean {
        return other is Player && other.id == this.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
