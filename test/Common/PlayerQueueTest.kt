package Common

import Common.tile.Degree
import Common.tile.GameTile
import Common.tile.Path
import Common.tile.treasure.Gem
import Common.tile.treasure.Treasure
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

internal class PlayerQueueTest {

    @Test
    fun testGetCurrentPlayer() {
        val queue = makeQueue()

        assertEquals(player1, queue.getCurrentPlayer())

        queue.nextPlayer()

        assertEquals(player2, queue.getCurrentPlayer())

        queue.nextPlayer()

        assertEquals(player3, queue.getCurrentPlayer())

        queue.nextPlayer()
        assertEquals(player1, queue.getCurrentPlayer())
    }

    @Test
    fun testRemoveCurrentPlayer() {
        val queue = makeQueue()

        val removed = queue.removeCurrentPlayer()
        assertEquals(player1, removed)
        assertEquals(player2, queue.getCurrentPlayer())
    }

    @Test
    fun testRemoveLastPlayer() {
        val queue = makeQueue()

        queue.nextPlayer()
        queue.nextPlayer()

        queue.removeCurrentPlayer()
        assertEquals(player1, queue.getCurrentPlayer())

        queue.nextPlayer()
        queue.nextPlayer()
        assertEquals(player1, queue.getCurrentPlayer())
    }

    companion object {
        val player1 = Player(
            UUID.fromString("f9728f95-96db-4cf4-a9c1-13113635d312"),
            Treasure(Gem.APLITE, Gem.AMETHYST),
            GameTile(Path.CROSS, Degree.ZERO, Treasure(Gem.HEMATITE, Gem.HACKMANITE)))
        val player2 = Player(
            UUID.fromString("f9728f95-96db-4cf4-a9c1-13113635d312"),
            Treasure(Gem.HEMATITE, Gem.HACKMANITE),
            GameTile(Path.CROSS, Degree.ONE_EIGHTY, Treasure(Gem.APLITE, Gem.AMETHYST))
        )
        val player3 = Player(
            UUID.fromString("f25bc452-5ccc-4d29-8ad1-a76f89f42c24"),
            Treasure(Gem.ALEXANDRITE, Gem.ZIRCON),
            GameTile(Path.VERTICAL, Degree.ONE_EIGHTY, Treasure(Gem.ALEXANDRITE, Gem.ZIRCON))
        )

        fun makeQueue(): PlayerQueue {
            return PlayerQueue(
                mutableListOf(player1, player2, player3)
            )
        }
    }
}