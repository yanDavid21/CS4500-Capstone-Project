package Common.tile

import Common.TestData
import Common.player.Player
import Common.tile.treasure.Gem
import Common.tile.treasure.Treasure
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals


internal class TileTest {

    private val L = HorizontalDirection.LEFT
    private val R = HorizontalDirection.RIGHT
    private val U = VerticalDirection.UP
    private val D = VerticalDirection.DOWN

    private val treasure = Treasure(Gem.AMETHYST, Gem.AMETRINE)
    private val homeTile = GameTile(Path.CROSS, Degree.NINETY, Treasure(Gem.ZIRCON, Gem.RAW_BERYL))
    private val testPlayer = TestData.createPlayer2()

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

        val newTile = tile.rotate(Degree.NINETY)
        assertEquals(setOf(L, U), newTile.getOutgoingDirections())
        assertEquals(setOf(R, D), newTile.getIncomingDirections())
    }

    @Test
    fun testEquals() {
        val tile = GameTile(Path.UP_RIGHT, Degree.ONE_EIGHTY, treasure)
        val tileButTurned = GameTile(Path.UP_RIGHT, Degree.NINETY, treasure)
        val tileButTurnedAgain = GameTile(Path.UP_RIGHT, Degree.NINETY, treasure)
        assert(tile == tileButTurned)
        assert(tile == tileButTurnedAgain)
    }


    @Test
    fun testNotEquals() {
        val tile = GameTile(Path.UP_RIGHT, Degree.ONE_EIGHTY, treasure)

        val tileButDiffPath = GameTile(Path.CROSS, Degree.NINETY, treasure)
        val tileButDiffTreasure = GameTile(Path.CROSS, Degree.NINETY, Treasure(Gem.AMETHYST, Gem.HACKMANITE))

        assert(tile != tileButDiffPath)
        assert(tile != tileButDiffTreasure)
    }
    @Test
    fun testCanBeReachedFrom() {
        assertEquals(true, GameTile(Path.VERTICAL, Degree.ZERO, treasure).canBeReachedFrom(VerticalDirection.DOWN))
        assertEquals(true, GameTile(Path.VERTICAL, Degree.ZERO, treasure).canBeReachedFrom(VerticalDirection.UP))

        assertEquals(true, GameTile(Path.CROSS, Degree.ZERO, treasure).canBeReachedFrom(VerticalDirection.DOWN))
        assertEquals(true, GameTile(Path.CROSS, Degree.ZERO, treasure).canBeReachedFrom(VerticalDirection.UP))
        assertEquals(true, GameTile(Path.CROSS, Degree.ZERO, treasure).canBeReachedFrom(HorizontalDirection.LEFT))
        assertEquals(true, GameTile(Path.CROSS, Degree.ZERO, treasure).canBeReachedFrom(HorizontalDirection.RIGHT))

        assertEquals(true, GameTile(Path.T, Degree.ZERO, treasure).canBeReachedFrom(VerticalDirection.UP))
        assertEquals(true, GameTile(Path.T, Degree.ZERO, treasure).canBeReachedFrom(HorizontalDirection.LEFT))
        assertEquals(true, GameTile(Path.T, Degree.ZERO, treasure).canBeReachedFrom(HorizontalDirection.RIGHT))

        assertEquals(true, GameTile(Path.UP_RIGHT, Degree.ZERO, treasure).canBeReachedFrom(VerticalDirection.DOWN))
        assertEquals(true, GameTile(Path.UP_RIGHT, Degree.ZERO, treasure).canBeReachedFrom(HorizontalDirection.LEFT))

    }

    @Test
    fun testCanBeReachedFromRotation() {
        assertEquals(true, GameTile(Path.VERTICAL, Degree.NINETY, treasure).canBeReachedFrom(HorizontalDirection.LEFT))
        assertEquals(true, GameTile(Path.VERTICAL, Degree.NINETY, treasure).canBeReachedFrom(HorizontalDirection.RIGHT))
        assertEquals(true, GameTile(Path.VERTICAL, Degree.TWO_SEVENTY, treasure).canBeReachedFrom(HorizontalDirection.LEFT))
        assertEquals(true, GameTile(Path.VERTICAL, Degree.TWO_SEVENTY, treasure).canBeReachedFrom(HorizontalDirection.RIGHT))
        assertEquals(true, GameTile(Path.VERTICAL, Degree.ONE_EIGHTY, treasure).canBeReachedFrom(VerticalDirection.DOWN))
        assertEquals(true, GameTile(Path.VERTICAL, Degree.ONE_EIGHTY, treasure).canBeReachedFrom(VerticalDirection.UP))

        assertEquals(true, GameTile(Path.CROSS, Degree.ZERO, treasure).canBeReachedFrom(VerticalDirection.DOWN))
        assertEquals(true, GameTile(Path.CROSS, Degree.ZERO, treasure).canBeReachedFrom(VerticalDirection.UP))
        assertEquals(true, GameTile(Path.CROSS, Degree.ZERO, treasure).canBeReachedFrom(HorizontalDirection.LEFT))

        assertEquals(true, GameTile(Path.T, Degree.NINETY, treasure).canBeReachedFrom(VerticalDirection.UP))
        assertEquals(true, GameTile(Path.T, Degree.NINETY, treasure).canBeReachedFrom(VerticalDirection.DOWN))
        assertEquals(true, GameTile(Path.T, Degree.NINETY, treasure).canBeReachedFrom(HorizontalDirection.LEFT))
        assertEquals(true, GameTile(Path.T, Degree.TWO_SEVENTY, treasure).canBeReachedFrom(VerticalDirection.UP))
        assertEquals(true, GameTile(Path.T, Degree.TWO_SEVENTY, treasure).canBeReachedFrom(HorizontalDirection.RIGHT))
        assertEquals(true, GameTile(Path.T, Degree.TWO_SEVENTY, treasure).canBeReachedFrom(VerticalDirection.DOWN))
        assertEquals(true, GameTile(Path.T, Degree.ONE_EIGHTY, treasure).canBeReachedFrom(VerticalDirection.DOWN))
        assertEquals(true, GameTile(Path.T, Degree.ONE_EIGHTY, treasure).canBeReachedFrom(HorizontalDirection.LEFT))
        assertEquals(true, GameTile(Path.T, Degree.ONE_EIGHTY, treasure).canBeReachedFrom(HorizontalDirection.RIGHT))

        assertEquals(true, GameTile(Path.UP_RIGHT, Degree.NINETY, treasure).canBeReachedFrom(VerticalDirection.DOWN))
        assertEquals(true, GameTile(Path.UP_RIGHT, Degree.NINETY, treasure).canBeReachedFrom(HorizontalDirection.RIGHT))
        assertEquals(true, GameTile(Path.UP_RIGHT, Degree.ONE_EIGHTY, treasure).canBeReachedFrom(VerticalDirection.UP))
        assertEquals(true, GameTile(Path.UP_RIGHT, Degree.ONE_EIGHTY, treasure).canBeReachedFrom(HorizontalDirection.RIGHT))
        assertEquals(true, GameTile(Path.UP_RIGHT, Degree.TWO_SEVENTY, treasure).canBeReachedFrom(VerticalDirection.UP))
        assertEquals(true, GameTile(Path.UP_RIGHT, Degree.TWO_SEVENTY, treasure).canBeReachedFrom(HorizontalDirection.LEFT))
    }

    @Test
    fun testCannotBeReachedFrom() {
        assertEquals(false, GameTile(Path.VERTICAL, Degree.ZERO, treasure).canBeReachedFrom(HorizontalDirection.LEFT))
        assertEquals(false, GameTile(Path.VERTICAL, Degree.ZERO, treasure).canBeReachedFrom(HorizontalDirection.RIGHT))

        assertEquals(false, GameTile(Path.T, Degree.ZERO, treasure).canBeReachedFrom(VerticalDirection.DOWN))

        assertEquals(false, GameTile(Path.UP_RIGHT, Degree.ZERO, treasure).canBeReachedFrom(VerticalDirection.UP))
        assertEquals(false, GameTile(Path.UP_RIGHT, Degree.ZERO, treasure).canBeReachedFrom(HorizontalDirection.RIGHT))
    }

    @Test
    fun testCannotBeReachedFromRotation() {
        assertEquals(false, GameTile(Path.VERTICAL, Degree.NINETY, treasure).canBeReachedFrom(VerticalDirection.DOWN))
        assertEquals(false, GameTile(Path.VERTICAL, Degree.NINETY, treasure).canBeReachedFrom(VerticalDirection.UP))
        assertEquals(false, GameTile(Path.VERTICAL, Degree.TWO_SEVENTY, treasure).canBeReachedFrom(VerticalDirection.DOWN))
        assertEquals(false, GameTile(Path.VERTICAL, Degree.TWO_SEVENTY, treasure).canBeReachedFrom(VerticalDirection.UP))
        assertEquals(false, GameTile(Path.VERTICAL, Degree.ONE_EIGHTY, treasure).canBeReachedFrom(HorizontalDirection.LEFT))
        assertEquals(false, GameTile(Path.VERTICAL, Degree.ONE_EIGHTY, treasure).canBeReachedFrom(HorizontalDirection.RIGHT))


        assertEquals(false, GameTile(Path.T, Degree.NINETY, treasure).canBeReachedFrom(HorizontalDirection.RIGHT))
        assertEquals(false, GameTile(Path.T, Degree.TWO_SEVENTY, treasure).canBeReachedFrom(HorizontalDirection.LEFT))
        assertEquals(false, GameTile(Path.T, Degree.ONE_EIGHTY, treasure).canBeReachedFrom(VerticalDirection.UP))

        assertEquals(false, GameTile(Path.UP_RIGHT, Degree.NINETY, treasure).canBeReachedFrom(HorizontalDirection.LEFT))
        assertEquals(false, GameTile(Path.UP_RIGHT, Degree.TWO_SEVENTY, treasure).canBeReachedFrom(HorizontalDirection.RIGHT))
        assertEquals(false, GameTile(Path.UP_RIGHT, Degree.ONE_EIGHTY, treasure).canBeReachedFrom(HorizontalDirection.LEFT))
        assertEquals(false, GameTile(Path.UP_RIGHT, Degree.NINETY, treasure).canBeReachedFrom(VerticalDirection.UP))
        assertEquals(false, GameTile(Path.UP_RIGHT, Degree.TWO_SEVENTY, treasure).canBeReachedFrom(VerticalDirection.DOWN))
        assertEquals(false, GameTile(Path.UP_RIGHT, Degree.ONE_EIGHTY, treasure).canBeReachedFrom(VerticalDirection.DOWN))
    }


    @Test
    fun testAddPlayerToTile() {
        val gameTile = GameTile(Path.UP_RIGHT, Degree.ZERO, treasure)
        assertEquals(gameTile.getPlayersOnTile().size, 0)
        assertEquals(gameTile.hasCertainPlayer(testPlayer), false)
        val newGameTile = gameTile.addPlayerToTile(testPlayer)
        assertEquals(newGameTile.getPlayersOnTile().size, 1)
        assertEquals(newGameTile.hasCertainPlayer(testPlayer), true)
    }

    @Test
    fun removePlayerFromTile() {
        val gameTile = GameTile(Path.UP_RIGHT, Degree.ZERO, treasure)
        assertEquals(gameTile.hasCertainPlayer(testPlayer), false)
        val tile2 = gameTile.addPlayerToTile(testPlayer)
        assertEquals(tile2.hasCertainPlayer(testPlayer), true)
        val tile3 = gameTile.removePlayerFromTile(testPlayer)
        assertEquals(tile3.hasCertainPlayer(testPlayer), false)
    }

    @Test
    fun hasCertainPlayer() {
        val gameTile = GameTile(Path.UP_RIGHT, Degree.ZERO, treasure)
        assertEquals(gameTile.hasCertainPlayer(testPlayer), false)
        val newTile = gameTile.addPlayerToTile(testPlayer)
        assertEquals(newTile.hasCertainPlayer(testPlayer), true)
    }
}

