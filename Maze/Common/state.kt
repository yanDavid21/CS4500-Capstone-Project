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

    /**
     * Moves the currently active player from it tile to a given destination.
     *
     * Throws IllegalArgumentException if the given tile is not reachable.
     */
    fun moveActivePlayer(to: Coordinates) {
        val activePlayer = playerQueue.getCurrentPlayer()
        checkActiveMovePlayer(activePlayer, activePlayer.currentPosition, to)

        movePlayerAcrossBoard(activePlayer, activePlayer.currentPosition, to)
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
        val activePlayer = playerQueue.removeCurrentPlayer()
        val playerPosition = activePlayer.currentPosition
        this.board = board.setTile(playerPosition, board.getTile(playerPosition).removePlayerFromTile(activePlayer))
    }

    /**
     * Looks for the player in the board, if not found throws exception.
     */
    private fun findPlayerPosition(player: Player): Coordinates {
        for (rowPos in Position.MIN_ROW_INDEX until Position.MAX_ROW_INDEX) {
            for (colPos in Position.MIN_COL_INDEX until Position.MAX_COL_INDEX) {
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
     * Performs a specific board sliding operation and then removes the player
     * from the newly created spare tile to the just inserted one if needed;
     */
    private fun slideInsertAndUpdateSpare(degree: Degree, getDislodgedAndSlide: () -> Pair<Board, GameTile>) {
        this.spareTile = this.spareTile.rotate(degree)
        val toBeInserted = this.spareTile

        val newBoardAndSpare = getDislodgedAndSlide()
        this.board = newBoardAndSpare.first
        this.spareTile = newBoardAndSpare.second

        moveAmnestiedPlayersIfAny(this.spareTile, toBeInserted)
    }

    private fun moveAmnestiedPlayersIfAny(fromTile: GameTile, toTile: GameTile) {
        fromTile.getPlayersOnTile().forEach {
            fromTile.removePlayerFromTile(it)
            toTile.addPlayerToTile(it)
        }
    }

    /**
     * Moves all players in thr queue when a column is slide up or down.
     */
    private fun movePlayersAfterColumnSlide(columnBeingSlidPosition: ColumnPosition, direction: VerticalDirection) {
        playerQueue.get().forEach { player ->
            val newRowPosition = player.currentPosition.row.nextPosition(direction)
            player.currentPosition = newRowPosition?.let { Coordinates(it, columnBeingSlidPosition)}
                ?: board.getEmptySlotPositionAfterSliding(columnBeingSlidPosition, direction)

        }
    }


    private fun canPlayerReachTile(player: Player, location: Coordinates): Boolean {
        return board.getReachableTiles(player.currentPosition).contains(location)
    }

    private fun movePlayerAcrossBoard(activePlayer: Player, currentPosition: Coordinates, targetCoord: Coordinates) {
        val tileToMoveTo = board.getTile(targetCoord)

        this.board = board
            .setTile(currentPosition, board.getTile(currentPosition).removePlayerFromTile(activePlayer))
            .setTile(targetCoord, tileToMoveTo.addPlayerToTile(activePlayer))

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