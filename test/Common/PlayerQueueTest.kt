package Common


import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals
import Common.TestData.player1
import Common.TestData.player2
import Common.TestData.player3

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
        fun makeQueue(): PlayerQueue {
            return PlayerQueue(
                mutableListOf(player1, player2, player3)
            )
        }
    }
}