package Common.board

import Common.ColumnPosition
import Common.Coordinates
import Common.HorizontalDirection
import Common.RowPosition
import Common.board.tile.Degree
import org.junit.Assert
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
        val expectedTiles = arrayOf(EmptyTile(), GameTile(Path.VERTICAL, Degree.ZERO), GameTile(Path.CROSS, Degree.TWO_SEVENTY))
        Assert.assertArrayEquals(expectedTiles, newFirstRow)

        board.slide(RowPosition(2), HorizontalDirection.LEFT)

        val newLastRow = tiles[2]
        Assert.assertArrayEquals(arrayOf(GameTile(Path.CROSS, Degree.TWO_SEVENTY), GameTile(Path.T, Degree.NINETY), EmptyTile()), newLastRow)
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

        Assert.assertArrayEquals(arrayOf(spareTile, GameTile(Path.VERTICAL, Degree.ZERO), GameTile(Path.CROSS, Degree.TWO_SEVENTY)), tiles[0])

        board.slide(RowPosition(2), HorizontalDirection.LEFT)

        board.insertSpareTile()

        Assert.assertArrayEquals(arrayOf<Tile>(GameTile(Path.CROSS, Degree.TWO_SEVENTY), GameTile(Path.T, Degree.NINETY), GameTile(Path.T, Degree.NINETY)), tiles[2])

    }

    @Test
    fun testGetReachableTiles() {
        val tiles = createTiles()
        val board = createBoard(tiles)

        val reachableFromTopLeft = board.getReachableTiles(Coordinates(RowPosition(0), ColumnPosition(0)))
        assertEquals(setOf(tiles[1][0]), reachableFromTopLeft)
    }

    private fun createTiles(): Array<Array<Tile>> {
        return arrayOf(
            arrayOf(GameTile(Path.VERTICAL, Degree.ZERO), GameTile(Path.CROSS, Degree.TWO_SEVENTY), GameTile(Path.T, Degree.NINETY)),
            arrayOf(GameTile(Path.UP_RIGHT, Degree.ONE_EIGHTY), GameTile(Path.T, Degree.TWO_SEVENTY), GameTile(Path.CROSS, Degree.NINETY)),
            arrayOf(GameTile(Path.VERTICAL, Degree.ZERO), GameTile(Path.CROSS, Degree.TWO_SEVENTY), GameTile(Path.T, Degree.NINETY)))
    }
    private fun createSpareTile(): Tile {
        return GameTile(Path.T, Degree.ONE_EIGHTY)
    }

    private fun createBoard(tiles: Array<Array<Tile>>): Board {
        return Board(tiles, createSpareTile())
    }

    private fun createBoard(): PlayableBoard {
        return Board(createTiles(), createSpareTile())
    }
}