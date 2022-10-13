package Common

import Common.board.*
import Common.tile.GameTile
import Common.tile.HorizontalDirection
import Common.tile.VerticalDirection

/**
 * Contains all knowledge about the game state. Including all Player state (player id, goal tile, home tile),
 * tiles on the board, what the current spare tile is, whose turn it is, if there is a winner.
 */
class Referee(
    private val board: Board,
    private var spareTile: GameTile,
    players: List<Player>
) {
    private val playerQueue = PlayerQueue(players.toMutableList())
    private var winner: Player? = null

    /**
     * Moves the currently active player from it tile to a given destination.
     *
     * Throws IllegalArgumentException if the given tile is not reachable.
     */
    fun moveActivePlayer(to: Coordinates) {
        val activePlayer = playerQueue.getCurrentPlayer()
        val currentPlayerPosition = findPlayerPosition(activePlayer)

        checkActiveMovePlayer(activePlayer, currentPlayerPosition, to)
        movePlayerAcrossBoard(activePlayer, currentPlayerPosition, to)
    }

    /**
     * Looks for a player
     */
    private fun findPlayerPosition(player: Player): Coordinates {
        for (rowPos in 0 until 7) {
            for (colPos in 0 until 7) {
                val pos = Coordinates.fromRowAndValue(rowPos, colPos)
                val tileAtPos = board.getTile(pos)
                if (tileAtPos.hasCertainPlayer(player)) {
                    return pos
                }
            }
        }
        throw  java.lang.IllegalStateException("A Player should always be on the board, could not find $player")
    }

    /**
     * Has the active player reached it goal?
     */
    fun hasActivePlayerReachedGoal(): Boolean {
        return playerQueue.getCurrentPlayer().treasureFound
    }

    /**
     * Slides a row in the board in the provided direction, then inserts the spare tile into the vacant spot.
     */
    fun slideRowAndInsertSpare(rowPosition: RowPosition, direction: HorizontalDirection) {
        slideInsertAndDealWithPlayers { board.slideRowAndInsert(rowPosition,direction, this.spareTile) }
    }

    /**
     * Slides a column in the board in the provided direction, then inserts the spare tile into the vacant spot.
     */
    fun slideColumnAndInsertSpare(columnPosition: ColumnPosition, direction: VerticalDirection) {
        slideInsertAndDealWithPlayers { board.slideColAndInsert(columnPosition, direction, this.spareTile) }
    }

    /**
     * Removes the active player from the queue and from all the tiles. Automatically makes the
     * active player the next player in the queue.
     */
    fun kickOutActivePlayer() {
        val activePlayer = playerQueue.removeCurrentPlayer()
        val playerPosition = findPlayerPosition(activePlayer)
        board.getTile(playerPosition).removePlayerFromTile(activePlayer)
    }

    /**
     * Performs a specific board sliding operation and then removes the player
     * from the newly created spare tile to the just inserted one if needed;
     */
    private fun slideInsertAndDealWithPlayers(getDislodgedAndSlide: () -> GameTile) {
        val toBeInserted = this.spareTile
        this.spareTile = getDislodgedAndSlide()
        moveAmnestiedPlayersIfAny(this.spareTile, toBeInserted)
    }

    private fun moveAmnestiedPlayersIfAny(fromTile: GameTile, toTile: GameTile) {
        fromTile.getPlayers().forEach {
            fromTile.removePlayerFromTile(it)
            toTile.addPlayerToTile(it)
        }
    }

    private fun canPlayerReachTile(player: Player, location: Coordinates): Boolean {
        return board.getReachableTiles(board.findPlayerLocation(player)).contains(location)
    }

    private fun movePlayerAcrossBoard(activePlayer:Player, currentPosition: Coordinates, targetCoord: Coordinates) {
        val tileToMoveTo= board.getTile(targetCoord)
        board.getTile(currentPosition).removePlayerFromTile(activePlayer)
        tileToMoveTo.addPlayerToTile(activePlayer)

        activePlayer.treasureFound = activePlayer.treasureFound || tileToMoveTo.treasure == activePlayer.goal
        updateWinner(activePlayer, tileToMoveTo)
    }

    private fun updateWinner(activePlayer: Player, targetTile: GameTile) {
        if (activePlayer.homeTile == targetTile && activePlayer.treasureFound) {
            winner = activePlayer
        }
    }

    private fun checkActiveMovePlayer(activePlayer: Player, currentPosition: Coordinates,  to: Coordinates) {
        if (!canPlayerReachTile(activePlayer, to) || currentPosition == to) {
            throw IllegalArgumentException("Can not move active player to $to.")
        }
    }
}