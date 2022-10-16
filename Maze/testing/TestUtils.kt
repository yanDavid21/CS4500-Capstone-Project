package testing

import Common.tile.Degree
import Common.tile.GameTile
import Common.tile.Path
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
            "│" -> GameTile(Path.VERTICAL, Degree.ZERO, treasure, setOf())
            "─" -> GameTile(Path.VERTICAL, Degree.NINETY, treasure, setOf())
            "┐" -> GameTile(Path.UP_RIGHT, Degree.ONE_EIGHTY, treasure, setOf())
            "└" -> GameTile(Path.UP_RIGHT, Degree.ZERO, treasure, setOf())
            "┌" -> GameTile(Path.UP_RIGHT, Degree.TWO_SEVENTY, treasure, setOf())
            "┘" -> GameTile(Path.UP_RIGHT, Degree.NINETY, treasure, setOf())
            "┬" -> GameTile(Path.T, Degree.ZERO, treasure, setOf())
            "├" -> GameTile(Path.T, Degree.NINETY, treasure, setOf())
            "┴" -> GameTile(Path.T, Degree.ONE_EIGHTY, treasure, setOf())
            "┤" -> GameTile(Path.T, Degree.TWO_SEVENTY, treasure, setOf())
            "┼" -> GameTile(Path.CROSS, Degree.ZERO, treasure, setOf())
            else -> throw IllegalArgumentException("$string is not a valid connector")
        }
    }
}