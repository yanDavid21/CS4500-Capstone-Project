package Common.board

import Common.ColumnPosition
import Common.Coordinates
import Common.HorizontalDirection
import Common.RowPosition
import Common.board.tile.Degree
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals


@TestInstance(TestInstance.Lifecycle.PER_METHOD)
internal class BoardTest {

    @Test
    fun testSlideHorizontal() {
        val tiles = createTiles()
        val board = createBoard(tiles)

        board.slide(RowPosition(0), HorizontalDirection.RIGHT)

        val newFirstRow = tiles[0]
        assertEquals(mutableListOf(Tile(Path.VERTICAL, Degree.ZERO), Tile(Path.CROSS, Degree.TWO_SEVENTY)), newFirstRow)

        board.slide(RowPosition(2), HorizontalDirection.LEFT)

        val newLastRow = tiles[2]
        assertEquals(mutableListOf(Tile(Path.CROSS, Degree.TWO_SEVENTY), Tile(Path.T, Degree.NINETY)), newLastRow)
    }

    @Test
    fun testSlideInvalidHorizontalPosition() {
        val board = createBoard()
        assertThrows<IllegalArgumentException> { board.slide(RowPosition(1), HorizontalDirection.RIGHT) }
        assertThrows<IllegalArgumentException> { board.slide(RowPosition(3), HorizontalDirection.LEFT) }
    }

    @Test
    fun testInsertAfterASlide() {
        val tiles = createTiles()
        val spareTile = createSpareTile()
        val board = Board(tiles, spareTile)

        board.slide(RowPosition(0), HorizontalDirection.RIGHT)

        board.insertSpareTile()

        assertEquals(mutableListOf(spareTile, Tile(Path.VERTICAL, Degree.ZERO), Tile(Path.CROSS, Degree.TWO_SEVENTY)), tiles[0])

        board.slide(RowPosition(2), HorizontalDirection.LEFT)

        board.insertSpareTile()

        assertEquals(mutableListOf(Tile(Path.CROSS, Degree.TWO_SEVENTY), Tile(Path.T, Degree.NINETY), Tile(Path.T, Degree.NINETY)), tiles[2])

    }

    @Test
    fun testGetReachableTiles() {
        val tiles = createTiles()
        val board = createBoard(tiles)

        val reachableFromTopLeft = board.getReachableTiles(Coordinates(RowPosition(0), ColumnPosition(0)))
        assertEquals(setOf(tiles[1][0]), reachableFromTopLeft)
    }

    private fun createTiles(): MutableList<MutableList<Tile>> {
        return mutableListOf(
            mutableListOf(Tile(Path.VERTICAL, Degree.ZERO), Tile(Path.CROSS, Degree.TWO_SEVENTY), Tile(Path.T, Degree.NINETY)),
            mutableListOf(Tile(Path.UP_RIGHT, Degree.ONE_EIGHTY), Tile(Path.T, Degree.TWO_SEVENTY), Tile(Path.CROSS, Degree.NINETY)),
            mutableListOf(Tile(Path.VERTICAL, Degree.ZERO), Tile(Path.CROSS, Degree.TWO_SEVENTY), Tile(Path.T, Degree.NINETY)))
    }
    private fun createSpareTile(): Tile {
        return Tile(Path.T, Degree.ONE_EIGHTY)
    }

    private fun createBoard(tiles: MutableList<MutableList<Tile>>): Board {
        return Board(tiles, createSpareTile())
    }

    private fun createBoard(): PlayableBoard {
        return Board(createTiles(), createSpareTile())
    }
}