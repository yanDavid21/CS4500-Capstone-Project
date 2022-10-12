package Common.referee

import Common.GameState
import Common.Player
import Common.PlayerQueue
import Common.board.Board
import Common.board.ColumnPosition
import Common.board.RowPosition
import Common.tile.*

class Referee(
    private val board: Board,
    private var spareTile: GameTile,
    players: List<Player>
) {
    private var nextAction: GameState = GameState.INITIAL
    private val playerQueue = PlayerQueue(players.toMutableList())

    fun moveActivePlayer(tile: GameTile) {
        if (!activePlayerCanReachTile(tile)) {
            throw IllegalArgumentException("Can not move active player to $tile.")
        }
        val activePlayer = playerQueue.getCurrentPlayer()

        board.removePlayerFromTiles(activePlayer)
        tile.addPlayerToTile(activePlayer)

        // TODO: add check treasure method to tile

        // checkWinConditions
    }

    fun hasActivePlayerReachedGoal(): Boolean {
        return playerQueue.getCurrentPlayer().treasureFound
    }

    fun slideRowAndInsertSpare(rowPosition: RowPosition, direction: HorizontalDirection) {
        val dislodgedTile = board.slideRowAndInsert(rowPosition,direction, this.spareTile)
        this.spareTile = dislodgedTile
        //moveAmnestiedPlayersIfAny(this.spareTile, toBeInserted)
    }

    fun slideColumnAndInsertSpare(columnPosition: ColumnPosition, direction: VerticalDirection) {
        val dislodgedTile = board.slideColAndInsert(columnPosition, direction, this.spareTile)
        this.spareTile = dislodgedTile
        //moveAmnestiedPlayersIfAny(this.spareTile, toBeInserted)
    }

    fun kickOutActivePlayer() {
        val activePlayer = playerQueue.removeCurrentPlayer()
        board.removePlayerFromTiles(activePlayer)
    }

    private fun moveAmnestiedPlayersIfAny(fromTile: GameTile, toTile: GameTile) {
        fromTile.getPlayers().forEach {
            fromTile.removePlayerFromTile(it)
            toTile.addPlayerToTile(it)
        }
    }

    private fun activePlayerCanReachTile(tile: GameTile): Boolean {
        val currentPlayer = playerQueue.getCurrentPlayer()
        val playerLocation = board.findPlayerLocation(currentPlayer)
        return board.getReachableTiles(playerLocation).contains(tile)
    }
}