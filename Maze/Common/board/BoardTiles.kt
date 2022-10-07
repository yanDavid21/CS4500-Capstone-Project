package Common.board

import Common.tile.*
import java.util.*
import kotlin.collections.HashSet

class BoardTiles(private val tiles: Array<Array<Tile>>,
                 private var emptySlotPosition: Coordinates?) {

    private val height = tiles.size
    private val width = tiles[0].size


    fun insertTileIntoEmptySlot(tile: Tile) {
        emptySlotPosition?.let { emptySlotPosition ->
            this.setTile(emptySlotPosition, tile)
            this.emptySlotPosition = null
        } ?: throw IllegalStateException("Empty slot must be non-null to insert tile.")

    }

    fun slide(position: Position, direction: Direction): Tile {
        if (!isSlideable(position)) {
            throw IllegalArgumentException("Row/col ${position.value} is not slideable.")
        }

        val dislodgedTile = getDislodgedTile(position, direction)

        shiftByDirection(position,direction)

        // TODO: deal with player on tile
        val newEmptySlot = getEmptySlotPositionAfterSliding(position, direction)
        setTile(newEmptySlot, EmptyTile())
        this.emptySlotPosition = newEmptySlot

        return dislodgedTile
    }

    fun getReachableTiles(startingPosition: Coordinates): Set<Tile>  {
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
                visitedNodes.add(currentTile)
            }
        }
        visitedNodes.remove(startingTile)
        return visitedNodes
    }

    private fun getDislodgedTile(position: Position, direction: Direction): Tile {
        return when (direction) {
            HorizontalDirection.LEFT -> tiles[position.value][0]
            HorizontalDirection.RIGHT -> tiles[position.value][this.width - 1]
            VerticalDirection.UP -> tiles[0][position.value]
            VerticalDirection.DOWN -> tiles[this.height - 1][position.value]
            else -> throw java.lang.IllegalArgumentException("LEFT,RIGHT,UP,DOWN are the only possible directions.")
        }
    }

    private fun getEmptySlotPositionAfterSliding(position: Position, direction: Direction): Coordinates {
        return when(direction) {
            HorizontalDirection.LEFT -> Coordinates(RowPosition(position.value), ColumnPosition(width - 1))
            HorizontalDirection.RIGHT -> Coordinates(RowPosition(position.value), ColumnPosition(0))
            VerticalDirection.UP -> Coordinates(RowPosition(0), ColumnPosition(position.value))
            VerticalDirection.DOWN -> Coordinates(RowPosition(height - 1), ColumnPosition(0))
            else -> throw java.lang.IllegalArgumentException("LEFT,RIGHT,UP,DOWN are the only possible directions.")
        }
    }

    private fun shiftByDirection(position: Position, direction: Direction) {
        for (index in getIterationRange(direction)) {
            setTileFromNextTile(index, position, direction)
        }
    }

    private fun getIterationRange(direction: Direction): Iterable<Int> {
        return when(direction) {
            HorizontalDirection.RIGHT -> width - 1  downTo 1
            HorizontalDirection.LEFT -> 0 until width-1
            VerticalDirection.DOWN -> height-1 downTo   1
            VerticalDirection.UP -> 0 until height-1
            else -> throw IllegalArgumentException("YOU WILL NEVER REACH THIS")
        }
    }

    private fun getDifference(direction: Direction): Int {
        return when(direction) {
            HorizontalDirection.LEFT, VerticalDirection.DOWN -> 1
            HorizontalDirection.RIGHT, VerticalDirection.UP -> -1
            else -> throw IllegalArgumentException("")
        }
    }

    private fun setTileFromNextTile(index: Int,  position: Position, direction: Direction) {
        val difference = getDifference(direction)
        val currentTilePosition: Coordinates = when (direction) {
            is HorizontalDirection -> Coordinates(RowPosition(position.value), ColumnPosition(index))
            is VerticalDirection -> Coordinates(RowPosition(index), ColumnPosition(position.value))
            else -> throw IllegalArgumentException("Direction must be horizontal and vertical.")
        }
        val nextTilePosition: Coordinates = when (direction) {
            is HorizontalDirection -> Coordinates(RowPosition(position.value ), ColumnPosition(index + difference))
            is VerticalDirection -> Coordinates(RowPosition(index + difference), ColumnPosition(position.value))
            else -> throw IllegalArgumentException("Direction must be horizontal and vertical.")
        }
        setTile(currentTilePosition, getTile(nextTilePosition))
    }


    private fun addReachableNeighborToPath(currentPosition: Coordinates, outgoingDirection: Direction,
                                           stack: Stack<Coordinates>, visitedNodes: MutableSet<Tile>) {
        getPositionAdjacentTo(currentPosition, outgoingDirection)?.let { neighborPosition ->
            val neighbor = getTile(neighborPosition)
            if (neighbor.canBeReachedFrom(outgoingDirection)) {
                stack.add(neighborPosition)
            }
        }
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


    private fun setTile(position: Coordinates, tile: Tile) {
        val row = tiles[position.row.value]
        row[position.col.value] = tile
    }

    private fun getTile(position: Coordinates): Tile {
        return tiles[position.row.value][position.col.value]
    }

    private fun isSlideable(position: Position): Boolean {
        return position.value % 2 == 0
    }
}