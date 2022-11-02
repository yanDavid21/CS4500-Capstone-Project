package serialization.converters

import Common.board.Coordinates
import Common.player.Color
import Common.player.PlayerData
import Common.tile.treasure.Gem
import Common.tile.treasure.Treasure
import serialization.data.PlayerDTO
import java.util.*

object PlayerConverter {
    fun playerFromDto(playerDTO: PlayerDTO): PlayerData {
        val id = UUID.randomUUID().toString()
        val goal = Treasure(Gem.GROSSULAR_GARNET, Gem.HACKMANITE) // random treasure
        val currentPosition = CoordinateConverter.coordinateFromDTO(playerDTO.current)
        val homePosition = CoordinateConverter.coordinateFromDTO(playerDTO.home)
        return PlayerData(
            id,
            currentPosition,
            Coordinates.fromRowAndValue(0,0),
            homePosition,
            Color.valueOf(playerDTO.color)
        )
    }
}