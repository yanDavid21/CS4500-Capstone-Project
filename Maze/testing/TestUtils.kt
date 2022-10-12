package testing

import Common.tile.Degree
import Common.tile.GameTile
import Common.tile.Path
import Common.tile.treasure.Treasure

object TestUtils {

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
}