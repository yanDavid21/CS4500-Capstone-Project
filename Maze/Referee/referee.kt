package Players

import Common.*
import Common.board.Board
import Common.board.Position
import Common.player.PlayerData
import Common.tile.GameTile
import kotlin.math.abs

/**
 * A Maze game referee entity. To handle player's when running a game to completion.
 *
 * The following steps are done when starting a game:
 *  1. For every player (in the given order of ascending age), get a board suggestion.
 *  2. Chose a board and create the initial game state.
 *  3. In the same order as above, transmit the initial public state and treasure goal to every player.
 *  4. Play a game.
 *     - Until 1000 rounds pass, or a player wins, or every player passes, get the current player move.
 *     - If the move request terminates before the timeout with no raised exceptions and the move supplied is valid
 *       according to the rules of the game, then  apply the move to the state, move on the next player,
 *       otherwise kick out the player from the game and terminate all communication
 *     - Once the game is over, determine a winner:
 *       * A player who found its home after having found the treasure wins.
 *       * If no single player accomplished this, the players who share the smallest euclidian distance to home after
 *         having found the treasure are all tied winners.
 *       * If no player found the treasure, the players who share the smallest euclidian distance to the treasure tile
 *         are all winner.
 *  5. For every player with active communication, send its win/loss information.
 */
abstract class Referee {

    /**
     * Selects a suggested board and creates a game from it.
     */
    protected abstract fun createStateFromChosenBoard(suggestedBoards: List<Board>, players: List<PlayerMechanism>): GameState

    /**
     * Begins a game given a list of players (ordered by player age). Sets initial game data to all players,
     * plays one full game, and sends winning player data to players.
     */
    fun startGame(players: List<PlayerMechanism>) {
        val (gameState, playersThatSuggestedBoard) = setup(players)

        val playersThatResponded = sendInitialGameStateData(gameState, playersThatSuggestedBoard)

        val endGameData =  playGame(gameState, playersThatResponded)

        sendGameOverInformation(endGameData, playersThatResponded)
    }

    /**
     * Get board suggests from all players, creates a game with the chosen one.
     */
    private fun setup(players: List<PlayerMechanism>): Pair<GameState, List<PlayerMechanism>> {
        val (suggestedBoards, playersInGame) = queryAllPlayers(players) {
            it.proposeBoard0(Position.WIDTH, Position.HEIGHT)
        }
        val gameState = createStateFromChosenBoard(
            validateBoards(suggestedBoards), playersInGame
        )
        return Pair(gameState, playersInGame)
    }

    /**
     * Transmits initial game data to all players, including the public game state and the player's goal.
     */
    private fun sendInitialGameStateData(gameState: GameState, players: List<PlayerMechanism>): List<PlayerMechanism> {
        val initialPlayerState = gameState.toPublicState()
        val (_, playersThatResponded) = queryAllPlayers(players) { player ->
            val goal = gameState.getPlayerGoal(player.name)
            player.setupAndUpdateGoal(initialPlayerState, goal)
        }
        return playersThatResponded
    }


    /**
     * Runs a single game of maze to completion.
     */
    fun playGame(initialState: GameState, players: List<PlayerMechanism>): Map<String, Boolean> {
        val playerData = players.associateBy { it.name }
        var state = initialState
        var roundCount = 0
        while (!state.isGameOver() && roundCount < MAX_ROUNDS) {
            val currentPlayer = state.getActivePlayer()

            state = playerData[currentPlayer.id]?.let { playerMechanism ->
                runRoundSafelyWithTimeout(currentPlayer, playerMechanism, state)
            } ?: state.kickOutActivePlayer()

            roundCount += 1
        }

        return getWinners(state)
    }

    /**
     * Transmits endgame data, a single win/loss value.
     */
    private fun sendGameOverInformation(endgameData: Map<String, Boolean>, players: List<PlayerMechanism>) {
        players.forEach { player ->
            endgameData[player.name]?.let { playerWon -> player.won(playerWon)}
        }
    }

    private fun <T> queryAllPlayers(players: List<PlayerMechanism>, action: (PlayerMechanism) -> T): Pair<List<T>, List<PlayerMechanism>> {
        val answers = mutableListOf<T>()
        val playersThatResponded = mutableListOf<PlayerMechanism>()
        for (player in players) {
            val playerAnswer = safelyQueryPlayer(player, action)
            playerAnswer?.let { answers.add(it) ; playersThatResponded.add(player) }
        }
        return Pair(answers, playersThatResponded)
    }

    private fun <T> safelyQueryPlayer(player: PlayerMechanism, action: (PlayerMechanism) -> T): T? {
        return try {
            //runBlocking { withTimeout(TIMEOUT) {
                action(player)
            //} }
        } catch (_: Exception) {
            null
        }
    }

    /**
     * Runs a single round. If the player API call takes longer than TIMEOUT seconds, or throws an exception, the player
     * will be removed from the game.
     */
    private fun runRoundSafelyWithTimeout(currentPlayer: PlayerData, currentMechanism: PlayerMechanism, state: GameState): GameState {
        return safelyQueryPlayer(currentMechanism) {
            playOneRound(currentPlayer, currentMechanism, state)
        } ?: state.kickOutActivePlayer()
    }

