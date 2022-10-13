package Common.referee

import Common.Player
import Common.PlayerQueue
import Common.board.Board
import Common.board.ColumnPosition
import Common.board.RowPosition
import Common.tile.GameTile
import Common.tile.HorizontalDirection
import Common.tile.VerticalDirection

class Referee(
    private val board: Board,
    private var spareTile: GameTile,
    players: List<Player>
) {
    private val playerQueue = PlayerQueue(players.toMutableList())
    private var winner: Player? = null

    fun moveActivePlayer(tile: GameTile) {
        val activePlayer = playerQueue.getCurrentPlayer()
        if (!canPlayerReachTile(tile, activePlayer)) {
            throw IllegalArgumentException("Can not move active player to $tile.")
        }
        movePlayerAcrossBoard(tile, activePlayer)
        activePlayer.treasureFound = activePlayer.treasureFound || tile.treasure == activePlayer.goal
        updateWinner(activePlayer, tile)
    }

    fun hasActivePlayerReachedGoal(): Boolean {
        return playerQueue.getCurrentPlayer().treasureFound
    }

    fun slideRowAndInsertSpare(rowPosition: RowPosition, direction: HorizontalDirection) {
        slideAndInsert { board.slideRowAndInsert(rowPosition,direction, this.spareTile) }
    }

    fun slideColumnAndInsertSpare(columnPosition: ColumnPosition, direction: VerticalDirection) {
        slideAndInsert { board.slideColAndInsert(columnPosition, direction, this.spareTile) }
    }

    fun kickOutActivePlayer() {
        val activePlayer = playerQueue.removeCurrentPlayer()
        board.removePlayerFromTiles(activePlayer)
    }

    /**
     * Performs a specific board sliding operation and then removes the player
     * from the newly created spare tile to the just inserted one if needed;
     */
    private fun slideAndInsert(getDislodgedAndSlide: () -> GameTile) {
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

    private fun canPlayerReachTile(tile: GameTile, player: Player): Boolean {
        val playerLocation = board.findPlayerLocation(player)
        return board.getReachableTiles(playerLocation).contains(tile)
    }

    private fun movePlayerAcrossBoard(tile: GameTile, activePlayer:Player) {
        board.removePlayerFromTiles(activePlayer)
        tile.addPlayerToTile(activePlayer)
    }

    private fun updateWinner(activePlayer: Player, targetTile: GameTile) {
        if (activePlayer.homeTile == targetTile && activePlayer.treasureFound) {
            winner = activePlayer
        }
    }
}