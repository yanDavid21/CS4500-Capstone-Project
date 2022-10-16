package Common.board

import Common.TestData.createBoard
import Common.TestData.createTiles
import Common.tile.*
import Common.tile.treasure.Gem
import Common.tile.treasure.Treasure
import org.junit.Assert
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals


@TestInstance(TestInstance.Lifecycle.PER_METHOD)
internal class BoardTest {

    /**
     * Remove players from tile
     *
     * Find player location
     *
     * Get tile
     */

    @Test
    fun testSlideHorizontal() {
        //ARRANGE
        val tiles = createTiles()
        val board = createBoard(tiles)
        val spareTile = GameTile(Path.T, Degree.ZERO, Treasure(Gem.AMETRINE, Gem.AMETHYST))
        val spareTile2 = GameTile(Path.CROSS, Degree.NINETY, Treasure(Gem.AMETRINE, Gem.AMETHYST))

        // ACT
        val newBoard = board.slideRowAndInsert(RowPosition(0), HorizontalDirection.RIGHT, spareTile)
        val newFirstRow = tiles[0]
        val expectedTiles = getArrayWithElementInIndex(newFirstRow,0,  spareTile)

        // ASSERT

        Assert.assertArrayEquals(expectedTiles, newFirstRow)

        // ACT
        board.slideRowAndInsert(RowPosition(2), HorizontalDirection.LEFT, spareTile2)
        val newLastRow = tiles[2]

        // ASSERT
        Assert.assertArrayEquals(getArrayWithElementInIndex(newLastRow, 6, spareTile2), newLastRow)
    }

    @Test
    fun testSlideVerticalUP() {
        val tiles = createTiles()
        val board = createBoard(tiles)
        val spareTile = GameTile(Path.T, Degree.ZERO, Treasure(Gem.AMETRINE, Gem.AMETHYST))

        board.slideColAndInsert(ColumnPosition(0), VerticalDirection.UP, spareTile)
        val newFirstCol = tiles.map { it[0] }.toTypedArray()
        val expectedTiles = getArrayWithElementInIndex(newFirstCol, 6, spareTile)
        Assert.assertArrayEquals(expectedTiles, newFirstCol)

    }

    @Test
    fun testSlideVerticalDown() {
        val tiles = createTiles()
        val board = createBoard(tiles)
        val spareTile2 = GameTile(Path.CROSS, Degree.NINETY, Treasure(Gem.AMETRINE, Gem.AMETHYST))

        board.slideColAndInsert(ColumnPosition(6), VerticalDirection.DOWN, spareTile2)
        val newLastCol = tiles.map { it[6] }.toTypedArray()
        Assert.assertArrayEquals(getArrayWithElementInIndex(newLastCol, 0, spareTile2), newLastCol)
    }


    @Test
    fun testSlideInvalidHorizontalPosition() {
        val board = createBoard()
        val someTile = GameTile(Path.UP_RIGHT, Degree.TWO_SEVENTY, Treasure(Gem.ALEXANDRITE, Gem.APATITE))
        assertThrows<IllegalArgumentException> { board.slideRowAndInsert(RowPosition(1), HorizontalDirection.RIGHT, someTile) }
        assertThrows<IllegalArgumentException> { board.slideRowAndInsert(RowPosition(3), HorizontalDirection.LEFT, someTile) }
    }

    @Test
    fun testSlideInvalidVerticalPosition() {
        val board = createBoard()
        val someTile = GameTile(Path.UP_RIGHT, Degree.TWO_SEVENTY, Treasure(Gem.ALEXANDRITE, Gem.APATITE))
        assertThrows<IllegalArgumentException> { board.slideColAndInsert(ColumnPosition(1), VerticalDirection.UP, someTile) }
        assertThrows<IllegalArgumentException> { board.slideColAndInsert(ColumnPosition(3), VerticalDirection.DOWN, someTile) }
    }


    @Test
    fun testGetReachableTilesUnreachable() {
        // ARRANGE
        val tiles = createTiles()
        val board = createBoard(tiles)

        // ACT
        val reachableFromTopLeft = board.getReachableTiles(Coordinates(RowPosition(1), ColumnPosition(1)))

        // ASSERT
        assertEquals(setOf(Coordinates.fromRowAndValue(1, 1)), reachableFromTopLeft)
    }

    @Test
    fun testGetUnreachableTilesSome() {
        // ARRANGE
        val tiles = createTiles()
        val board = createBoard(tiles)

        // ASSERT
        val reachableFromTopRight = board.getReachableTiles(Coordinates.fromRowAndValue(6, 6))
        assertEquals(setOf(Coordinates.fromRowAndValue(5, 4), Coordinates.fromRowAndValue(5,5),
            Coordinates.fromRowAndValue(6, 4), Coordinates.fromRowAndValue(6,5),
        Coordinates.fromRowAndValue(6,6)), reachableFromTopRight)
    }





    private fun <T> getArrayWithElementInIndex(array: Array<T>, index: Int, element: T): Array<T> {
        val copy = array.copyOf()
        copy[index] = element
        return copy
    }


}

