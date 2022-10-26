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
        val suggestedBoards = players.map {
            it.proposeBoard0(Position.MAX_ROW_INDEX + 1, Position.MAX_COL_INDEX + 1)
        }

        val gameState = setUp(suggestedBoards, players)
        val initialPlayerState = gameState.toPublicState()
        players.forEach { player ->
            val goal = gameState.getPlayerGoal(player.name)
            player.setupAndUpdateGoal(initialPlayerState, goal)
        }

        val endGameData =  playGame(gameState, players.associateBy { it.name })
        players.forEach {
            val playerWon = endGameData[it.name]!!
            it.won(playerWon)
        }
    }

    fun setUp(suggestedBoards: List<Array<Array<GameTile>>>, players: List<PlayerMechanism>): GameState {
        return choseBoard(
            validateBoards(suggestedBoards), players
        )
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


    fun playGame(state: GameState, players: Map<String, PlayerMechanism>): Map<String, Boolean> {

        while (!state.isGameOver()) {
            val currentPlayer = state.getActivePlayer()
            val currentMechanism = players[currentPlayer.id] ?: throw IllegalStateException("Invalid player.")


            val suggestedMove = currentMechanism.takeTurn(state.toPublicState())

            //runBlocking {  }

            if (isMoveValid(suggestedMove, state)) {
                performMove(suggestedMove, state)
                if (state.hasActivePlayerReachedGoal()) {
                    currentMechanism.setupAndUpdateGoal(null, currentPlayer.homePosition)
                }
            } else {
                state.kickOutActivePlayer()
            }
        }

        return getWinners(state, players.keys)
    }

    private fun <T> performSafely(action: () -> T, ifFails: () -> T): T {
        return try {
            action()
        } catch (e: IllegalArgumentException) {
            ifFails()
        }
    }

    private fun getWinners(state: GameState, players: Set<String>): Map<String, Boolean> {
        return state.winner?.let { winner ->
            players.associate { player -> Pair(player, player == winner.id) }
        } ?:  getPlayersClosestToWinning(state, players)
    }

    private fun getPlayersClosestToWinning(state: GameState, players: Set<String>): Map<String, Boolean> {
        val playerData = state.getPlayerData()
        val minimumDistance = playerData.values.map { it.getGoal().euclidDistanceTo(it.currentPosition) }.min()!!

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

    private fun findPlayersWhoFoundTreasure(players: Map<String, Player>): Set<Player> {
        val playersWhoFound = mutableSetOf<Player>()
        players.forEach { (_, player) ->
            if (player.treasureFound) {
                playersWhoFound.add(player)
            }
        }
        return playersWhoFound
    }

    private fun isMoveValid(action: Action, state: GameState): Boolean {
        return when(action) {
            is Skip -> true
            is RowAction -> state.isValidRowMove(action.rowPosition, action.direction, action.rotation, action.newPosition)
            is ColumnAction -> state.isValidColumnMove(action.columnPosition, action.direction, action.rotation, action.newPosition)
            else -> throw IllegalArgumentException("Not a valid action: $action")
        }
    }

    private fun performMove(action: Action, state: GameState) {
        when(action) {
            is Skip -> state.passCurrentPlayer()
            is RowAction -> state.slideRowAndInsertSpare(action.rowPosition, action.direction, action.rotation, action.newPosition)
            is ColumnAction -> state.slideColumnAndInsertSpare(action.columnPosition, action.direction, action.rotation, action.newPosition)
            else -> throw IllegalArgumentException("Not a valid action: $action")
        }
    }

    companion object {
        val TIMEOUT = Duration.ofSeconds(100)
    }
}