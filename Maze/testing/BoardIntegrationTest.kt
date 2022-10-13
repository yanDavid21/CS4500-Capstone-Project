package testing

import Common.board.Board
import Common.board.ColumnPosition
import Common.board.Coordinates
import Common.board.RowPosition
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import java.io.InputStreamReader

data class TileMatrix(
   val connectors: List<List<String>>,
   val treasures: List<List<List<String>>>
)

data class TestCoordinate(
    val `row#` : Int, val `col#`: Int
) {
    companion object {
        fun fromCoordinates(coordinates: Coordinates): TestCoordinate {
            return TestCoordinate(coordinates.row.value, coordinates.col.value)
        }
    }
}

fun main() {
    val jsonReader = JsonReader(InputStreamReader(System.`in`))
    val gson = Gson()

    val testBoard = gson.fromJson<TileMatrix>(jsonReader, TileMatrix::class.java)
    val coordinates = gson.fromJson<TestCoordinate>(jsonReader, TestCoordinate::class.java)

    val treasures = TestUtils.getTreasuresFromStrings(testBoard.treasures)

    val tiles = TestUtils.getTilesFromConnectorsAndTreasures(testBoard.connectors, treasures)

    val board = Board(tiles)

    val reachableTiles = board.getReachableTiles(Coordinates(RowPosition(coordinates.`row#`),
        ColumnPosition(coordinates.`col#`)))

    val reachablePosition = reachableTiles.map { TestCoordinate.fromCoordinates(board.getTilePosition(it)!!) }
    val comp = compareBy<TestCoordinate>({ it.`row#`}, {it.`col#`})

    println(reachablePosition.sortedWith(comp).map { gson.toJson(it, TestCoordinate::class.java) })
}