package Common.tile

import Common.tile.treasure.Gem
import Common.tile.treasure.Treasure
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals


internal class TileTest {

    private val L = HorizontalDirection.LEFT
    private val R = HorizontalDirection.RIGHT
    private val U = VerticalDirection.UP
    private val D = VerticalDirection.DOWN

    private val treasure = Treasure(Gem.AMETHYST, Gem.AMETRINE)
    @Test
    fun testGetOutComingDirectionsNoRotation() {
        assertEquals(setOf(U, D), GameTile(Path.VERTICAL, Degree.ZERO, treasure).getOutgoingDirections())
        assertEquals(setOf(R, U), GameTile(Path.UP_RIGHT, Degree.ZERO,  treasure).getOutgoingDirections())
        assertEquals(setOf(L, R, D), GameTile(Path.T, Degree.ZERO,  treasure).getOutgoingDirections())
        assertEquals(setOf(R, U, L, D), GameTile(Path.CROSS, Degree.ZERO,  treasure).getOutgoingDirections())
    }

    @Test
    fun testGetOutComingDirectiosnRotate() {
        assertEquals(setOf(L, R), GameTile(Path.VERTICAL, Degree.NINETY,  treasure).getOutgoingDirections())
        assertEquals(setOf(L, U, R, D), GameTile(Path.CROSS, Degree.ONE_EIGHTY,  treasure).getOutgoingDirections())
        assertEquals(setOf(L, D), GameTile(Path.UP_RIGHT, Degree.ONE_EIGHTY,  treasure).getOutgoingDirections())
        assertEquals(setOf(U, D, L), GameTile(Path.T, Degree.TWO_SEVENTY,  treasure).getOutgoingDirections())
    }

    @Test
    fun getIncomingDirectionsNoRotation() {
        assertEquals(setOf(U, D), GameTile(Path.VERTICAL, Degree.ZERO, treasure).getIncomingDirections())
        assertEquals(setOf(L, D), GameTile(Path.UP_RIGHT, Degree.ZERO, treasure).getIncomingDirections())
        assertEquals(setOf(R, L, U), GameTile(Path.T, Degree.ZERO, treasure).getIncomingDirections())
        assertEquals(setOf(L, R, D, U), GameTile(Path.CROSS, Degree.ZERO, treasure).getIncomingDirections())
    }

    @Test
    fun getDirectionsAfterRotation() {
        val tile = GameTile(Path.UP_RIGHT, Degree.ZERO,  treasure)

        assertEquals(setOf(U, R), tile.getOutgoingDirections())
        assertEquals(setOf(D, L), tile.getIncomingDirections())

        tile.rotate(Degree.NINETY)
        assertEquals(setOf(L, U), tile.getOutgoingDirections())
        assertEquals(setOf(R, D), tile.getIncomingDirections())

        tile.rotate(Degree.NINETY)
    }
}

