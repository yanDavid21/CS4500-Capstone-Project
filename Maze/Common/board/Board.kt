package Common.board

import Common.*
import Common.board.tile.Degree

class Board(
    private val tiles: MutableList<MutableList<Tile>>, private val spareTile: Tile
): PlayableBoard {

    private var dislodgedTile: Tile?
    private var emptySlot: Coordinates?
    private val height: Int
    private val width: Int

    init {
        this.dislodgedTile = null
        this.emptySlot = null
        this.height = tiles.size
        this.width = tiles.size
    }

    // TODO: abstract slide
    override fun slide(rowPosition: RowPosition, direction: HorizontalDirection) {
        if (!isSlideable(rowPosition)) {
            throw IllegalArgumentException("Row Position ${rowPosition.x} is not slideable.")
        }

        val row = tiles[rowPosition.getValue()]

        when (direction) {
            HorizontalDirection.LEFT -> {
                this.dislodgedTile = row.removeAt(0)
                this.emptySlot = Coordinates(rowPosition, ColumnPosition(this.width))
            }
            HorizontalDirection.RIGHT -> {
                this.dislodgedTile = row.removeAt(width - 1)
                this.emptySlot = Coordinates(rowPosition, ColumnPosition(0))
            }
        }
    }

    //TODO: abstract out
    override fun slide(columnPosition: ColumnPosition, direction: VerticalDirection) {
        if (!isSlideable(columnPosition)) {
            throw IllegalArgumentException("Column Position ${columnPosition.y} is not slideable.")
        }

        val col = tiles[columnPosition.getValue()]

        when (direction) {
            VerticalDirection.UP -> {
                this.dislodgedTile = col.removeAt(0)
                this.emptySlot = Coordinates(RowPosition(this.height), columnPosition)
            }
            VerticalDirection.DOWN -> {
                this.dislodgedTile = col.removeAt(height - 1)
                this.emptySlot = Coordinates(RowPosition(0), columnPosition)
            }
        }
    }

    override fun insertSpareTile() {
        if (dislodgedTile != null || emptySlot != null) {
            throw IllegalStateException("Empty slot and dislodged tile both need to be non-null.")
        }

        tiles[emptySlot.row.x]
    }

    override fun rotateSpareTile(degree: Degree) {

        // TODO("Not yet implemented")
    }

    override fun getReachableTiles(rowPosition: RowPosition, columnPosition: ColumnPosition): Set<Tile> {
        // TODO("Not yet implemented")
    }

    private fun isSlideable(position: Position): Boolean {
        return position.getValue() % 2 == 1
    }




}