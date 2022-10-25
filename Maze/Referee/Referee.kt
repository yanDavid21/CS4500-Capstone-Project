package Referee

import Common.GameState
import Common.MovingAction
import Common.PlayerState
import Common.board.Board
import Common.board.Position
import Common.player.Player
import Common.tile.GameTile
import Players.PlayerMechanism

abstract class Referee {

    abstract fun setUp(suggestedBoards: List<Board>, players: List<PlayerMechanism>): GameState

    fun startGame(players: List<PlayerMechanism>): WinningPlayers {
        val suggestedBoards = players.map {
            it.proposeBoard(Position.MAX_ROW_INDEX + 1, Position.MAX_COL_INDEX + 1)
        }

        val gameState = setUp(suggestedBoards, players)
        val initialPlayerState = gameState.createPlayerState()
        players.forEach { player ->
            val goal = gameState.getPlayerGoal(player.name)
            player.setup(initialPlayerState, goal)
        }

        return playGame(gameState, players)
    }


    fun playGame(state: GameState, players: List<PlayerMechanism>): WinningPlayers {
        val currentPlayer = players[0]
        val move = currentPlayer.takeTurn(state.createPlayerState())

        currentPlayer.takeTurn()
        players[0].takeTurn()
        state.moveActivePlayer()
    }

}