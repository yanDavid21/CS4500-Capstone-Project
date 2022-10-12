package Common

class PlayerQueue(
    private val players: MutableList<Player>
) {

    private var currentPlayerIndex = 0

    fun getCurrentPlayer(): Player {
        return players[currentPlayerIndex]
    }

    fun nextPlayer(): Player {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size
        return players[currentPlayerIndex]
    }

    fun removeCurrentPlayer():Player  {
        val player = players.removeAt(currentPlayerIndex)
        currentPlayerIndex %= players.size
        return player
    }
}