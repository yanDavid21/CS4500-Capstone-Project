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
import java.time.Duration

abstract class Referee {

    // implementations will chose square and odd boards
    abstract fun choseBoard(suggestedBoards: List<Board>, players: List<PlayerMechanism>): GameState

    fun startGame(players: List<PlayerMechanism>) {
        val gameState = setUp(players)

        sendInitialGameStateData(gameState, players)

        val endGameData =  playGame(gameState, players.associateBy { it.name })

        sendGameOverInformation(endGameData, players)
    }

    fun setUp(players: List<PlayerMechanism>): GameState {
        val suggestedBoards = players.map {
            it.proposeBoard0(Position.WIDTH, Position.HEIGHT)
        }
        return choseBoard(
            validateBoards(suggestedBoards), players
        )
    }

    fun sendInitialGameStateData(gameState: GameState, players: List<PlayerMechanism>) {
        val initialPlayerState = gameState.toPublicState()
        players.forEach { player ->
            val goal = gameState.getPlayerGoal(player.name)
            player.setupAndUpdateGoal(initialPlayerState, goal)
        }
    }

    fun sendGameOverInformation(endgameData: Map<String, Boolean>, players: List<PlayerMechanism>) {
        players.forEach { player ->
            endgameData[player.name]?.let { playerWon -> player.won(playerWon)}
        }
    }

    fun playGame(initialState: GameState, players: Map<String, PlayerMechanism>): Map<String, Boolean> {
        var state = initialState
        var roundCount = 0
        while (!state.isGameOver() && roundCount < MAX_ROUNDS) {
            val currentPlayer = state.getActivePlayer()
            val currentMechanism = players[currentPlayer.id] ?: throw IllegalStateException("Invalid player.")

            state = runRoundSafelyWithTimeout(currentPlayer, currentMechanism, state)
            roundCount++
        }

        return getWinners(state, players.keys)
    }

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

    private fun getWinners(state: GameState, players: Set<String>): Map<String, Boolean> {
        return state.winner?.let { winner ->
            players.associate { player -> Pair(player, player == winner.id) }
        } ?:  getPlayersClosestToWinning(state)
    }

    private fun getPlayersClosestToWinning(state: GameState): Map<String, Boolean> {
        val playersData = state.getPlayersData()
        if (playersData.isEmpty()) {
            return mapOf()
        }
        // will only return null if playersData is empty
        val minimumDistance = playersData.values.minOfOrNull { it.getGoal().euclidDistanceTo(it.currentPosition) }!!

        val playersWhoFoundTreasure = findPlayersWhoFoundTreasure(playerData)
        return if (playersWhoFoundTreasure.isNotEmpty()) {
            playerData.values.associate {
                val isAsCloseAsMin = it.currentPosition.euclidDistanceTo(it.goalPosition) == minimumDistance
                Pair(it.id, it.treasureFound && isAsCloseAsMin ) }
        } else {
            playerData.values.associate {
                Pair(it.id, it.currentPosition.euclidDistanceTo(it.goalPosition) == minimumDistance)
            }
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
        const val TIMEOUT = 2L // in seconds
        const val MAX_ROUNDS = 10000
        private const val DELTA = 0.000001
        fun Double.equalsDelta(other: Double) = abs(this - other) < DELTA
    }
}