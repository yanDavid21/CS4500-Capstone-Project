package testing

import Common.GameState
import Common.board.Board
import Common.board.ColumnPosition
import Common.board.Coordinates
import Common.board.RowPosition
import Common.player.Color
import Common.player.Player
import Common.tile.Degree
import Common.tile.HorizontalDirection
import Common.tile.VerticalDirection
import Common.tile.treasure.Gem
import Common.tile.treasure.Treasure
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.stream.JsonReader
import java.io.InputStreamReader
import java.util.*

// state, index, direction, degree
fun main() {
    val jsonReader = JsonReader(InputStreamReader(System.`in`, "UTF-8"))
    val gson = Gson()

    val state = gson.fromJson<GameState>(jsonReader, GameState::class.java)
    val index = gson.fromJson<Int>(jsonReader, Int::class.java)
    val direction = gson.fromJson<DirectionTest>(jsonReader, DirectionTest::class.java)
    val degree = Degree.valueOf(gson.fromJson<Int>(jsonReader, Int::class.java))

    val treasures = TestUtils.getTreasuresFromStrings(state.board.treasures)
    val tiles = TestUtils.getTilesFromConnectorsAndTreasures(state.board.connectors, treasures)
    val board = Board(tiles)

    val spareTest = state.spare
    val spareTile= TestUtils.getTileFromStringAndTreasure(spareTest.tilekey,
        TestUtils.getTreasureFromString(spareTest.image1, spareTest.image2))

    val players = state.plmt.map {
        val id = UUID.randomUUID()
        val goal = Treasure(Gem.GROSSULAR_GARNET, Gem.HACKMANITE) // random treasure
        val playerCoord = Coordinates.fromRowAndValue(it.current.`row#`, it.current.`col#`)
        val homeTile = board.getTile(it.home.toCoordinate())
        val player = Player(id, playerCoord, goal, homeTile, Color.valueOf(it.color))

        player
    }
    val referee = GameState(board, spareTile, players)

    //val lastAction = state.last

    when(direction) {
        DirectionTest.LEFT -> referee.slideRowAndInsertSpare(RowPosition(index), HorizontalDirection.LEFT, degree)
        DirectionTest.RIGHT -> referee.slideRowAndInsertSpare(RowPosition(index), HorizontalDirection.RIGHT, degree)
        DirectionTest.UP -> referee.slideColumnAndInsertSpare(ColumnPosition(index), VerticalDirection.UP, degree)
        DirectionTest.DOWN -> referee.slideColumnAndInsertSpare(ColumnPosition(index), VerticalDirection.DOWN, degree)
    }

    val reachablePositions = referee.getBoard().getReachableTiles(players[0].currentPosition).map { TestCoordinate.fromCoordinates(it) }

    val comp = compareBy<TestCoordinate>({ it.`row#`}, {it.`col#`})

    println(reachablePositions.sortedWith(comp).map { gson.toJson(it, TestCoordinate::class.java) })
}

data class State(
    val board: BoardTest,
    val spare: TileTest,
    val plmt: List<PlayerTest>,
    val last: String?
)

enum class DirectionTest {
    LEFT, RIGHT, UP, DOWN
}

typealias Action = Pair<Int, String>?


data class BoardTest(
    val connectors: List<List<String>>,
    val treasures: List<List<List<String>>>
)

data class TileTest(
    val tilekey: String,

    @SerializedName("1-image")
    val image1: String,

    @SerializedName("2-image")
    val image2: String
)


data class PlayerTest(
    val current: TestCoordinate,
    val home: TestCoordinate,
    val color: String
)

