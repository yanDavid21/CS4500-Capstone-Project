package Common

import Common.board.Coordinates
import Common.tile.Tile
import Common.tile.Treasure
import java.util.*

data class Player(val id: UUID,
                  val goal: Treasure,
                  val homeTile: Tile,
                  var location: Coordinates,
                  var treasureFound: Boolean)
