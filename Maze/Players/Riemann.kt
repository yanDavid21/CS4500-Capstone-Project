package Players

import Common.player.Player

class Riemann(player: Player): AbstractOrderingStrategy(compareBy({ it.row.value  }, { it.col.value }), player)
