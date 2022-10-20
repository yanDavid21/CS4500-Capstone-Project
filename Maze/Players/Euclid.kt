package Players

import Common.board.Coordinates
import Common.player.Player

class Euclid(player: Player): AbstractOrderingStrategy(
    compareBy { coord -> euclidDistanceFromGoal(coord, player) },
    player) {
}

fun euclidDistanceFromGoal(targetCoords: Coordinates, player: Player): Double {
    val goalPosition = if (player.treasureFound) player.homePosition else player.goalPosition

    return targetCoords.euclidDistanceTo(goalPosition)
}