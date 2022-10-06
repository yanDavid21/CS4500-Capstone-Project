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
            throw IllegalArgumentException("Row Position $rowPosition is not slideable.")
        }

        val row = tiles[rowPosition.value]

        when (direction) {
            HorizontalDirection.LEFT -> {
                this.dislodgedTile = row.removeAt(0)
                this.emptySlot = Coordinates(rowPosition, ColumnPosition(this.width - 1))
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
            throw IllegalArgumentException("Column Position $columnPosition is not slideable.")
        }

        val col = tiles[columnPosition.value]

        when (direction) {
            VerticalDirection.UP -> {
                this.dislodgedTile = col.removeAt(0)
                this.emptySlot = Coordinates(RowPosition(this.height - 1), columnPosition)
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
                this.emptySlot = null

                this.spareTile = dislodgedTile
                this.dislodgedTile = null

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
        stack.add(startingPosition)

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
        getPositionAdjacentTo(currentPosition, outgoingDirection)?.let { neighborPosition ->
            val neighbor = getTile(neighborPosition)
            if (neighbor.canBeReachedFrom(outgoingDirection)) {
                stack.add(neighborPosition)
            }
            visitedNodes.add(neighbor)
        }
    }

    private fun isSlideable(position: Position): Boolean {
        return position.value % 2 == 0
    }

    private fun setTile(position: Coordinates, tile: Tile) {
        val row = tiles[position.row.value]
        row.add(position.col.value, tile)
    }

    private fun getTile(position: Coordinates): Tile {
        return tiles[position.row.value][position.col.value]
    }

    private fun getPositionAdjacentTo(position: Coordinates, outgoingDirection: Direction): Coordinates? {
        val rowValue = position.row.value
        val colValue = position.col.value

        return if (outgoingDirection == VerticalDirection.UP && rowValue > 0) {
            position.copyWithNewRow(rowValue - 1)
        } else if (outgoingDirection == VerticalDirection.DOWN && rowValue < this.height - 1) {
            position.copyWithNewRow(rowValue + 1)
        } else if (outgoingDirection == HorizontalDirection.LEFT && colValue > 0) {
            position.copyWithNewCol(colValue - 1)
        } else if (outgoingDirection == HorizontalDirection.RIGHT && colValue < this.width - 1) {
            position.copyWithNewCol(colValue + 1)
        } else {
            null
        }
    }
}