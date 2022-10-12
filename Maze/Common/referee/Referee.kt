package Common.referee

import Common.GameState
import Common.Player
import Common.PlayerQueue
import Common.board.Board
import Common.board.ColumnPosition
import Common.board.RowPosition
import Common.tile.Degree
import Common.tile.HorizontalDirection
import Common.tile.Tile
import Common.tile.VerticalDirection

class Referee(
    private val board: Board,
    private var spareTile: Tile,
    players: List<Player>
): IReferee {
    private var dislodgedTile: Tile? = null
    private var nextAction: GameState = GameState.INITIAL
    private val playerQueue = PlayerQueue(players.toMutableList())


    override fun activePlayerCanReachTile(tile: Tile): Boolean {
        val currentPlayer = playerQueue.getCurrentPlayer()
        return board.getReachableTiles(currentPlayer.location).contains(tile)
    }

    override fun slideRow(rowPosition: RowPosition, direction: HorizontalDirection) {
        performActionAndTransitionState(GameState.SLIDE, GameState.INSERT) {
            this.dislodgedTile = board.slide(rowPosition,direction)
        }
    }

    override fun slideColumn(columnPosition: ColumnPosition, direction: VerticalDirection) {
        performActionAndTransitionState(GameState.SLIDE, GameState.INSERT) {
            this.dislodgedTile = board.slide(columnPosition, direction)
        }
    }

    override fun insertSpareTile() {
        performActionAndTransitionState(GameState.INSERT, GameState.MOVE) {
            dislodgedTile?.let {newSpareTile ->
                this.moveAmnestiedPlayersIfAny(newSpareTile)
                this.board.insertTileIntoEmptySlot(spareTile)
                this.spareTile = newSpareTile
                this.dislodgedTile = null
            } ?: throw IllegalStateException("Dislodged tile must be non-null to insert spare tile.")
        }
    }

    override fun rotateSpareTile(degree: Degree) {
        ensureStateIs(GameState.INSERT)
        this.spareTile.rotate(degree)
    }

    override fun kickOutActivePlayer() {
        val activePlayer = playerQueue.removeCurrentPlayer()
        board.removePlayerFromTiles(activePlayer)
    }

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
}