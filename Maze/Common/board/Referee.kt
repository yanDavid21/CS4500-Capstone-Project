package Common.board

import Common.GameState
import Common.Player
import Common.tile.*
import java.util.*

class Referee(
    randomSeed: Long? = null,
    playerIDs: List<UUID>
): PlayableBoard {
    private var spareTile: Tile
    private val board = Board(tiles)
    private var dislodgedTile: Tile? = null
    private var nextAction: GameState = GameState.INITIAL
    private var currentPlayer: Player
    private val players: LinkedList<Player>
    private val randomObj = randomSeed?.let {Random(it)} ?: Random()

    init {
        /*
        1. Create 50 random tiles
        2. Select one to be the spareTile
        3. Make tiles out of the remaining 49,
        4. To create players, select n unmovable tiles, n other random tiles (for treasures)
            make players with them
         */
        val gems = Gem.values().toMutableList()
        gems.shuffle()
        val tiles = Array(7) { rowIndex ->
            Array(7) { colIndex ->
                {
                    val firstGem = gems[()]
                }
            }
        }

        for (tileIndex in 0 until 50) {
            val firstGem = gems[tileIndex * 2]
            val secondGem = gems[tileIndex * 2 + 1]
            val treasure = Treasure(firstGem, secondGem)

            val tile = GameTile(randomPath(), randomDegree(),  treasure)
        }



        for (gemIndex in gems.indices) {
            val firstGem = gems[gemIndex]
            val secondGem = gems[gemIndex + 1]
        }

        if (playerIDs.isEmpty()) {
            throw IllegalArgumentException("Can not have 0 players! No fun.")
        }
        players = playerIDs.map { createPlayer(it)}.toSet()
        currentPlayer = players.first()
    }

    private fun randomPath(): Path {

    }

    private fun randomDegree(): Degree {

    }


    override fun slide(rowPosition: RowPosition, direction: HorizontalDirection) {
        performActionAndTransitionState(GameState.SLIDE, GameState.INSERT) {
            this.dislodgedTile = board.slide(rowPosition,direction)
        }
    }

    override fun slide(columnPosition: ColumnPosition, direction: VerticalDirection) {
        performActionAndTransitionState(GameState.SLIDE, GameState.INSERT) {
            this.dislodgedTile = board.slide(columnPosition, direction)
        }
    }

    override fun insertSpareTile() {
        performActionAndTransitionState(GameState.INSERT, GameState.MOVE) {
            dislodgedTile?.let { dislodgedTile ->
                this.board.insertTileIntoEmptySlot(spareTile)
                this.spareTile = dislodgedTile
                this.dislodgedTile = null
            } ?: throw IllegalStateException("Dislodged tile must be non-null to insert spare tile.")
        }
    }

    override fun rotateSpareTile(degree: Degree) {
        ensureStateIs(GameState.INSERT)
        this.spareTile.rotate(degree)
    }

    /**
     * Performs a depth-first search of all reachable tiles starting from _position_, neighbors are determined
     * by whether two adjacent tile's have connecting shapes.
     */
    override fun getReachableTiles(startingPosition: Coordinates): Set<Tile> {
        return board.getReachableTiles(startingPosition)
    }

    override fun kickOutActivePlayer() {
        this.currentPlayer
    }

//    //TODO: define if there is max player count/when to start game
//    override fun addPlayerToGame() {
//        val newPlayer = Player(UUID.randomUUID(), )
//        this.players.add()
//    }

    private fun <T> performActionAndTransitionState(initialState: GameState, nextState: GameState, func: () -> T): T {
        ensureStateIs(initialState)
        val output = func()
        this.nextAction = nextState
        return output
    }

    private fun ensureStateIs(state: GameState) {
        if (nextAction != state) {
            throw java.lang.IllegalStateException("This is the incorrect state of the game.")
        }
    }

    private fun createPlayer(id: UUID) {

    }
}