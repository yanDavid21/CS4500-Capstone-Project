package Common.board

import Common.*
import Common.board.tile.Degree

class Board(
    tiles: Array<Array<Tile>>, private var spareTile: Tile
): PlayableBoard {

    private val tiles = BoardTiles(tiles, null)
    private var dislodgedTile: Tile? = null


    override fun slide(rowPosition: RowPosition, direction: HorizontalDirection) {
        this.dislodgedTile = tiles.slide(rowPosition,direction)
    }

    override fun slide(columnPosition: ColumnPosition, direction: VerticalDirection) {
        this.dislodgedTile = tiles.slide(columnPosition, direction)
    }

    override fun insertSpareTile() {
        dislodgedTile?.let { dislodgedTile ->
            this.tiles.insertTileIntoEmptySlot(spareTile)
            this.spareTile = dislodgedTile
            this.dislodgedTile = null
        } ?: throw IllegalStateException("Dislodged tile must be non-null to insert spare tile.")

    }

    override fun rotateSpareTile(degree: Degree) {
        this.spareTile.rotate(degree)
    }

    /**
     * Performs a depth-first search of all reachable tiles starting from _position_, neighbors are determined
     * by whether two adjacent tile's have connecting shapes.
     */
    override fun getReachableTiles(startingPosition: Coordinates): Set<Tile> {
        return tiles.getReachableTiles(startingPosition)
    }
}