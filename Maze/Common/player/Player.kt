package Common.player

import Common.board.Coordinates
import Common.tile.GameTile
import Common.tile.treasure.Treasure
import java.util.*

/**
 * Represents a Maze player, every player has a treasure goal, position and color for home tile.
 */
data class Player(val id: UUID,
                  var currentPosition: Coordinates,
                  val goal: Treasure,
                  val homeTile: GameTile,
                  val color: Color,
                  var treasureFound: Boolean = false) {

    override fun equals(other: Any?): Boolean {
        return other is Player && other.id == this.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
