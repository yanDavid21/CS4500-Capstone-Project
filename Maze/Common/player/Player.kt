package Common.player

import Common.tile.GameTile
import Common.tile.treasure.Treasure
import java.util.*

data class Player(val id: UUID,
                  val goal: Treasure,
                  val homeTile: GameTile,
                  var treasureFound: Boolean = false)
