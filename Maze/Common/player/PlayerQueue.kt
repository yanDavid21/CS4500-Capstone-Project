package Common.player

/**
 * A rotating queue of players specifying whose turn it is. The queue can have players added and removed from.
 */
class PlayerQueue(
    private val players: MutableList<Player>
) {

    private var currentPlayerIndex = 0

    /**
     * Returns the player at the current index.
     */
    fun getCurrentPlayer(): Player {
        return players[currentPlayerIndex]
    }

    /**
     * Gets the next player by moving increment the currentPlayer index by one.
     */
    fun getNextPlayer(): Player {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size
        return players[currentPlayerIndex]
    }

    /**
     * Removes the player at the current index.
     */
    fun removeCurrentPlayer(): Player {
        val player = players.removeAt(currentPlayerIndex)
        currentPlayerIndex %= players.size
        return player
    }

    /**
     * Returns an immutable list of players.
     */
    fun get(): List<Player> {
        return this.players
    }

    fun size(): Int {
        return this.players.size
    }
}