    /**
     * Plays an entire round with the currentPlayer. Receives a move from players, if it is valid it executes it,
     * otherwise kick out the player. If the player reached the treasure, it will send it its home.
     */
    private fun playOneRound(currentPlayer: PlayerData, currentMechanism: PlayerMechanism, state: GameState): GameState {
        val suggestedMove = currentMechanism.takeTurn(state.toPublicState())
        val newState = if (isMoveValid(suggestedMove, state)) {
            performMove(suggestedMove, state)
        } else {
            state.kickOutActivePlayer()
        }
        if (newState.hasActivePlayerReachedTreasure()) {
            currentMechanism.setupAndUpdateGoal(null, currentPlayer.homePosition)
        }
        return newState.endActivePlayerRound()
    }

    /**
     * Finds winning information from a final game state.
     * If there is a winner, marks the single player as winner, everyone else as a loser.
     * Otherwise, it marks the players closest to winning.
     */
    private fun getWinners(state: GameState): Map<String, Boolean> {
        return state.winner?.let { winner ->
            state.getPlayersData().mapValues { (_, player) -> player == winner }
        } ?:  getPlayersClosestToWinning(state)
    }

    /**
     * Gets the players who share the smallest euclidian distance to their home tile (if they found their treasure),
     * or the players who share the smallest Euclidian distance to their treasure tile
     */
    private fun getPlayersClosestToWinning(state: GameState): Map<String, Boolean> {
        val playersData = state.getPlayersData()
        if (playersData.isEmpty()) {
            return mapOf()
        }
        // will only return null if playersData is empty
        val minimumDistance = playersData.values.minOfOrNull { it.getGoal().euclidDistanceTo(it.currentPosition) }!!

        return if (didAPlayerFindTreasure(playersData)) {
            findPlayersWhoFoundClosestToHomeAfterFindingTreasure(playersData.values, minimumDistance)
        } else {
            findPlayersWhoWereClosestToTreasure(playersData.values, minimumDistance)
        }
    }

    /**
     * Returns the players who have found their respective treasure that are the closest to their home tile.
     */
    private fun findPlayersWhoFoundClosestToHomeAfterFindingTreasure(players: Collection<PlayerData>, minDistance: Double): Map<String, Boolean> {
        return players.associate {
            val isClosest = it.currentPosition.euclidDistanceTo(it.homePosition).equalsDelta(minDistance)
            Pair(it.id, it.treasureFound && isClosest)
        }
    }

    /**
     * Returns the players that were closest to their respective treasure.
     */
    private fun findPlayersWhoWereClosestToTreasure(players: Collection<PlayerData>, minDistance: Double): Map<String, Boolean> {
        return players.associate {
            Pair(it.id, it.currentPosition.euclidDistanceTo(it.goalPosition).equalsDelta(minDistance))
        }
    }

    /**
     * Returns whether at least one player has found their respective treasure.
     */
    private fun didAPlayerFindTreasure(players: Map<String, PlayerData>): Boolean {
        for (player in players.values) {
            if (player.treasureFound) {
                return true
            }
        }
        return false
    }

    /**
     * Returns if the move is valid based on the current state.
     */
    private fun isMoveValid(action: Action, state: GameState): Boolean {
        return when(action) {
            is Skip -> true
            is RowAction -> state.isValidRowMove(action.rowPosition, action.direction, action.rotation, action.newPosition)
            is ColumnAction -> state.isValidColumnMove(action.columnPosition, action.direction, action.rotation, action.newPosition)
            else -> throw IllegalArgumentException("Not a valid action: $action")
        }
    }

    /**
     * Perform the move on behalf of player and returns the resulting GameState.
     */
    private fun performMove(action: Action, state: GameState): GameState {
        return when(action) {
            is Skip -> state.passCurrentPlayer()
            is RowAction -> state.slideRowAndInsertSpare(action.rowPosition, action.direction, action.rotation, action.newPosition)
            is ColumnAction -> state.slideColumnAndInsertSpare(action.columnPosition, action.direction, action.rotation, action.newPosition)
            else -> throw IllegalArgumentException("Not a valid action: $action")
        }
    }

    /**
     * Given a list of 2d arrays of tiles, returns the boards that are valid.
     */
    private fun validateBoards(suggestedBoardTiles: List<Array<Array<GameTile>>>): List<Board> {
        val validBoards = mutableListOf<Board>()
        for (suggestedTiles in suggestedBoardTiles) {
            if (Board.tilesAreValid(suggestedTiles)) {
                validBoards.add(Board(suggestedTiles))
            }
        }
        return validBoards
    }



    companion object {
        const val TIMEOUT = 15L // in seconds
        const val MAX_ROUNDS = 10000
        private const val DELTA = 0.000001
        fun Double.equalsDelta(other: Double) = abs(this - other) < DELTA // equality check for Doubles using DELTA
    }
}