package Common.board

import Common.*

class Board(
    private val tiles: MutableList<MutableList<Tile>>
): PlayableBoard {


    override fun slide(rowPosition: RowPosition, direction: HorizontalDirection) {
        TODO("Not yet implemented")
    }

    override fun slide(columnPosition: ColumnPosition, direction: VerticalDirection) {
        TODO("Not yet implemented")
    }

    override fun insertSpareTile() {
        TODO("Not yet implemented")
    }

    override fun rotateSpareTile(degree: Degree) {
        TODO("Not yet implemented")
    }

    override fun getReachableTiles(rowPosition: RowPosition, columnPosition: ColumnPosition): Set<Tile> {
        TODO("Not yet implemented")
    }



}