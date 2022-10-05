package Common.board

import Common.*
import Common.board.tile.Degree
import java.util.*
import kotlin.collections.HashSet

class Board(
    private val tiles: MutableList<MutableList<Tile>>, private var spareTile: Tile
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
        dislodgedTile?.let { dislodgedTile ->
            emptySlot?.let { emptySlot ->
                this.setTile(emptySlot, spareTile)
                this.spareTile = dislodgedTile

                this.dislodgedTile = null
                this.emptySlot = null
            }
        } ?: throw IllegalStateException("Empty slot and dislodged tile both need to be non-null.")

    }

    override fun rotateSpareTile(degree: Degree) {
        this.spareTile.rotate(degree)
    }

    /**
     * Performs a depth-first search of all reachable tiles starting from _position_, neighbors are determined
     * by whether two adjacent tile's have connecting shapes.
     */
    override fun getReachableTiles(startingPosition: Coordinates): Set<Tile> {
        val startingTile = getTile(startingPosition)
        val stack = Stack<Coordinates>()
        val visitedNodes = HashSet<Tile>()
        visitedNodes.add(startingTile)

        while (stack.isNotEmpty()) {
            val currentPosition = stack.pop()
            val currentTile = getTile(currentPosition)
            if (!visitedNodes.contains(currentTile)) {
                currentTile.getOutgoingDirections().forEach { outgoingDirection ->
                    addReachableNeighborToPath(currentPosition, outgoingDirection, stack, visitedNodes)
                }
            }
        }
        visitedNodes.remove(startingTile)
        return visitedNodes
    }

    private fun addReachableNeighborToPath(currentPosition: Coordinates, outgoingDirection: Direction,
                                           stack: Stack<Coordinates>, visitedNodes: MutableSet<Tile>) {
        val neighborPosition = getPositionAdjacentTo(currentPosition, outgoingDirection)
        neighborPosition?.let { neighborPosition ->
            val neighbor = getTile(neighborPosition)
            if (neighbor.canBeReachedFrom(outgoingDirection)) {
                stack.add(neighborPosition)
            }
            visitedNodes.add(neighbor)
        }

    }

    private fun isSlideable(position: Position): Boolean {
        return position.getValue() % 2 == 1
    }

    private fun setTile(position: Coordinates, tile: Tile) {
        val row = tiles[position.row.getValue()]
        row[position.col.getValue()] = tile
    }

    private fun getTile(position: Coordinates): Tile {
        return tiles[position.row.getValue()][position.col.getValue()]
    }

    private fun getPositionAdjacentTo(position: Coordinates, outgoingDirection: Direction): Coordinates? {
        val rowValue = position.row.getValue()
        val colValue = position.col.getValue()

        return when (outgoingDirection) {
            VerticalDirection.UP-> if (rowValue > 0) position.copyWithNewRow(rowValue - 1)  else null
            VerticalDirection.DOWN -> if (rowValue < this.height) position.copyWithNewRow(rowValue + 1) else null
            HorizontalDirection.LEFT->
                if (colValue > 0) position.copyWithNewCol(colValue - 1) else null
            HorizontalDirection.RIGHT->
                if (colValue < this.width) position.copyWithNewCol(colValue + 1) else null
            else -> {
                throw IllegalStateException("Invalid Direction given, must be one of: UP, DOWN, LEFT, RIGHT")
            }
        }
    }




}