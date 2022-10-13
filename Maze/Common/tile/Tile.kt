package Common.tile

import Common.Player
import Common.tile.treasure.Treasure
import java.util.*

data class GameTile(val path: Path, var degree: Degree, val treasure: Treasure) {
    private var players  = mutableSetOf<Player>()
    private lateinit var incomingDirections: Set<Direction>
    private lateinit var outgoingDirections: Set<Direction>

    init {
        recomputeDirections()
    }

    fun rotate(degree: Degree) {
        this.degree = this.degree.add(degree)
        recomputeDirections()
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

    fun addPlayerToTile(player: Player) {
        this.players.add(player)
    }

    fun removePlayerFromTile(player: Player) {
        this.players.remove(player)
    }

    fun hasCertainPlayer(player: Player): Boolean {
        return this.players.contains(player)
    }

    fun getPlayers(): Set<Player> {
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

    private fun recomputeDirections() {
        outgoingDirections = path.getDefaultOutgoingDirections().mapTo(mutableSetOf()) { it.rotateBy(degree) }
        incomingDirections = outgoingDirections.mapTo(mutableSetOf()) { it.reverse() }
    }
}

