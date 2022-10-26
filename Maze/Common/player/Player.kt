package Common.player

import Common.board.Coordinates

/**
 * Represents a Maze player, every player has a treasure goal, position and color for home tile.
 */
data class Player(
    val id: String,
    val currentPosition: Coordinates,
    val goalPosition: Coordinates,
    val homePosition: Coordinates,
    val color: Color,
    val treasureFound: Boolean = false) {

    fun getGoal(): Coordinates {
        return if (treasureFound) homePosition else goalPosition
    }

    override fun equals(other: Any?): Boolean {
        return other is Player && other.id == this.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    fun toPublicPlayerData(): PublicPlayerData {
        return PublicPlayerData(id, currentPosition, homePosition, color)
    }
}


/**
 * Public information about the player data.
 */
data class PublicPlayerData(var name: String, var currentPosition: Coordinates, val homePosition: Coordinates, val color: Color)