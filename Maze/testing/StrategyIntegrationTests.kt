package testing

import Common.ColumnAction
import Common.PlayerState
import Common.RowAction
import Common.Skip
import Common.board.Board
import Common.board.Coordinates
import Common.player.Color
import Common.player.Player
import Players.Euclid
import Players.MazeStrategy
import Players.Riemann
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import java.io.InputStreamReader
import java.util.*

fun main() {
    val jsonReader = JsonReader(InputStreamReader(System.`in`, "UTF-8"))
    val gson = Gson()

    val strategyDesignation = gson.fromJson<StrategyDesignation>(jsonReader, StrategyDesignation::class.java)
    val state = gson.fromJson<State>(jsonReader, State::class.java)
    val target = gson.fromJson<TestCoordinate>(jsonReader, TestCoordinate::class.java)

    val currentBoard = Board(TestUtils.getTilesFromConnectorsAndTreasures(state.board.connectors,
        TestUtils.getTreasuresFromStrings(state.board.treasures)))
    val spare = TestUtils.getTileFromStringAndTreasure(
        state.spare.tilekey,
        TestUtils.getTreasureFromString(state.spare.image1, state.spare.image2)
    )
    val lastAction = TestUtils.getLastMovingAction(state.last)

    val currentPlayer = state.plmt[0].let {
        val id = UUID.randomUUID()
        val playerCoord = Coordinates.fromRowAndValue(it.current.`row#`, it.current.`column#`)
        val homeCoord = Coordinates.fromRowAndValue(it.home.`row#`, it.home.`column#`)
        Player(id, playerCoord, target.toCoordinate(), homeCoord, Color.valueOf(it.color))
    }

    val strategy = strategyDesignation.getStrategy(currentPlayer)

    val choice = strategy.decideMove(PlayerState(currentBoard, spare, lastAction))

    val output: Any = when(choice) {
        Skip -> "PASS"
        is ColumnAction -> listOf<Any>(
            choice.columnPosition.value,
            choice.direction,
            choice.rotation.value,
            TestCoordinate.fromCoordinates(choice.newPosition)
        )
        is RowAction -> listOf<Any>(
            choice.rowPosition.value,
            choice.direction,
            choice.rotation.value,
            TestCoordinate.fromCoordinates(choice.newPosition)
        )
        else -> throw IllegalArgumentException("Invalid choice")
    }

    println(gson.toJson(output).toString())
}

enum class StrategyDesignation {
    Riemann, Euclid;

    fun getStrategy(player: Player): MazeStrategy {
        return when(this) {
            Riemann -> Riemann(player)
            Euclid -> Euclid(player)
        }
    }
}
