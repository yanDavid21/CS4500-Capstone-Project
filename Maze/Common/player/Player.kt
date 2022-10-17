package Common.player

import Common.tile.GameTile
import Common.tile.treasure.Treasure
import java.util.*

/**
 * Represents a Maze player, every player has a treasure goal and home tile. A player's location is tracked by the board.
 */
data class Player(val id: UUID,
                  val goal: Treasure,
                  val homeTile: GameTile,
                  var treasureFound: Boolean = false) {

    override fun equals(other: Any?): Boolean {
        return other is Player && other.id == this.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
