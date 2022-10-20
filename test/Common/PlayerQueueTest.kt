package Common


import Common.TestData.createPlayer1
import Common.TestData.createPlayer2
import Common.TestData.createPlayer3
import Common.player.PlayerQueue
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class PlayerQueueTest {

    @Test
    fun testGetCurrentPlayer() {
        val queue = makeQueue()

        assertEquals(createPlayer1(), queue.getCurrentPlayer())

        queue.getNextPlayer()

        assertEquals(createPlayer2(), queue.getCurrentPlayer())

        queue.getNextPlayer()

        assertEquals(createPlayer3(), queue.getCurrentPlayer())

        queue.getNextPlayer()
        assertEquals(createPlayer1(), queue.getCurrentPlayer())
    }

    @Test
    fun testRemoveCurrentPlayer() {
        val queue = makeQueue()

        val removed = queue.removeCurrentPlayer()
        assertEquals(createPlayer1(), removed)
        assertEquals(createPlayer2(), queue.getCurrentPlayer())
    }

    @Test
    fun testRemoveLastPlayer() {
        val queue = makeQueue()

        queue.getNextPlayer()
        queue.getNextPlayer()

        queue.removeCurrentPlayer()
        assertEquals(createPlayer1(), queue.getCurrentPlayer())

        queue.getNextPlayer()
        queue.getNextPlayer()
        assertEquals(createPlayer1(), queue.getCurrentPlayer())
    }

    companion object {
        fun makeQueue(): PlayerQueue {
            return PlayerQueue(
                mutableListOf(createPlayer1(), createPlayer2(), createPlayer3())
            )
        }
    }
}