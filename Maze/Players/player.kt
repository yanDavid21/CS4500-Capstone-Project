package Players

import Common.Action
import Common.PublicGameState
import Common.board.Coordinates
import Common.player.Player
import Common.tile.Degree
import Common.tile.GameTile
import Common.tile.Path
import Common.tile.treasure.Gem
import Common.tile.treasure.Treasure
import java.util.*


/**
 * Abstraction layer class denoting a player's API, to be used to represent a player connecting to the game and the
 * API expected from it. RandomSeed is used for testing.
 */
interface PlayerMechanism {
    val name: String

    fun proposeBoard0(rows: Int, columns: Int): Array<Array<GameTile>>

    fun setupAndUpdateGoal(state: PublicGameState?, goal: Coordinates)

    fun takeTurn(state: PublicGameState): Action

    fun won(hasPlayerWon: Boolean)
}

class PlayerMechanismImpl(val name: String, randomSeed: Long = 0L) {
    private val randomObj: Random = Random(randomSeed)
    private var hasWon: Boolean = false
    private var hasFoundTreasure: Boolean = false
    private lateinit var nextGoal: Coordinates
    private lateinit var playerData: Player

    fun name(): String {
        return name
    }

    /**
     * Produces a board given the dimensions.
     */
    fun proposeBoard0(rows: Int, columns: Int): Array<Array<GameTile>> {
        checkValidBoardDimensions(rows, columns)
        val possibleTreasure = mutableSetOf<Treasure>()

        val tiles = Array(rows) {
            Array(columns) {
                GameTile(Path.UP_RIGHT, Degree.ZERO, Treasure(Gem.GROSSULAR_GARNET, Gem.GOLDSTONE))
                //  GameTile(getRandomPath(), getRandomDegree(), getRandomTreasure(possibleTreasure, rows * columns))
            }
        }
        return tiles
    }


    // the player is handed the inital state, which is visible to all
    // plus a (private) goal that it must visit next
    //
    // if state0 is NONE, setup is used to tell the player go-home
    // and goal is just a reminder where home is.
    fun setupAndUpdateGoal(state: PublicGameState?, goal: Coordinates) {
        if (state != null) {
            val (name, currentPosition, homePosition, color) = state.getPlayerData(name)
            playerData = Player(name, currentPosition, goal, homePosition, color, hasFoundTreasure)
        }
        nextGoal = goal
        hasFoundTreasure = true
    }


    // after receiving the state, a player passes on taking an action
    // or picks
    // -- a row or column index and a direction,
    // -- a degree of rotation for the spare,
    // -- a new place to move to.
    fun takeTurn(state: PublicGameState): Action {
        val (name, currentPosition, homePosition, color) = state.getPlayerData(name)
        playerData = Player(name, currentPosition, this.playerData.goalPosition, homePosition, color, this.hasFoundTreasure)
        return Riemann(playerData).decideMove(state)
    }

    fun won(hasPlayerWon: Boolean) {
        hasWon = hasPlayerWon
    }

    // throws IllegalArgumentException (referee handles)
    private fun checkValidBoardDimensions(rows: Int, columns: Int) {
        if (rows <= 0 || columns <= 0 ) {
            throw IllegalArgumentException("Board dimensions must be a natural number received values: $rows, $columns")
        }
    }

    private fun getRandomDegree(): Degree {
        val randomIndex = this.randomObj.nextInt()
        return Degree.values()[randomIndex]
    }

    private fun getRandomPath(): Path {
        val randomIndex = this.randomObj.nextInt()
        return Path.values()[randomIndex]
    }

    private fun getRandomTreasure(setOfTreasures: MutableSet<Treasure>, gemsNeeded: Int): Set<Treasure> {
        val gem1 = Gem.values()[0]
        return Array(gemsNeeded) { index -> Treasure(gem1, Gem.values()[index])}.toSet()
    }
}