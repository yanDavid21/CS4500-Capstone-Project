package Common.board

import Common.tile.*
import Common.tile.treasure.Gem
import Common.tile.treasure.Treasure
import org.junit.Assert
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import testing.TestUtils
import kotlin.test.assertEquals

/*
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
internal class BoardTest {



    @Test
    fun testSlideHorizontal() {
        //ARRANGE
        val tiles = createTiles()
        val board = createBoard(tiles)

        // ACT
        board.slide(RowPosition(0), HorizontalDirection.RIGHT)
        val newFirstRow = tiles[0]
        val expectedTiles = arrayOf(EmptyTile(), GameTile(Path.VERTICAL, Degree.ZERO, Gem(1)), GameTile(Path.CROSS, Degree.TWO_SEVENTY, Gem(2)))

        // ASSERT
        Assert.assertArrayEquals(expectedTiles, newFirstRow)

        // ACT
        board.slide(RowPosition(2), HorizontalDirection.LEFT)
        val newLastRow = tiles[2]

        // ASSERT
        Assert.assertArrayEquals(arrayOf(GameTile(Path.CROSS, Degree.TWO_SEVENTY,  Gem(7)), GameTile(Path.T, Degree.NINETY,  Gem(8)), EmptyTile()), newLastRow)
    }

    @Test
    fun testSlideVertical() {

    }

    @Test
    fun testSlideInvalidHorizontalPosition() {
        val board = createBoard()
        assertThrows<IllegalArgumentException> { board.slide(RowPosition(1), HorizontalDirection.RIGHT) }
        assertThrows<IllegalArgumentException> { board.slide(RowPosition(3), HorizontalDirection.LEFT) }
    }

    @Test
    fun testSlideInvalidVerticalPosition() {
        val board = createBoard()
        assertThrows<IllegalArgumentException> { board.slide(ColumnPosition(1), VerticalDirection.UP) }
        assertThrows<IllegalArgumentException> { board.slide(ColumnPosition(3), VerticalDirection.DOWN) }
    }

    @Test
    fun testInsertBeforeSlideInvalid() {
        val board = createBoard()

        assertThrows<IllegalStateException>("must be non-null") {
            board.insertTileIntoEmptySlot()
        }
    }


    @Test
    fun testInsertAfterInsertInvalid() {
        val board = createBoard()

        board.slide(RowPosition(0), HorizontalDirection.RIGHT)
        board.insertTileIntoEmptySlot()

        assertThrows<IllegalStateException>("must be non-null") {
            board.insertTileIntoEmptySlot()
        }
    }

    @Test
    fun testInsertAfterASlide() {
        // ARRANGE
        val tiles = createTiles()
        val spareTile = createSpareTile()
        val board = Board(tiles)

        // ACT
        board.slideRowAndInsert(RowPosition(0), HorizontalDirection.RIGHT)
        board.insertTileIntoEmptySlot(spareTile)

        // ASSERT
        Assert.assertArrayEquals(arrayOf(spareTile, GameTile(Path.VERTICAL, Degree.ZERO,  Gem(1)), GameTile(Path.CROSS, Degree.TWO_SEVENTY,  Gem(2))), tiles[0])

        // ACT
        board.slideRowAndInsert(RowPosition(2), HorizontalDirection.LEFT)
        board.insertSpareTile()

        // ASSERT
        Assert.assertArrayEquals(arrayOf(GameTile(Path.CROSS, Degree.TWO_SEVENTY,  Gem(7)), GameTile(Path.T, Degree.NINETY,  Gem(8)), GameTile(Path.T, Degree.NINETY,  Gem(3))), tiles[2])
    }

    @Test
    fun testGetReachableTilesUnreachable() {
        // ARRANGE
        val tiles = createTiles()
        val board = createBoard(tiles)

        // ACT
        val reachableFromTopLeft = board.getReachableTiles(Coordinates(RowPosition(0), ColumnPosition(0)))

        // ASSERT
        assertEquals(setOf(), reachableFromTopLeft)
    }

    @Test
    fun testGetUnreachableTilesSome() {
        // ARRANGE
        val tiles = createTiles()
        val board = createBoard(tiles)

        // ASSERT
        val reachableFromTopRight = board.getReachableTiles(Coordinates(RowPosition(0), ColumnPosition(2)))
        assertEquals(setOf(tiles[1][2], tiles[2][2]), reachableFromTopRight)
    }

    private fun createTiles(): Array<Array<GameTile>> {
        return TestUtils.getTilesFromConnectorsAndTreasures(board, treasures)
        /*
        return arrayOf(
            arrayOf(GameTile(Path.VERTICAL, Degree.ZERO, Gem(1) ), GameTile(Path.CROSS, Degree.TWO_SEVENTY,  Gem(2)), GameTile(Path.T, Degree.NINETY,  Gem(3))),
            arrayOf(GameTile(Path.UP_RIGHT, Degree.ONE_EIGHTY,  Gem(4)), GameTile(Path.T, Degree.TWO_SEVENTY,  Gem(4)), GameTile(Path.CROSS, Degree.NINETY,  Gem(5))),
            arrayOf(GameTile(Path.VERTICAL, Degree.ZERO,  Gem(6)), GameTile(Path.CROSS, Degree.TWO_SEVENTY,  Gem(7)), GameTile(Path.T, Degree.NINETY,  Gem(8))))
       */
    }

    private fun createSpareTile(): GameTile {
        return GameTile(Path.T, Degree.ONE_EIGHTY, Treasure(Gem.AMETHYST, Gem.AMETRINE))
    }


    private fun createBoard(tiles: Array<Array<GameTile>>): Board {
        return Board(tiles)
    }

    private fun createBoard(): Board {
        return Board(createTiles())
    }

    companion object {
        val board = listOf(
            listOf("│", "─", "┐", "└", "┌", "┘", "┬"),
            listOf("┘", "│", "┌",  "─", "┐", "└", "┬"),
            listOf("┘", "┌", "│",  "┐", "└", "─", "┬"),
            listOf("┘", "│", "┌",  "─", "┐", "└", "┬"),
            listOf("│", "─", "┐", "└", "┌", "┘", "┬"),
            listOf("┘", "┌", "│",  "┐", "└", "─", "┬")
        )

        val treasures = listOf(
            listOf(Treasure(Gem.AMETHYST, Gem.ALEXANDRITE),
                Treasure(Gem.AMETHYST, Gem.ALEXANDRITE),
                Treasure(Gem.AMETHYST, Gem.ALEXANDRITE),
                Treasure(Gem.AMETHYST, Gem.ALEXANDRITE),
                Treasure(Gem.AMETHYST, Gem.ALEXANDRITE),
                Treasure(Gem.AMETHYST, Gem.ALEXANDRITE),
                Treasure(Gem.AMETHYST, Gem.ALEXANDRITE),
                Treasure(Gem.AMETHYST, Gem.ALEXANDRITE),
                Treasure(Gem.AMETHYST, Gem.ALEXANDRITE),
                Treasure(Gem.AMETHYST, Gem.ALEXANDRITE),
                Treasure(Gem.AMETHYST, Gem.ALEXANDRITE),
                Treasure(Gem.AMETHYST, Gem.ALEXANDRITE),
                Treasure(Gem.AMETHYST, Gem.ALEXANDRITE),))
    }
}

 */