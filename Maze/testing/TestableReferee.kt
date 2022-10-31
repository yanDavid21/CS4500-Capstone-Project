package testing

import Common.GameState
import Common.board.Board
import Players.PlayerMechanism
import Players.Referee

class TestableReferee: Referee() {

    override fun createStateFromChosenBoard(suggestedBoards: List<Board>, players: List<PlayerMechanism>): GameState {
        TODO("Not yet implemented")
    }
}