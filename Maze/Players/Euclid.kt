package Players

import Common.player.Player

class Euclid(player: Player): AbstractOrderingStrategy(
    compareBy { it.distanceTo(player.goal) },
    player) {
}