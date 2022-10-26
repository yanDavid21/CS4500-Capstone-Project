package Players

import Common.player.Player

class Riemann(player: Player): AbstractOrderingStrategy(
    compareBy({ coord -> coord.row.value  }, { coord -> coord.col.value }),
    player)
