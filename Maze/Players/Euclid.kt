package Players

import Common.board.Coordinates
import Common.player.Player

class Euclid(player: Player): AbstractOrderingStrategy(
    compareBy({ coord -> euclidDistanceFromGoal(coord, player) }, { it.row.value }, { it.col.value }),
    player)

fun euclidDistanceFromGoal(targetCoords: Coordinates, player: Player): Double {
    val goalPosition = player.getGoal()
    return targetCoords.euclidDistanceTo(goalPosition)
}