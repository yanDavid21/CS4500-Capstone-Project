package Common

import Common.tile.git Tile
import Common.tile.treasure.Treasure
import java.util.*

data class Player(val id: UUID,
                  val goal: Treasure,
                  val homeTile: Tile,
                  var treasureFound: Boolean)
