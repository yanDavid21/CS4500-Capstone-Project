package Referee

import Common.*
import Common.board.Board
import Common.board.Position
import Common.player.Player
import Common.tile.GameTile
import Common.tile.treasure.Treasure
import Players.PlayerMechanism
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
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
    abstract fun createStateFromChosenBoard(suggestedBoards: List<Board>, players: List<PlayerMechanism>): GameState

    fun startGame(players: List<PlayerMechanism>) {
        val gameState = setup(players)

        sendInitialGameStateData(gameState, players)

        val endGameData =  playGame(gameState, players)

        sendGameOverInformation(endGameData, players)
    }

    /**
     * Get board suggests from all players, creates a game with the chosen one.
     */
    private fun setup(players: List<PlayerMechanism>): GameState {
        val suggestedBoards = players.map {
            it.proposeBoard0(Position.WIDTH, Position.HEIGHT)
        }
        return createStateFromChosenBoard(
            validateBoards(suggestedBoards), players
        )
    }

    /**
     * Transmits initial game data to all players, including the public game state and the player's goal.
     */
    private fun sendInitialGameStateData(gameState: GameState, players: List<PlayerMechanism>) {
        val initialPlayerState = gameState.toPublicState()
        players.forEach { player ->
            val goal = gameState.getPlayerGoal(player.name)
            player.setupAndUpdateGoal(initialPlayerState, goal)
        }
    }


    /**
     * Runs a single game of maze to completion.
     */
    private fun playGame(initialState: GameState, players: List<PlayerMechanism>): Map<String, Boolean> {
        val playerData = players.associateBy { it.name }
        var state = initialState
        var roundCount = 0
        while (!state.isGameOver() && roundCount < MAX_ROUNDS) {
            val currentPlayer = state.getActivePlayer()
            val currentMechanism = playerData[currentPlayer.id] ?: throw IllegalStateException("Invalid player.")

            state = runRoundSafelyWithTimeout(currentPlayer, currentMechanism, state)
            roundCount++
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

    /**
     * Runs a single round. If the player API call takes longer than TIMEOUT seconds, or throws an exception, the player
     * will be removed from the game.
     */
    private fun runRoundSafelyWithTimeout(currentPlayer: Player, currentMechanism: PlayerMechanism, state: GameState): GameState {
        return runBlocking {
            withTimeout(TIMEOUT) {
                try {
                    playOneRound(currentPlayer, currentMechanism, state)
                } catch (exception: Exception) {
                    state.kickOutActivePlayer()
                }
            }
        }
    }

    private fun playOneRound(currentPlayer:Player, currentMechanism: PlayerMechanism, state: GameState): GameState {
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

    private fun getWinners(state: GameState): Map<String, Boolean> {
        return state.winner?.let { winner ->
            state.getPlayersData().mapValues { (_, player) -> player == winner }
        } ?:  getPlayersClosestToWinning(state)
    }

    private fun getPlayersClosestToWinning(state: GameState): Map<String, Boolean> {
        val playersData = state.getPlayersData()
        if (playersData.isEmpty()) {
            return mapOf()
        }
        // will only return null if playersData is empty
        val minimumDistance = playersData.values.minOfOrNull { it.getGoal().euclidDistanceTo(it.currentPosition) }!!

        return if (didAPlayerFindTreasure(playersData)) {
            findWinnersWhoFoundClosestToHomeTreasure(playersData.values, minimumDistance)
        } else {
            findWinnersWhoWereClosestToTreasure(playersData.values, minimumDistance)
        }
    }

    private fun findWinnersWhoFoundClosestToHomeTreasure(players: Collection<Player>, minDistance: Double): Map<String, Boolean> {
        return players.associate {
            val isClosest = it.currentPosition.euclidDistanceTo(it.goalPosition).equalsDelta(minDistance)
            Pair(it.id, it.treasureFound && isClosest)
        }
    }
    
    private fun findWinnersWhoWereClosestToTreasure(players: Collection<Player>, minDistance: Double): Map<String, Boolean> {
        return players.associate {
            Pair(it.id, it.currentPosition.euclidDistanceTo(it.homePosition).equalsDelta(minDistance))
        }
    }

    private fun didAPlayerFindTreasure(players: Map<String, Player>): Boolean {
        for (player in players.values) {
            if (player.treasureFound) {
                return true
            }
        }
        return false
    }

    private fun isMoveValid(action: Action, state: GameState): Boolean {
        return when(action) {
            is Skip -> true
            is RowAction -> state.isValidRowMove(action.rowPosition, action.direction, action.rotation, action.newPosition)
            is ColumnAction -> state.isValidColumnMove(action.columnPosition, action.direction, action.rotation, action.newPosition)
            else -> throw IllegalArgumentException("Not a valid action: $action")
        }
    }

    private fun performMove(action: Action, state: GameState): GameState {
        return when(action) {
            is Skip -> state.passCurrentPlayer()
            is RowAction -> state.slideRowAndInsertSpare(action.rowPosition, action.direction, action.rotation, action.newPosition)
            is ColumnAction -> state.slideColumnAndInsertSpare(action.columnPosition, action.direction, action.rotation, action.newPosition)
            else -> throw IllegalArgumentException("Not a valid action: $action")
        }
    }

    private fun validateBoards(suggestedBoardTiles: List<Array<Array<GameTile>>>): List<Board> {
        val validBoards = mutableListOf<Board>()
        for (suggestedTiles in suggestedBoardTiles) {
            if (tilesAreValid(suggestedTiles)) {
                validBoards.add(Board(suggestedTiles))
            }
        }
        return validBoards
    }

    private fun tilesAreValid(tiles: Array<Array<GameTile>>): Boolean {
        if (tiles.size != Position.MAX_COL_INDEX + 1) {
            return false
        }
        if (tiles[0].size != Position.MAX_ROW_INDEX + 1) {
            return false
        }
        val gems = tiles.flatten().map { it.treasure }
        return allGemsAreUnique(gems)
    }

    private fun allGemsAreUnique(gems: List<Treasure>): Boolean {
        val setOfGems = mutableSetOf<Treasure>()
        for (gem in gems) {
            if (!setOfGems.contains(gem)) {
                setOfGems.add(gem)
            } else {
                return false
            }
        }
        return true
    }

    companion object {
        const val TIMEOUT = 4L // in seconds
        const val MAX_ROUNDS = 10000
        private const val DELTA = 0.000001
        fun Double.equalsDelta(other: Double) = abs(this - other) < DELTA
    }
}