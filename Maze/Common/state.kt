package Common

import Common.board.Board
import Common.board.ColumnPosition
import Common.board.Coordinates
import Common.board.RowPosition
import Common.player.PlayerData
import Common.player.PublicPlayerData
import Common.tile.Degree
import Common.tile.GameTile
import Common.tile.HorizontalDirection
import Common.tile.VerticalDirection

/**
 * Contains all knowledge about the game state. Including all Player state (player id, goal tile, home tile),
 * tiles on the board, what the current spare tile is, whose turn it is, if there is a winner.
 */
data class GameState(
    private val board: Board,
    private val spareTile: GameTile,
    private val players: List<PlayerData>,
    private val lastMovingAction: MovingAction? = null,
    val winner: PlayerData? = null,
    private val consecutiveSkips: Int = 0
) {
    /**
     * Returns whether the active player reached its goal.
     */
    fun hasActivePlayerReachedTreasure(): Boolean {
        return getActivePlayer().treasureFound
    }

    /**
     * Slides a row in the board in the provided direction, then rotates and inserts the spare tile into the vacant spot.
     */
    fun slideRowAndInsertSpare(rowPosition: RowPosition, direction: HorizontalDirection, degree: Degree, to: Coordinates): GameState {
        val rotatedSpare = this.spareTile.rotate(degree)
        val (board, spareTile) = board.slideRowAndInsert(rowPosition,direction, rotatedSpare)

        val playersAfterRowSlide = movePlayersAfterRowSlide(rowPosition, direction)
        val playersAfterActivePlayerMove = moveActivePlayer(playersAfterRowSlide, to, board)

        val potentialWinner = checkActivePlayerWon(playersAfterActivePlayerMove)

        val movingAction = RowAction(rowPosition, direction, degree, to)
        return GameState(board, spareTile, playersAfterActivePlayerMove, movingAction, potentialWinner, 0)
    }

    /**
     * Slides a column in the board in the provided direction, then rotates and inserts the spare tile into the vacant spot.
     */
    fun slideColumnAndInsertSpare(columnPosition: ColumnPosition, direction: VerticalDirection, degree: Degree, to: Coordinates): GameState {
        val rotatedSpare = this.spareTile.rotate(degree)
        val (board, spareTile) = board.slideColAndInsert(columnPosition, direction, rotatedSpare)

        val playersAfterColumnSlide = movePlayersAfterColumnSlide(columnPosition, direction)
        val playersAfterActivePlayerMove = moveActivePlayer(playersAfterColumnSlide, to, board)

        val potentialWinner = checkActivePlayerWon(playersAfterActivePlayerMove)

        val movingAction = ColumnAction(columnPosition, direction, degree, to)
        return GameState(board, spareTile, playersAfterActivePlayerMove, movingAction, potentialWinner, 0)
    }

    /**
     * Checks if a sliding action is valid by performing the move on the board and seeing if the active player
     * can reach the target position. Target position can not equal active player's previous position.
     */
    fun isValidRowMove(rowPosition: RowPosition, direction: HorizontalDirection, degree: Degree, to: Coordinates): Boolean {
        val activePlayer = getActivePlayer()
        val (board, _) = board.slideRowAndInsert(rowPosition, direction, spareTile.rotate(degree))
        val playersAfterRowSlide = movePlayersAfterRowSlide(rowPosition, direction)
        return to != activePlayer.currentPosition && canPlayerReachTile(getActivePlayer(playersAfterRowSlide), to, board)
    }

    /**
     * Checks if a sliding action is valid by performing the move on the board and seeing if the active player
     * can reach the target position. Target position can not equal active player's previous position.
     */
    fun isValidColumnMove(columnPosition: ColumnPosition, direction: VerticalDirection, degree: Degree, to: Coordinates): Boolean {
        val activePlayer = getActivePlayer()
        val (board, _) = board.slideColAndInsert(columnPosition, direction, spareTile.rotate(degree))
        val playersAfterColumnSlide = movePlayersAfterColumnSlide(columnPosition, direction)
        return to != activePlayer.currentPosition && canPlayerReachTile(getActivePlayer(playersAfterColumnSlide), to, board)
    }

    /**
     * Returns a mapping of all the player's current goal positions.
     */
    fun getPlayerGoal(playerName: String): Coordinates {
        return playerGoals().getOrElse(playerName) {
            throw IllegalArgumentException("Could not find goal for player: $playerName")
        }
    }

    /**
     * Returns a mapping of player identifiers to their data.
     */
    fun getPlayersData(): Map<String, PlayerData> {
        return players.associateBy { it.id }
    }

    /**
     * Moves the currently active player from it tile to a given destination.
     *
     * Throws IllegalArgumentException if the given tile is not reachable.
     */
    private fun moveActivePlayer(players: List<PlayerData>, to:Coordinates, board: Board): List<PlayerData> {
        val activePlayer = getActivePlayer(players)
        checkActivePlayerMove(activePlayer, activePlayer.currentPosition, to, board)
        val playerAfterMove = activePlayer.move(to)
        return listOf(playerAfterMove).plus(players.getNext())
    }

    private fun checkActivePlayerWon(players: List<PlayerData>): PlayerData? {
        val activePlayer = getActivePlayer(players)
        return if (activePlayer.treasureFound && activePlayer.currentPosition == activePlayer.homePosition) {
            activePlayer
        } else {
            null
        }
    }

    /**
     * Passes the current player. Increments the player's consecutive skips. Does not end the turn.
     */
    fun passCurrentPlayer(): GameState {
        return this.copy(
            consecutiveSkips = this.consecutiveSkips + 1
        )
    }

    /**
     * Removes the active player from the queue and from all the tiles. Automatically makes the
     * active player the next player in the queue.
     */
    fun kickOutActivePlayer(): GameState {
        return this.copy(
            players = this.players.getNext()
        )
    }

    /**
     * Finishes the current round. The new state will have the next player as the active player and the
     * currently active player at the end of the queue.
     */
    fun endActivePlayerRound(): GameState {
        return this.copy(
            players = this.players.popFirstAndMoveToLast()
        )
    }

    fun getBoard(): Board {
        return this.board.getCopyOfBoard()
    }

    fun toPublicState(): PublicGameState {
        return PublicGameState(board, spareTile, lastMovingAction,
            players.associate { player -> Pair(player.id, player.toPublicPlayerData()) })
    }

    /**
     * Determines if the game is finished. Ending conditions are:
     *  - All players have been kicked out
     *  - There is a winner
     *  - A round has passed with everyone skipping.
     */
    fun isGameOver(): Boolean {
        return this.players.isEmpty() || winner?.let { true } ?: (consecutiveSkips >= players.size)
    }

    private fun playerGoals(): Map<String, Coordinates> {
        return players.associate { player ->
            Pair(player.id,  player.getGoal())
        }
    }

    /**
     * Moves all players in the queue when a row is slid left or right.
     */
    private fun movePlayersAfterRowSlide(rowBeingSlidPosition: RowPosition, direction: HorizontalDirection): List<PlayerData>{
        return players.map { player ->
            if (player.currentPosition.row == rowBeingSlidPosition) {
                val newColumnPosition: ColumnPosition? = player.currentPosition.col.nextPosition(direction)
                val newPlayerCoordinates =  newColumnPosition?.let { Coordinates(rowBeingSlidPosition, it) }
                    ?: board.getEmptySlotPositionAfterSliding(rowBeingSlidPosition, direction)
                player.move(newPlayerCoordinates)
            } else {
                player
            }
        }
    }

    /**
     * Moves all players in thr queue when a column is slide up or down.
     */
    private fun movePlayersAfterColumnSlide(columnBeingSlidPosition: ColumnPosition, direction: VerticalDirection): List<PlayerData> {
        return players.map { player ->
            if (player.currentPosition.col == columnBeingSlidPosition) {
                val newRowPosition = player.currentPosition.row.nextPosition(direction)
                val newPlayerCoordinates = newRowPosition?.let { Coordinates(it, columnBeingSlidPosition) }
                    ?: board.getEmptySlotPositionAfterSliding(columnBeingSlidPosition, direction)
                player.move(newPlayerCoordinates)
            } else {
                player
            }
        }
    }

    private fun canPlayerReachTile(player: PlayerData, location: Coordinates, board: Board): Boolean {
        return board.getReachableTiles(player.currentPosition).contains(location)
    }


    private fun checkActivePlayerMove(activePlayer: PlayerData, currentPosition: Coordinates, to: Coordinates, board: Board) {
        if (!canPlayerReachTile(activePlayer, to, board)) {
            throw IllegalArgumentException("Can not move active player to $to.")
        }
    }

    fun getActivePlayer(): PlayerData {
        return getActivePlayer(this.players)
    }

    private fun getActivePlayer(players: List<PlayerData> = this.players): PlayerData {
        if (players.isEmpty()) {
            throw IllegalStateException("All playrs have been kicked out, could not get active player.")
        }
        return players[0]
    }
}

/**
 * Holds the public data players will know about the game.
 */
data class PublicGameState(
    val board: Board, val spareTile: GameTile, val lastAction: MovingAction?, val publicPlayerData: Map<String, PublicPlayerData>
) {

    fun getPlayerData(playerName: String): PublicPlayerData {
        return publicPlayerData[playerName] ?: throw IllegalStateException("Could not find player data for player: $playerName")
    }
}

fun <T> List<T>.popFirstAndMoveToLast(): List<T> {
    return if (this.isEmpty()) listOf() else this.getNext().plus(this.first())
}

fun <T> List<T>.getNext(): List<T> {
    return if (this.isEmpty()) listOf() else this.subList(1, this.size )
}