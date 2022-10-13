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


@TestInstance(TestInstance.Lifecycle.PER_METHOD)
internal class BoardTest {



    @Test
    fun testSlideHorizontal() {
        //ARRANGE
        val tiles = createTiles()
        val board = createBoard(tiles)
        val spareTile = GameTile(Path.T, Degree.ZERO, Treasure(Gem.AMETRINE, Gem.AMETHYST))
        val spareTile2 = GameTile(Path.CROSS, Degree.NINETY, Treasure(Gem.AMETRINE, Gem.AMETHYST))

        // ACT
        board.slideRowAndInsert(RowPosition(0), HorizontalDirection.RIGHT, spareTile)
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
    fun testSlideVertical() {

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
        assertEquals(setOf(), reachableFromTopLeft)
    }

    @Test
    fun testGetUnreachableTilesSome() {
        // ARRANGE
        val tiles = createTiles()
        val board = createBoard(tiles)

        // ASSERT
        val reachableFromTopRight = board.getReachableTiles(Coordinates(RowPosition(6), ColumnPosition(6)))
        assertEquals(setOf(tiles[5][4], tiles[5][5], tiles[6][4], tiles[6][5]), reachableFromTopRight)
    }



    private fun createTiles(): Array<Array<GameTile>> {
        return TestUtils.getTilesFromConnectorsAndTreasures(board, TestUtils.getTreasuresFromStrings(treasureStrings))
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

    private fun <T> getArrayWithElementInIndex(array: Array<T>, index: Int, element: T): Array<T> {
        val copy = array.copyOf()
        copy[index] = element
        return copy
    }

    companion object {
        val board = listOf(
            listOf("│", "─", "┐", "└", "┌", "┘", "┬"),
            listOf("┘", "│", "┌",  "─", "┐", "└", "┬"),
            listOf("┘", "┌", "│",  "┐", "└", "─", "┬"),
            listOf("┘", "│", "┌",  "─", "┐", "└", "┬"),
            listOf("┘", "│", "┌",  "─", "┐", "└", "┬"),
            listOf("│", "─", "┐", "└", "┌", "┘", "┬"),
            listOf("┘", "┌", "│",  "┐", "└", "─", "┬")
        )

        val treasureStrings = listOf(listOf(listOf("grossular-garnet", "black-obsidian"), listOf("tigers-eye", "yellow-beryl-oval"),
            listOf("chrysoberyl-cushion", "color-change-oval"), listOf("sunstone", "prasiolite"),
            listOf("citrine", "purple-oval"), listOf("emerald", "heliotrope"), listOf("zircon", "pink-spinel-cushion")),
            listOf(listOf("grossular-garnet", "goldstone"), listOf("ruby-diamond-profile", "zoisite"), listOf("star-cabochon", "ruby"),
                listOf("chrome-diopside", "beryl"), listOf("moonstone", "rock-quartz"), listOf("kunzite-oval", "green-beryl"),
                listOf("emerald", "green-princess-cut")), listOf(listOf("stilbite", "orange-radiant"), listOf("padparadscha-sapphire", "rose-quartz"),
                listOf("purple-spinel-trillion", "garnet"), listOf("blue-ceylon-sapphire", "fancy-spinel-marquise"),
                listOf("apricot-square-radiant", "emerald"), listOf("rhodonite", "almandine-garnet"), listOf("tigers-eye", "apatite")),
            listOf(listOf("pink-opal", "australian-marquise"), listOf("dumortierite", "tourmaline-laser-cut"), listOf("hematite", "ametrine"),
                listOf("hackmanite", "carnelian"), listOf("citrine-checkerboard", "heliotrope"), listOf("green-princess-cut", "zircon"),
                listOf("pink-spinel-cushion", "yellow-heart")), listOf(listOf("red-spinel-square-emerald-cut", "carnelian"), listOf("ammolite", "green-beryl"),
                listOf("aquamarine", "blue-pear-shape"), listOf("clinohumite", "chrysoberyl-cushion"), listOf("red-spinel-square-emerald-cut",
                    "padparadscha-oval"), listOf("carnelian", "grandidierite"), listOf("raw-citrine", "golden-diamond-cut")),
            listOf(listOf("cordierite", "green-aventurine"), listOf("tanzanite-trillion", "chrome-diopside"), listOf("garnet", "morganite-oval"),
                listOf("golden-diamond-cut", "grossular-garnet"), listOf("tourmaline-laser-cut", "blue-pear-shape"), listOf("magnesite", "chrysolite"),
                listOf("purple-cabochon", "moss-agate")), listOf(listOf("spinel", "sunstone"), listOf("pink-spinel-cushion", "carnelian"),
                listOf("apricot-square-radiant", "jaspilite"), listOf("magnesite", "moonstone"), listOf("ametrine", "ruby"), listOf("citrine", "diamond"),
                listOf("blue-ceylon-sapphire", "chrysoberyl-cushion")))
    }
}

