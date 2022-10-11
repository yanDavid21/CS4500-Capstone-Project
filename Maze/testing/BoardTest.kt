package testing

import Common.board.Referee
import Common.board.ColumnPosition
import Common.board.Coordinates
import Common.board.RowPosition
import Common.tile.*
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import java.io.InputStreamReader
import java.io.Reader
import java.io.Writer


data class MazeInitData(
   val board: TileMatrix,
   //val coordinates: Coordinates
)


data class TileMatrix(
   val connectors: List<List<String>>,
   val treasures: List<List<List<String>>>
)


fun main() {
    val jsonReader = JsonReader(InputStreamReader(System.`in`))
    val gson = Gson()
    val testBoard = gson.fromJson<TileMatrix>(jsonReader, TileMatrix::class.java)
    println("found board")
    val coordinates = gson.fromJson<List<Int>>(jsonReader, List::class.java)

    val tiles = testBoard.connectors.mapIndexed { rowIndex, row ->
        row.mapIndexed { index, tile ->
            getTileFromStringAndTreasure(tile, testBoard.treasures[rowIndex][index])
        }.toTypedArray()
    }.toTypedArray()

    val board = Referee(tiles, EmptyTile()) // TODO: will not need spare tile after refactoring

    val reachableTiles = board.getReachableTiles(Coordinates(RowPosition(coordinates[0]), ColumnPosition(coordinates[1])))

    println(reachableTiles)

}

fun getTileFromStringAndTreasure(string: String, treasure: List<String>): Tile {
    val gems = Pair(Gem(treasure[0]), Gem(treasure[1]))
    return when(string) {
        "│" -> GameTile(Path.VERTICAL, Degree.ZERO, gems)
        "─" -> GameTile(Path.VERTICAL, Degree.NINETY, gems)
        "┐" -> GameTile(Path.UP_RIGHT, Degree.ONE_EIGHTY, gems)
        "└" -> GameTile(Path.UP_RIGHT, Degree.ZERO, gems)
        "┌" -> GameTile(Path.UP_RIGHT, Degree.TWO_SEVENTY, gems)
        "┘" -> GameTile(Path.UP_RIGHT, Degree.NINETY, gems)
        "┬" -> GameTile(Path.T, Degree.ZERO, gems)
        "├" -> GameTile(Path.T, Degree.NINETY, gems)
        "┴" -> GameTile(Path.T, Degree.ONE_EIGHTY, gems)
        "┤" -> GameTile(Path.T, Degree.TWO_SEVENTY, gems)
        "┼" -> GameTile(Path.CROSS, Degree.ZERO, gems)
        else -> throw IllegalArgumentException("$string is not a valid connector")
    }
}



fun decodeJSONFromStream(reader: Reader, writer: Writer) {
   val jsonReader = JsonReader(reader)
   return try {
      while (jsonReader.hasNext()) {
         val board: TileMatrix? = readBoard(jsonReader)
          board?.let {
            writer.write(board.toString())
            writer.flush()
         }
      }
   } finally {
      reader.close()
   }
}

fun cardinalCharacterToString(cardinalChar: CardinalCharacters):String {
   return when(cardinalChar) {
      CardinalCharacters(Horizontal.LEFT, Vertical.UP) -> "┘"
      CardinalCharacters(Horizontal.RIGHT, Vertical.UP) -> "└"
      CardinalCharacters(Horizontal.LEFT, Vertical.DOWN) -> "┐"
      CardinalCharacters(Horizontal.RIGHT, Vertical.DOWN) -> "┌"
      else -> throw IllegalStateException("Can not reach this branch.")
   }
}

data class CardinalCharacters(
   val horizontal: Horizontal,
   val vertical: Vertical,
)

enum class Horizontal {
   LEFT, RIGHT
}

enum class Vertical {
   UP, DOWN
}

fun readBoard(jsonReader: JsonReader):TileMatrix? {
    var connectors: List<List<String>>? = null
    var treasures: List<List<List<String>>>? = null

    jsonReader.beginObject()
    while (jsonReader.hasNext()) {
        when (jsonReader.nextName()) {
            "connectors" -> {
                connectors = readRows(jsonReader)
            }
            "treasures" -> {
                treasures = readRowsTreasure(jsonReader)
            }
            else -> {
                jsonReader.skipValue()
            }
        }
    }

    jsonReader.endObject()
    return connectors?.let {
        treasures?.let {
            TileMatrix(connectors, treasures)
        }
    }

}


fun readRows(jsonReader: JsonReader): List<List<String>> {
    val rows: MutableList<List<String>> = mutableListOf()
    jsonReader.beginArray()
    while(jsonReader.hasNext()) {
        rows.add(readColumns(jsonReader))
    }
    jsonReader.endArray()
    return rows.toList()
}

fun readColumns(jsonReader: JsonReader): List<String> {
    val cols: MutableList<String> = mutableListOf()
    jsonReader.beginArray()
    while(jsonReader.hasNext()) {
        cols.add(jsonReader.nextString())
    }
    jsonReader.endArray()
    return cols
}


fun readRowsTreasure(jsonReader: JsonReader): List<List<List<String>>> {
    val rows: MutableList<List<List<String>>> = mutableListOf()
    jsonReader.beginArray()
    while(jsonReader.hasNext()) {
        rows.add(readColumnsTreasure(jsonReader))
    }
    jsonReader.endArray()
    return rows.toList()
}

fun readColumnsTreasure(jsonReader: JsonReader): List<List<String>> {
    val cols: MutableList<List<String>> = mutableListOf()
    jsonReader.beginArray()
    while(jsonReader.hasNext()) {
        cols.add(readTreasurePair(jsonReader))
    }
    jsonReader.endArray()
    return cols
}

fun readTreasurePair(jsonReader: JsonReader): List<String> {
    val pair: MutableList<String> =  mutableListOf()
    jsonReader.beginArray()
    while (jsonReader.hasNext()) {
        pair.add(jsonReader.nextString())
    }
    jsonReader.endArray()
    return pair.toList()
}