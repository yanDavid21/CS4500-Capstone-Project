package serialization.converters

import Common.board.Board
import serialization.data.BoardDTO

object BoardConverter {

    fun getBoardFromBoardDTO(boardDTO: BoardDTO): Board {
        return Board(
            TileConverter.getTilesFromConnectorsAndTreasures(
                boardDTO.connectors,
                TreasureConverter.getTreasuresFromStrings(boardDTO.treasures)
            )
        )
    }
}