package serialization.converters

import Common.board.Coordinates
import serialization.data.CoordinateDTO

object CoordinateConverter {

    fun coordinateFromDTO(coordinateDTO: CoordinateDTO): Coordinates {
        return Coordinates.fromRowAndValue(coordinateDTO.`row#`, coordinateDTO.`column#`)
    }
}