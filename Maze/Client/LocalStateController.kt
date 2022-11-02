package Client

import Client.javafx.JavaFXObserverView
import Client.javafx.MazeObserverInterface
import Common.GameState
import Common.board.ColumnPosition
import Common.board.Coordinates
import Common.board.RowPosition
import Common.getNext
import testing.BoardTest

// create observers
//     observer -> javafxview() -> application launch
// create observable referee (observers)
//

class LocalStateController(state: GameState, private val view: JavaFXObserverView): MazeObserverInterface {
    private var gamestates = listOf<GameState>(state)

    init {
        view.giveFeatures(this)
    }

    // REFEREE REQUESTS
    fun notifyGameOver() {
        view.gameOver()
    }

    fun updateQueueOfGameStates(gameState: GameState) {
        gamestates = gamestates + listOf(gameState)
    }


    // CLIENT REQUESTS
    override fun next(): GameState? {
        val first = gamestates.first()
        gamestates = gamestates.getNext()
        return first
    }

    override fun save(filepath: String) {
        if (gamestates.isNotEmpty()) {
            val currentState = gamestates.first()

            val board = currentState.getBoard()
            val allTiles = RowPosition.getAll().map { row ->
                ColumnPosition.getAll().map { col ->
                    board.getTile(Coordinates(row, col))
                }
            }

            val serializedBoard = BoardTest(
                allTiles.map { row -> row.map { it.path.symbol } },
                allTiles.map { row -> row.map { listOf(it.treasure.gem1.toString(), it.treasure.gem2.toString()) } }
            )


            //val serialized = RefereeState(
            //  serializedBoard,
            //)
        }
        TODO("Not yet implemented")
    }

}