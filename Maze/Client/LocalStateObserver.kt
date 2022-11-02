package Client

import Common.GameState
import Common.board.ColumnPosition
import Common.board.Coordinates
import Common.board.RowPosition
import Common.getNext
import Referee.ObserverMechanism
import serialization.data.BoardDTO
import serialization.data.StateDTO
import serialization.data.TileDTO


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
            val currentState = gamestates.first().toPublicState()

            val board = currentState.board
            val allTiles = RowPosition.getAll().map { row ->
                ColumnPosition.getAll().map { col ->
                    board.getTile(Coordinates(row, col))
                }
            }
            val serializedBoard = BoardDTO(
                allTiles.map { row -> row.map { it.toString() } },
                allTiles.map { row -> row.map { listOf(it.treasure.gem1.toString(), it.treasure.gem2.toString()) } }
            )

            val spareTile = currentState.spareTile
            val serializedSpareTile = TileDTO(
                spareTile.toString(),
                spareTile.treasure.gem1.toString(),
                spareTile.treasure.gem2.toString()
            )



            val serializedState = StateDTO(
                serializedBoard, serializedSpareTile,
            )
        }
        TODO("Not yet implemented")
    }
}