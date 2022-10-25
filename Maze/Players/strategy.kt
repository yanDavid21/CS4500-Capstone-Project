package Players

import Common.Action
import Common.PublicGameState

interface MazeStrategy {
    fun decideMove(playerState: PublicGameState): Action
}