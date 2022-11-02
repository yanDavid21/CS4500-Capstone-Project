package Client

import Common.GameState
import Common.board.ColumnPosition
import Common.board.Coordinates
import Common.board.RowPosition
import Common.getNext
import Referee.ObserverMechanism
import testing.BoardTest


class LocalStateObserver(state: GameState): ObserverMechanism {
    private var gamestates = listOf(state)
    private var isGameOver = false

    override fun updateState(newState: GameState) {
        gamestates = gamestates + listOf(newState)
    }

    override fun gameOver() {
        isGameOver = true
    }

    // CLIENT REQUESTS
    fun next(): GameState? {
        if (gamestates.isNotEmpty()) {
            val first = gamestates.first()
            gamestates = gamestates.getNext()
            return first
        }
        return null
    }


    fun save(filepath: String) {
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