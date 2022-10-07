package Common.tile

import Common.HorizontalDirection
import Common.VerticalDirection
import Common.board.EmptyTile
import Common.board.GameTile
import Common.board.Path
import Common.board.tile.Degree
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

internal class TileTest {

    private val L = HorizontalDirection.LEFT
    private val R = HorizontalDirection.RIGHT
    private val U = VerticalDirection.UP
    private val D = VerticalDirection.DOWN

    @Test
    fun testGetOutComingDirectionsNoRotation() {
        assertEquals(setOf(U, D), GameTile(Path.VERTICAL, Degree.ZERO).getOutgoingDirections())
        assertEquals(setOf(R, U), GameTile(Path.UP_RIGHT, Degree.ZERO).getOutgoingDirections())
        assertEquals(setOf(L, R, D), GameTile(Path.T, Degree.ZERO).getOutgoingDirections())
        assertEquals(setOf(R, U, L, D), GameTile(Path.CROSS, Degree.ZERO).getOutgoingDirections())
    }

    @Test
    fun testGetOutComingDirectiosnRotate() {
        assertEquals(setOf(L, R), GameTile(Path.VERTICAL, Degree.NINETY).getOutgoingDirections())
        assertEquals(setOf(L, U, R, D), GameTile(Path.CROSS, Degree.ONE_EIGHTY).getOutgoingDirections())
        assertEquals(setOf(L, D), GameTile(Path.UP_RIGHT, Degree.ONE_EIGHTY).getOutgoingDirections())
        assertEquals(setOf(U, D, L), GameTile(Path.T, Degree.TWO_SEVENTY).getOutgoingDirections())
    }

    @Test
    fun getIncomingDirectionsNoRotation() {
        assertEquals(setOf(U, D), GameTile(Path.VERTICAL, Degree.ZERO).getIncomingDirections())
        assertEquals(setOf(L, D), GameTile(Path.UP_RIGHT, Degree.ZERO).getIncomingDirections())
        assertEquals(setOf(R, L, U), GameTile(Path.T, Degree.ZERO).getIncomingDirections())
        assertEquals(setOf(L, R, D, U), GameTile(Path.CROSS, Degree.ZERO).getIncomingDirections())
    }

    @Test
    fun getDirectionsAfterRotation() {
        val tile = GameTile(Path.UP_RIGHT, Degree.ZERO)

        assertEquals(setOf(U, R), tile.getOutgoingDirections())
        assertEquals(setOf(D, L), tile.getIncomingDirections())

        tile.rotate(Degree.NINETY)
        assertEquals(setOf(L, U), tile.getOutgoingDirections())
        assertEquals(setOf(R, D), tile.getIncomingDirections())

        tile.rotate(Degree.NINETY)
    }

    @Test
    fun testEmptyTileHasNoOutgoingDirections() {
        assertEquals(setOf(), EmptyTile().getOutgoingDirections())
    }

    @Test
    fun testEmptyTileHasNoIncomingDirections() {
        assertEquals(setOf(), EmptyTile().getIncomingDirections())
    }

    @Test
    fun testEmptyTileIsUnreachable() {
        val empty = EmptyTile()

        HorizontalDirection.values().forEach { assertFalse { empty.canBeReachedFrom(it) } }
        VerticalDirection.values().forEach { assertFalse { empty.canBeReachedFrom(it) } }
    }

    @Test
    fun testEmptyTileEqualsEmptyTile() {
        assertEquals(EmptyTile(), EmptyTile())

        val emptyTile = EmptyTile()
        assertEquals(emptyTile, emptyTile)
    }

}