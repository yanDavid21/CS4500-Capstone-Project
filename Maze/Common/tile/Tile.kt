package Common.tile

import Common.Player
import Common.tile.treasure.Treasure
import java.util.*

interface Tile {
    fun getOutgoingDirections(): Set<Direction>

    fun getIncomingDirections(): Set<Direction>

    fun rotate(degree: Degree)

    fun hasPlayer(): Boolean

    fun addPlayerToTile(player: Player)

    fun removePlayerFromTile(player: Player)

    fun hasCertainPlayer(player:Player): Boolean

    fun canBeReachedFrom(incomingDirection: Direction): Boolean

    fun getPlayers(): Set<Player>
}

class EmptyTile : Tile {
    override fun getOutgoingDirections(): Set<Direction> {
        return emptySet()
    }

    override fun rotate(degree: Degree) {
        return
    }

    override fun getIncomingDirections(): Set<Direction> {
        return emptySet()
    }

    override fun canBeReachedFrom(incomingDirection: Direction): Boolean {
        return false
    }

    override fun hasPlayer(): Boolean {
        return false
    }

    override fun hasCertainPlayer(player: Player): Boolean {
        return false
    }

    override fun addPlayerToTile(player: Player) {
        throw IllegalArgumentException("Can not add player to the empty tile.")
    }

    override fun removePlayerFromTile(player: Player) {
        throw IllegalArgumentException("Can not remove player from the empty tile.")
    }

    override fun hashCode(): Int {
        return 0
    }

    override fun equals(other: Any?): Boolean {
        return other is EmptyTile
    }

    override fun getPlayers(): Set<Player> {
        return setOf()
    }
}

data class GameTile(val path: Path, var degree: Degree, val treasure: Treasure): Tile {
    private var players  = mutableSetOf<Player>()
    private lateinit var incomingDirections: Set<Direction>
    private lateinit var outgoingDirections: Set<Direction>

    init {
        recomputeDirections()
    }

    override fun rotate(degree: Degree) {
        this.degree = this.degree.add(degree)
        recomputeDirections()
    }

    private fun recomputeDirections() {
        outgoingDirections = path.getDefaultOutgoingDirections().mapTo(mutableSetOf()) { it.rotateBy(degree) }
        incomingDirections = outgoingDirections.mapTo(mutableSetOf()) { it.reverse() }
    }

    override fun getOutgoingDirections(): Set<Direction> {
        return outgoingDirections
    }

    override fun getIncomingDirections(): Set<Direction> {
        return incomingDirections
    }


    override fun canBeReachedFrom(incomingDirection: Direction): Boolean {
        return this.incomingDirections.contains(incomingDirection)
    }

    override fun hasPlayer(): Boolean {
        return players.isNotEmpty()
    }

    override fun addPlayerToTile(player: Player) {
        this.players.add(player)
    }

    override fun removePlayerFromTile(player: Player) {
        this.players.remove(player)
    }

    override fun hasCertainPlayer(player: Player): Boolean {
        return this.players.contains(player)
    }

    override fun toString(): String {
        return "($path, $degree)"
    }

    override fun hashCode(): Int {
        return Objects.hash(this.treasure, this.path, this.degree)
    }

    override fun getPlayers(): Set<Player> {
        return this.players
    }

    override fun equals(other: Any?): Boolean {
        if (other is GameTile) {
            return (this.treasure.equals(other.treasure) && this.path == other.path && this.degree == other.degree)
        }
        return false
    }


}

