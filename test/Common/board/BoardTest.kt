package Common.board

import Common.HorizontalDirection
import Common.RowPosition
import Common.board.tile.Degree
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class BoardTest {

    private lateinit var smallBoardTiles: MutableList<MutableList<Tile>>

    private lateinit var spareTile: Tile

    private lateinit var smallBoard: PlayableBoard

    @BeforeAll
    fun setUp() {
        smallBoardTiles = mutableListOf(
            mutableListOf(Tile(Path.VERTICAL, Degree.ZERO), Tile(Path.CROSS, Degree.TWO_SEVENTY), Tile(Path.T, Degree.NINETY)),
            mutableListOf(Tile(Path.UP_RIGHT, Degree.ONE_EIGHTY), Tile(Path.T, Degree.TWO_SEVENTY), Tile(Path.CROSS, Degree.NINETY)),
            mutableListOf(Tile(Path.VERTICAL, Degree.ZERO), Tile(Path.CROSS, Degree.TWO_SEVENTY), Tile(Path.T, Degree.NINETY))
        )

        spareTile = Tile(Path.T, Degree.ONE_EIGHTY)

        smallBoard = Board(smallBoardTiles, spareTile)
    }

    @Test
    fun testSlideHorizontal() {
        smallBoard.slide(RowPosition(0), HorizontalDirection.RIGHT)

        val newFirstRow = smallBoardTiles[0]
        assertEquals(mutableListOf(Tile(Path.VERTICAL, Degree.ZERO), Tile(Path.CROSS, Degree.TWO_SEVENTY)), newFirstRow)

        smallBoard.slide(RowPosition(2), HorizontalDirection.LEFT)

        val newLastRow = smallBoardTiles[2]
        assertEquals(mutableListOf(Tile(Path.CROSS, Degree.TWO_SEVENTY), Tile(Path.T, Degree.NINETY)), newLastRow)
    }

    @Test
    fun testSlideInvalidHorizontalPosition() {
        assertThrows<IllegalArgumentException> { smallBoard.slide(RowPosition(1), HorizontalDirection.RIGHT) }
        assertThrows<IllegalArgumentException> { smallBoard.slide(RowPosition(2), HorizontalDirection.LEFT) }
    }

    @Test

}