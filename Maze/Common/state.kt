package Common

import Common.board.Board
import Common.board.ColumnPosition
import Common.board.Coordinates
import Common.board.RowPosition
import Common.player.Player
import Common.player.PlayerQueue
import Common.tile.Degree
import Common.tile.GameTile
import Common.tile.HorizontalDirection
import Common.tile.VerticalDirection

/**
 * Contains all knowledge about the game state. Including all Player state (player id, goal tile, home tile),
 * tiles on the board, what the current spare tile is, whose turn it is, if there is a winner.
 */
class Referee(
    private var board: Board,
    private var spareTile: GameTile,
    players: List<Player>
) {
    private val playerQueue = PlayerQueue(players.toMutableList())
    private var winner: Player? = null
    private var lastAction: Action? = null

    /**
     * Moves the currently active player from it tile to a given destination.
     *
     * Throws IllegalArgumentException if the given tile is not reachable.
     */
    fun moveActivePlayer(to: Coordinates) {
        val activePlayer = playerQueue.getCurrentPlayer()
        checkActiveMovePlayer(activePlayer, activePlayer.currentPosition, to)

        movePlayerAcrossBoard(activePlayer, to)
        playerQueue.getNextPlayer()
    }


    /**
     * Returns whether the active player reached its goal.
     */
    fun hasActivePlayerReachedGoal(): Boolean {
        return playerQueue.getCurrentPlayer().treasureFound
    }

    /**
     * Slides a row in the board in the provided direction, then rotates and inserts the spare tile into the vacant spot.
     */
    fun slideRowAndInsertSpare(rowPosition: RowPosition, direction: HorizontalDirection, degree: Degree) {
        val rotatedSpare = this.spareTile.rotate(degree)
        val (board, spareTile) = board.slideRowAndInsert(rowPosition,direction, rotatedSpare)
        this.board = board
        this.spareTile = spareTile
        movePlayersAfterRowSlide(rowPosition, direction)
    }

    /**
     * Slides a column in the board in the provided direction, then rotates and inserts the spare tile into the vacant spot.
     */
    fun slideColumnAndInsertSpare(columnPosition: ColumnPosition, direction: VerticalDirection, degree: Degree) {
        val rotatedSpare = this.spareTile.rotate(degree)
        val (board, spareTile) = board.slideColAndInsert(columnPosition, direction, rotatedSpare)
        this.board = board
        this.spareTile = spareTile
        movePlayersAfterColumnSlide(columnPosition, direction)
    }

    /**
     * Passes the current player.
     */
    fun passCurrentPlayer() {
        this.playerQueue.getNextPlayer()
    }

    /**
     * Removes the active player from the queue and from all the tiles. Automatically makes the
     * active player the next player in the queue.
     */
    fun kickOutActivePlayer() {
        playerQueue.removeCurrentPlayer()
    }

    fun getBoard(): Board {
        return this.board.getCopyOfBoard()
    }

    /**
     * Moves all players in the queue when a row is slid left or right.
     */
    private fun movePlayersAfterRowSlide(rowBeingSlidPosition: RowPosition, direction: HorizontalDirection) {
        playerQueue.get().forEach { player ->
            if (player.currentPosition.row == rowBeingSlidPosition) {
                val newColumnPosition: ColumnPosition? = player.currentPosition.col.nextPosition(direction)
                player.currentPosition = newColumnPosition?.let { Coordinates(rowBeingSlidPosition, it) }
                    ?: board.getEmptySlotPositionAfterSliding(rowBeingSlidPosition, direction)
            }
        }
    }

    /**
     * Moves all players in thr queue when a column is slide up or down.
     */
    private fun movePlayersAfterColumnSlide(columnBeingSlidPosition: ColumnPosition, direction: VerticalDirection) {
        playerQueue.get().forEach { player ->
            if (player.currentPosition.col == columnBeingSlidPosition) {
                val newRowPosition = player.currentPosition.row.nextPosition(direction)
                player.currentPosition = newRowPosition?.let { Coordinates(it, columnBeingSlidPosition) }
                    ?: board.getEmptySlotPositionAfterSliding(columnBeingSlidPosition, direction)
            }
        }
    }

    private fun checkSlidingIsNotUndoingLastAction() {
        lastAction?.let {

        }
    }


    private fun canPlayerReachTile(player: Player, location: Coordinates): Boolean {
        return board.getReachableTiles(player.currentPosition).contains(location)
    }


    private fun movePlayerAcrossBoard(activePlayer: Player, targetCoord: Coordinates) {
        val tileToMoveTo = board.getTile(targetCoord)

        activePlayer.currentPosition = targetCoord
        activePlayer.treasureFound = activePlayer.treasureFound || tileToMoveTo.treasure == activePlayer.goal

        updateWinner(activePlayer, tileToMoveTo)
    }

    private fun updateWinner(activePlayer: Player, targetTile: GameTile) {
        if (activePlayer.homeTile == targetTile && activePlayer.treasureFound) {
            winner = activePlayer
        }
    }

    private fun checkActiveMovePlayer(activePlayer: Player, currentPosition: Coordinates, to: Coordinates) {
        if (!canPlayerReachTile(activePlayer, to) || currentPosition == to) {
            throw IllegalArgumentException("Can not move active player to $to.")
        }
    }
}