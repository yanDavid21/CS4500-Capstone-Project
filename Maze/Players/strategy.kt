package Players

import Common.Action
import Common.PlayerState

interface MazeStrategy {
    fun decideMove(playerState: PlayerState): Action
}