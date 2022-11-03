package Client.javafx

import Common.GameState
import Players.PlayerMechanism
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import serialization.converters.GameStateConverter
import serialization.data.RefereeStateDTO
import testing.getPlayerMechanisms
import java.io.InputStreamReader

/**
 * A RefereeObserverApplication that reads a game's intial state and player strategies from the command
 * line and executes a game.
 */
class CommandLineRefereeApp: RefereeObserverApplication() {
    override fun getStateAndPlayers(): Pair<GameState, List<PlayerMechanism>> {
        val jsonReader = JsonReader(InputStreamReader(System.`in`, "UTF-8"))
        val gson = Gson()

        val playerSpec = gson.fromJson<List<List<String>>>(jsonReader, List::class.java)
        val refereeState = gson.fromJson<RefereeStateDTO>(jsonReader, RefereeStateDTO::class.java)

        val state = GameStateConverter.getRefereeStateFromDTO(refereeState, playerSpec.map { it[0] })

        val mechanisms = getPlayerMechanisms(playerSpec, state)
        return Pair(state, mechanisms)
    }

}