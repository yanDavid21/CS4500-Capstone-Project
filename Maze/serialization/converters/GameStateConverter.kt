package serialization.converters

import Common.GameState
import Common.Skip
import com.google.gson.Gson
import serialization.data.RefereeStateDTO

object GameStateConverter {

    fun getRefereeStateFromDTO(state: RefereeStateDTO, names: List<String>): GameState {
        val board = BoardConverter.getBoardFromBoardDTO(state.board)
        val spareTile = TileConverter.getTileFromDTO(state.spare)
        val players = state.plmt.mapIndexed { index, it ->
            RefereePlayerConverter.refereePlayerFromDTO(it, names[index])
        }
        val action = ActionConverter.getLastMovingAction(state.last)
        return GameState(board, spareTile, players, action)
    }

    fun serializeGameState(gameState: GameState): RefereeStateDTO {
        val publicGameState = gameState.toPublicState()
        val board = gameState.getBoard()
        val serializedBoard = BoardConverter.serializeBoard(board)

        val spareTile = publicGameState.spareTile
        val serializedTile = TileConverter.serializeTile(spareTile)

        val players = gameState.getPlayersData()
        val serializedPlayers = players.map { (_, player) ->
            RefereePlayerConverter.serializeRefereePlayer(player)
        }

        val serializedAction = ActionConverter.serializeChoice(publicGameState.lastAction ?: Skip, Gson())


        return RefereeStateDTO(
            serializedBoard, serializedTile, serializedPlayers, listOf() // TODO
        )
    }
}