package Common

import Common.board.Board
import Common.board.ColumnPosition
import Common.board.Coordinates
import Common.board.RowPosition
import Common.player.Player
import Common.player.PlayerQueue
import Common.player.PublicPlayerData
import Common.tile.Degree
import Common.tile.GameTile
import Common.tile.HorizontalDirection
import Common.tile.VerticalDirection

/**
 * Contains all knowledge about the game state. Including all Player state (player id, goal tile, home tile),
 * tiles on the board, what the current spare tile is, whose turn it is, if there is a winner.
 */
class GameState(
    private var board: Board,
    private var spareTile: GameTile,
    players: List<Player>,
    private var lastMovingAction: MovingAction? = null
) {
    private val playerQueue = PlayerQueue(players.toMutableList())
    var winner: Player? = null
    private var consecutiveSkips: Int = 0



    /**
     * Returns whether the active player reached its goal.
     */
    fun hasActivePlayerReachedGoal(): Boolean {
        return playerQueue.getCurrentPlayer().treasureFound
    }

    /**
     * Slides a row in the board in the provided direction, then rotates and inserts the spare tile into the vacant spot.
     */
    fun slideRowAndInsertSpare(rowPosition: RowPosition, direction: HorizontalDirection, degree: Degree, to: Coordinates) {
        val rotatedSpare = this.spareTile.rotate(degree)
        val (board, spareTile) = board.slideRowAndInsert(rowPosition,direction, rotatedSpare)
        this.board = board
        this.spareTile = spareTile
        movePlayersAfterRowSlide(rowPosition, direction)
        moveActivePlayer(to)
        this.lastMovingAction = RowAction(rowPosition, direction, degree, to)
    }

    /**
     * Slides a column in the board in the provided direction, then rotates and inserts the spare tile into the vacant spot.
     */
    fun slideColumnAndInsertSpare(columnPosition: ColumnPosition, direction: VerticalDirection, degree: Degree, to: Coordinates) {
        val rotatedSpare = this.spareTile.rotate(degree)
        val (board, spareTile) = board.slideColAndInsert(columnPosition, direction, rotatedSpare)
        this.board = board
        this.spareTile = spareTile
        movePlayersAfterColumnSlide(columnPosition, direction)
        moveActivePlayer(to)
        this.lastMovingAction = ColumnAction(columnPosition, direction, degree, to)
    }

    fun isValidRowMove(rowPosition: RowPosition, direction: HorizontalDirection, degree: Degree, to: Coordinates): Boolean {
        val (board, _) = board.slideRowAndInsert(rowPosition, direction, spareTile.rotate(degree))
        movePlayersAfterRowSlide(rowPosition, direction)
        val isValidMove = canPlayerReachTile(playerQueue.getCurrentPlayer(), to, board)
        movePlayersAfterRowSlide(rowPosition, direction.reverse() as HorizontalDirection)
        return isValidMove
    }

    fun isValidColumnMove(columnPosition: ColumnPosition, direction: VerticalDirection, degree: Degree, to: Coordinates): Boolean {
        val (board, _) = board.slideColAndInsert(columnPosition, direction, spareTile.rotate(degree))
        movePlayersAfterColumnSlide(columnPosition, direction)
        val isValidMove = canPlayerReachTile(playerQueue.getCurrentPlayer(), to, board)
        movePlayersAfterColumnSlide(columnPosition, direction.reverse() as VerticalDirection)
        return isValidMove
    }

    /**
     * Returns a mapping of all the player's current goal positions.
     */
    fun getPlayerGoal(playerName: String): Coordinates {
        return playerGoals().getOrElse(playerName) {
            throw IllegalArgumentException("Could not find goal for player: $playerName")
        }
    }

    fun getPlayerData(): Map<String, Player> {
        return playerQueue.get().associateBy { it.id }
    }

    /**
     * Moves the currently active player from it tile to a given destination.
     *
     * Throws IllegalArgumentException if the given tile is not reachable.
     */
    private fun moveActivePlayer(to: Coordinates) {
        val activePlayer = playerQueue.getCurrentPlayer()
        checkActiveMovePlayer(activePlayer, activePlayer.currentPosition, to)

        movePlayerAcrossBoard(activePlayer, to)
        playerQueue.getNextPlayer()
    }

    /**
     * Passes the current player.
     */
    fun passCurrentPlayer() {
        this.playerQueue.getNextPlayer()
        this.consecutiveSkips += 1
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

    fun getActivePlayer(): Player {
        return playerQueue.getCurrentPlayer()
    }

    fun toPublicState(): PublicGameState {
        return PublicGameState(board, spareTile, lastMovingAction,
            playerQueue.get().associate { player -> Pair(player.id, player.toPublicPlayerData()) })
    }

    fun isGameOver(): Boolean {
        return winner?.let { true } ?: consecutiveSkips == playerQueue.size()
    }

    private fun playerGoals(): Map<String, Coordinates> {
        return playerQueue.get().associate { player ->
            Pair(player.id,  player.getGoal())
        }
    }

    /**
     * Moves all players in the queue when a row is slid left or right.
     */
    private fun movePlayersAfterRowSlide(rowBeingSlidPosition: RowPosition, direction: HorizontalDirection) {
        playerQueue.get().forEach { player ->
            if (player.currentPosition.row == rowBeingSlidPosition) {
                val newColumnPosition: ColumnPosition? = player.currentPosition.col.nextPosition(direction)
                val newPlayerCoordinates =  newColumnPosition?.let { Coordinates(rowBeingSlidPosition, it) }
                    ?: board.getEmptySlotPositionAfterSliding(rowBeingSlidPosition, direction)
                movePlayerAcrossBoard(player, newPlayerCoordinates)
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
                val newPlayerCoordinates = newRowPosition?.let { Coordinates(it, columnBeingSlidPosition) }
                    ?: board.getEmptySlotPositionAfterSliding(columnBeingSlidPosition, direction)
                movePlayerAcrossBoard(player, newPlayerCoordinates)
            }
        }
    }

    private fun canPlayerReachTile(player: Player, location: Coordinates, board: Board): Boolean {
        return board.getReachableTiles(player.currentPosition).contains(location)
    }

    private fun canPlayerReachTile(player: Player, location: Coordinates): Boolean {
        return canPlayerReachTile(player, location, board)
    }


    private fun movePlayerAcrossBoard(activePlayer: Player, targetCoord: Coordinates) {
        activePlayer.currentPosition = targetCoord
        activePlayer.treasureFound = activePlayer.treasureFound || targetCoord == activePlayer.goalPosition

        updateWinner(activePlayer)
    }

    private fun updateWinner(activePlayer: Player) {
        if (activePlayer.currentPosition == activePlayer.homePosition && activePlayer.treasureFound) {
            winner = activePlayer
        }
    }

    private fun checkActiveMovePlayer(activePlayer: Player, currentPosition: Coordinates, to: Coordinates) {
        if (!canPlayerReachTile(activePlayer, to) || currentPosition == to) {
            throw IllegalArgumentException("Can not move active player to $to.")
        }
    }
}

/**
 * Holds the data a particular player will know about the game.
 */
data class PublicGameState(
    val board: Board, val spareTile: GameTile, val lastAction: MovingAction?, val publicPlayerData: Map<String, PublicPlayerData>
) {

    fun getPlayerData(playerName: String): PublicPlayerData {
        return publicPlayerData[playerName] ?: throw IllegalStateException("Could not find player data for player: $playerName")
    }
}