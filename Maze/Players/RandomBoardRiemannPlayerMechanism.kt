package Players

import Common.Action
import Common.PublicGameState
import Common.board.Coordinates
import Common.player.PlayerData
import Common.tile.Degree
import Common.tile.GameTile
import Common.tile.Path
import Common.tile.treasure.Gem
import Common.tile.treasure.Treasure
import java.util.*

/**
 * Should be used to test referee with a local game.
 * An implementation of PlayerMechanism that generates a random board when prompted using the strategy given.
 * randomSeed to be used for testing.
 */
class RandomBoardRiemannPlayerMechanism(override val name: String, randomSeed: Long = 0L): PlayerMechanism {
    private val randomObj: Random = Random(randomSeed)
    var hasWon: Boolean = false
    private var hasFoundTreasure: Boolean = false
    lateinit var nextGoal: Coordinates

    /**
     * Produces a random board given the dimensions.
     */
    override fun proposeBoard0(rows: Int, columns: Int): Array<Array<GameTile>> {
        checkValidBoardDimensions(rows, columns)
        val setOfPossibleTreasure =  getRandomTreasures(rows * columns)

        val tiles = Array(rows) {
            Array(columns) {
                GameTile(
                    getRandomPath(),
                    getRandomDegree(),
                    setOfPossibleTreasure.first().also { setOfPossibleTreasure.remove(it) })
            }
        }
        return tiles
    }


    /**
     * If state is non-null, the player is handed the inital state, which is visible to all
     * plus a (private) goal that it must visit next (the treasure)
     *
     * If state is null, the player is told to go home and a reminder of where home is.
     */
    override fun setupAndUpdateGoal(state: PublicGameState?, goal: Coordinates) {
        if (state == null) {
            this.hasFoundTreasure = true
        }
        this.nextGoal = goal
    }


    /**
     * Generates a turn (PASS or MOVE) based on the strategy of this player mechanism.
     */
    override fun takeTurn(state: PublicGameState): Action {
        val (name, currentPosition, homePosition, color) = state.getPlayerData(name)
        val playerData = PlayerData(name, currentPosition, nextGoal, homePosition, color, hasFoundTreasure)
        return Riemann(playerData).decideMove(state)
    }

    /**
     * Notify this player mechanism if they have won.
     */
    override fun won(hasPlayerWon: Boolean) {
        hasWon = hasPlayerWon
    }

    /**
     * Throw exception if board dimensions are invalid.
     */
    private fun checkValidBoardDimensions(rows: Int, columns: Int) {
        if (rows <= 0 || columns <= 0 ) {
            throw IllegalArgumentException("Board dimensions must be a natural number received values: $rows, $columns")
        }
    }

    /**
     * Get a random Degree based on this player mechanism's random object.
     */
    private fun getRandomDegree(): Degree {
        val randomIndex = this.randomObj.nextInt()
        return Degree.values()[randomIndex]
    }

    /**
     * Get a random Path based on this player mechanism's random object.
     */
    private fun getRandomPath(): Path {
        val randomIndex = this.randomObj.nextInt()
        return Path.values()[randomIndex]
    }

    /**
     * Get a random set of Treasures based on this player mechanism's random object.
     */
    private fun getRandomTreasures(gemsNeeded: Int): MutableSet<Treasure> {
        val gem1 = Gem.values()[0]
        return Array(gemsNeeded) { index -> Treasure(gem1, Gem.values()[index]) }.toMutableSet()
    }
}