package Players

import Common.player.Player

class Euclid(player: Player): AbstractOrderingStrategy(
    compareBy { it.row.value }, // TODO: distance to goal, where is goal?
    player) {
}