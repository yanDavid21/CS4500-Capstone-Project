package testing

import Common.ColumnAction
import Common.MovingAction
import Common.RowAction
import Common.board.Board
import Common.board.ColumnPosition
import Common.board.Coordinates
import Common.board.RowPosition
import Common.tile.*
import Common.tile.treasure.Gem
import Common.tile.treasure.Treasure

object TestUtils {

    fun getTreasuresFromStrings(treasures: List<List<List<String>>>): List<List<Treasure>> {
        return treasures.map { it.map { pair -> getTreasureFromString(pair[0], pair[1]) } }
    }

    fun getTreasureFromString(image1: String, image2: String): Treasure {
        val gem1 = Gem.valueOf(image1.toUpperCase().replace("-","_"))
        val gem2 = Gem.valueOf(image2.toUpperCase().replace("-","_"))
        return Treasure(gem1, gem2)
    }

    fun getTilesFromConnectorsAndTreasures(connectors: List<List<String>>,
                                           treasures: List<List<Treasure>>): Array<Array<GameTile>> {
        return connectors.mapIndexed { rowIndex, row ->
            row.mapIndexed { index, tile ->
                getTileFromStringAndTreasure(tile, treasures[rowIndex][index])
            }.toTypedArray()
        }.toTypedArray()
    }

    fun getTileFromStringAndTreasure(string: String, treasure: Treasure): GameTile {
        return when(string) {
            "│" -> GameTile(Path.VERTICAL, Degree.ZERO, treasure)
            "─" -> GameTile(Path.VERTICAL, Degree.NINETY, treasure)
            "┐" -> GameTile(Path.UP_RIGHT, Degree.ONE_EIGHTY, treasure)
            "└" -> GameTile(Path.UP_RIGHT, Degree.ZERO, treasure)
            "┌" -> GameTile(Path.UP_RIGHT, Degree.TWO_SEVENTY, treasure)
            "┘" -> GameTile(Path.UP_RIGHT, Degree.NINETY, treasure)
            "┬" -> GameTile(Path.T, Degree.ZERO, treasure)
            "├" -> GameTile(Path.T, Degree.NINETY, treasure)
            "┴" -> GameTile(Path.T, Degree.ONE_EIGHTY, treasure)
            "┤" -> GameTile(Path.T, Degree.TWO_SEVENTY, treasure)
            "┼" -> GameTile(Path.CROSS, Degree.ZERO, treasure)
            else -> throw IllegalArgumentException("$string is not a valid connector")
        }
    }

    fun getTilesInRow(rowIndex: Int, board: Board): Array<GameTile> {
        return (0 .. 6)
            .map { board.getTile(Coordinates.fromRowAndValue(rowIndex, it)) }.toTypedArray()
    }

    fun getTilesInCol(colIndex: Int, board: Board): Array<GameTile> {
        return (0 .. 6)
            .map { board.getTile(Coordinates.fromRowAndValue(it, colIndex)) }.toTypedArray()
    }

    fun getLastMovingAction(data: List<String>?): MovingAction? {
        return if (data == null) {
            null
        } else {
            val index = data[0].toInt()
            val direction = DirectionTest.valueOf(data[1])

            val zero = Degree.ZERO
            val zerozero = Coordinates.fromRowAndValue(0, 0)
            when (direction) {
                DirectionTest.LEFT-> RowAction(RowPosition(index), HorizontalDirection.LEFT, zero, zerozero)
                DirectionTest.RIGHT -> RowAction(RowPosition(index), HorizontalDirection.RIGHT, zero, zerozero)
                DirectionTest.UP -> ColumnAction(ColumnPosition(index), VerticalDirection.UP, zero, zerozero)
                DirectionTest.DOWN -> ColumnAction(ColumnPosition(index), VerticalDirection.DOWN, zero, zerozero)
            }
        }
    }
}