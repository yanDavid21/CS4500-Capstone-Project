package testing

import Common.Action
import Common.PublicGameState
import Common.board.Coordinates
import Common.player.PlayerData
import Common.tile.GameTile
import Players.PlayerMechanism

class TestablePlayerMechanism(
    override val name: String,
    private val strategy: StrategyDesignation,
    private var nextGoal: Coordinates
): PlayerMechanism {
    private var hasFoundTreasure = false

    override fun proposeBoard0(rows: Int, columns: Int): Array<Array<GameTile>> {
        throw IllegalStateException("Player should not be asked to propose board yet.")
    }

    override fun setupAndUpdateGoal(state: PublicGameState?, goal: Coordinates) {
        if (state == null) {
            this.hasFoundTreasure = true
        }
        this.nextGoal = goal
    }


    override fun takeTurn(state: PublicGameState): Action {
        val (name, currentPosition, homePosition, color) = state.getPlayerData(name)
        val playerData = PlayerData(name, currentPosition, nextGoal, homePosition, color, hasFoundTreasure)
        return strategy.getStrategy(playerData).decideMove(state)
    }

    override fun won(hasPlayerWon: Boolean) {
        return
    }
}