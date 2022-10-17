package Common.tile

import Common.tile.treasure.Treasure
import java.util.*

data class GameTile(val path: Path, val degree: Degree, val treasure: Treasure, private val players: Set<Player> = setOf()) {
    private val outgoingDirections: Set<Direction> = path.getDefaultOutgoingDirections().map {
            outGoingDirection -> outGoingDirection.rotateBy(degree)
    }.toSet()
    private val incomingDirections: Set<Direction> = outgoingDirections.map { outgoingDirection -> outgoingDirection.reverse() }.toSet()

    fun rotate(degree: Degree): GameTile {
        return GameTile(this.path, this.degree.add(degree), this.treasure)
    }

    /**
     * The directions that one can go to from a tile.
     *
     * Example:
     * │ can go UP and DOWN
     * ┐ can go LEFT and DOWN
     * └ can go UP and RIGHT
     */
    fun getOutgoingDirections(): Set<Direction> {
        return outgoingDirections
    }

    /**
     * The directions that one can reach a tile from.
     *
     * Computed by taking the inverse of all outgoingDirections.
     *
     * Example:
     * │ can be reached from UP and DOWN
     * ┐ can be reached from RIGHT and UP
     * └ can be reached from DOWN and LEFT
     */
    fun getIncomingDirections(): Set<Direction> {
        return incomingDirections
    }


    /**
     * Looking from outside the tile, going in, can this tile be reached through this direction.
     *
     * Ex:
     * │ can be reached from UP and DOWN
     * ┐ can be reached from RIGHT and UP
     * └ can be reached from DOWN and LEFT
     */
    fun canBeReachedFrom(incomingDirection: Direction): Boolean {
        return this.incomingDirections.contains(incomingDirection)
    }

    fun addPlayerToTile(player: Player): GameTile {
        return GameTile(this.path, this.degree, this.treasure, setOf(player, *this.players.toTypedArray()))
    }

    fun removePlayerFromTile(player: Player): GameTile {
        return GameTile(this.path, this.degree, this.treasure, this.players subtract setOf(player))
    }

    fun hasCertainPlayer(player: Player): Boolean {
        return this.players.contains(player)
    }

    fun getPlayersOnTile(): Set<Player> {
        return this.players.toSet()
    }

    override fun toString(): String {
        return "($path, $degree)"
    }

    override fun hashCode(): Int {
        return Objects.hash(this.treasure, this.path)
    }

    override fun equals(other: Any?): Boolean {
        if (other is GameTile) {
            return this.treasure.equals(other.treasure) && this.path.equals(other.path)
        }
        return false
    }
}

