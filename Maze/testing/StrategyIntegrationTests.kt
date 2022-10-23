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
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.stream.JsonReader
import java.io.InputStreamReader
import java.util.*

fun main() {
    val jsonReader = JsonReader(InputStreamReader(System.`in`, "UTF-8"))
    val gson = Gson()

    val strategyDesignation = gson.fromJson<StrategyDesignation>(jsonReader, StrategyDesignation::class.java)
    val state = gson.fromJson<State>(jsonReader, State::class.java)
    val target = gson.fromJson<TestCoordinate>(jsonReader, TestCoordinate::class.java)

    val playerState = getPlayerState(state)
    val currentPlayer = getCurrentPlayer(state.plmt[0], target.toCoordinate())
    val strategy = strategyDesignation.getStrategy(currentPlayer)

    val choice = strategy.decideMove(playerState)

    val output = serializeChoice(choice, gson)

    println(output)
}

fun getPlayerState(state: State): PlayerState {
    val currentBoard = Board(TestUtils.getTilesFromConnectorsAndTreasures(state.board.connectors,
        TestUtils.getTreasuresFromStrings(state.board.treasures)))
    val spare = TestUtils.getTileFromStringAndTreasure(
        state.spare.tilekey,
        TestUtils.getTreasureFromString(state.spare.image1, state.spare.image2)
    )
    val lastAction = TestUtils.getLastMovingAction(state.last)

    return PlayerState(currentBoard, spare, lastAction)
}

fun getCurrentPlayer(playerData: PlayerTest, target: Coordinates): Player {
    val id = UUID.randomUUID()
    val playerCoord = Coordinates.fromRowAndValue(playerData.current.`row#`, playerData.current.`column#`)
    val homeCoord = Coordinates.fromRowAndValue(playerData.home.`row#`, playerData.home.`column#`)
    return Player(id, playerCoord, target, homeCoord, Color.valueOf(playerData.color))
}

fun serializeChoice(choice: Common.Action, gson: Gson): JsonElement {
    return when(choice) {
        Skip -> JsonPrimitive("PASS")
        is ColumnAction ->
            gson.toJsonTree(
                listOf(
                    choice.columnPosition.value,
                    choice.direction,
                    choice.rotation.value,
                    TestCoordinate.fromCoordinates(choice.newPosition)
                )
            )
        is RowAction -> gson.toJsonTree(
            listOf(
                choice.rowPosition.value,
                choice.direction,
                choice.rotation.value,
                TestCoordinate.fromCoordinates(choice.newPosition)
            )
        )
        else -> throw IllegalStateException("Invalid choice: $choice")
    }
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
