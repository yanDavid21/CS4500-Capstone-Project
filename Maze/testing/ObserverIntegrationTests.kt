package testing

import Common.Action
import Common.GameState
import Common.PublicGameState
import Common.board.Board
import Common.board.Coordinates
import Common.player.Color
import Common.player.PlayerData
import Common.tile.GameTile
import Players.PlayerMechanism
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import serialization.converters.ActionConverter
import serialization.converters.TileConverter
import serialization.converters.TreasureConverter
import serialization.data.RefereeStateDTO
import java.io.InputStreamReader


fun main() {
    val jsonReader = JsonReader(InputStreamReader(System.`in`, "UTF-8"))
    val gson = Gson()

    val playerSpec = gson.fromJson<List<List<String>>>(jsonReader, List::class.java)
    val refereeState = gson.fromJson<RefereeStateDTO>(jsonReader, RefereeStateDTO::class.java)

    val state = getRefereeState(refereeState, playerSpec.map { it[0] })
    val playerMechanisms = getPlayerMechanisms(playerSpec, state)
    val referee = TestableReferee()

    val endgameData = referee.playGame(state, playerMechanisms)

    val winners = endgameData.filter { it.value }.keys.sorted()

    println(gson.toJson(winners))
}

fun getRefereeState(state: RefereeStateDTO, names: List<String>): GameState {
    val board = Board(
        TileConverter.getTilesFromConnectorsAndTreasures(state.board.connectors,
            TreasureConverter.getTreasuresFromStrings(state.board.treasures))
    )
    val spareTile = TileConverter.getTileFromStringAndTreasure(
        state.spare.tilekey,
        TreasureConverter.getTreasureFromString(state.spare.image1, state.spare.image2)
    )
    val players = state.plmt.mapIndexed { index, it ->
        PlayerData(
            id = names[index],
            currentPosition = it.current.toCoordinate(),
            homePosition = it.home.toCoordinate(),
            goalPosition = it.goto.toCoordinate(),
            treasureFound = it.goto == it.home,
            color = Color.valueOf(it.color)
        )
    }
    val action = ActionConverter.getLastMovingAction(state.last)
    return GameState(board, spareTile, players, action)
}

fun getPlayerMechanisms(specs: List<List<String>>, state: GameState): List<PlayerMechanism> {
    val playersData = state.getPlayersData()
    return specs.map {
        val name = it[0]
        val strategy = StrategyDesignation.valueOf(it[1])
        object: PlayerMechanism {
            private lateinit var goal: Coordinates
            private val playerData = playersData[name] ?: throw IllegalStateException("Player mismatch. Could not find $name")
            override val name: String = name

            override fun proposeBoard0(rows: Int, columns: Int): Array<Array<GameTile>> {
                throw IllegalStateException("Player should not be asked to propose board yet.")
            }

            override fun setupAndUpdateGoal(state: PublicGameState?, goal: Coordinates) {
                this.goal = goal
            }

            override fun takeTurn(state: PublicGameState): Action {
                val newPublicPlayerData = state.getPlayerData(name)
                return strategy.getStrategy(playerData.copy(currentPosition = newPublicPlayerData.currentPosition)).decideMove(state)
            }

            override fun won(hasPlayerWon: Boolean) {
                return
            }
        }
    }
}

