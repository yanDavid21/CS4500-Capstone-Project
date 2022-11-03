package serialization.converters

import Common.PublicGameState
import serialization.data.StateDTO

object PublicGameStateConverter {

    fun serializeGameState(publicGameState: PublicGameState): StateDTO {
        return StateDTO(
            BoardConverter.serializeBoard(publicGameState.board),
            TileConverter.serializeTile(publicGameState.spareTile),
            publicGameState.publicPlayerData.map { (_, player) ->
                PlayerConverter.serializePublicPlayer(player)
            },
            ActionConverter.serializeAction(publicGameState.lastAction)
        )
    }
}