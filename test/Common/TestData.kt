package Common

import Common.board.Board
import Common.board.Coordinates
import Common.player.Player
import Common.tile.Degree
import Common.tile.GameTile
import Common.tile.Path
import Common.tile.treasure.Gem
import Common.tile.treasure.Treasure
import testing.TestUtils
import java.util.*

object TestData {

    fun createRefereeWithOnePlayer(player: Player, player1Pos: Coordinates): Referee {
        val tiles = createTiles()
        changeTile(tiles, player1Pos) { it.addPlayerToTile(player) }

        val board = createBoard(tiles)

        return Referee(board, createSpareTile(), listOf(player))
    }

    fun createReferee(tiles: Array<Array<GameTile>>): Referee {
        val player1 = createPlayer1()
        changeTile(tiles, 0, 0) { it.addPlayerToTile(player1) }

        val player2 = createPlayer2()
        changeTile(tiles, 0, 2) { it.addPlayerToTile(player2) }

        val player3 = createPlayer3()
        changeTile(tiles, 6, 6) { it.addPlayerToTile(player3) }

        val board = createBoard(tiles)
        return Referee(board, createSpareTile(), listOf(player1, player2, player3))
    }

    fun createReferee(): Referee {
        val tiles = createTiles()
        return createReferee(tiles)
    }

    fun createTiles(): Array<Array<GameTile>> {
        return TestUtils.getTilesFromConnectorsAndTreasures(TestData.board,
            TestUtils.getTreasuresFromStrings(TestData.treasureStrings))
    }

    fun createSpareTile(): GameTile {
        return GameTile(Path.T, Degree.ONE_EIGHTY, Treasure(Gem.AMETHYST, Gem.AMETRINE))
    }


    fun createBoard(tiles: Array<Array<GameTile>>): Board {
        return Board(tiles)
    }

    fun createBoard(): Board {
        return Board(createTiles())
    }

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

    fun createPlayer1(): Player {
        return Player(
            UUID.fromString("f9728f95-96db-4cf4-a9c1-13113635d312"),
            Treasure(Gem.BLACK_OBSIDIAN, Gem.GROSSULAR_GARNET),
            createTiles()[0][0]
        )
    }

    fun createPlayer2(): Player {
        return Player(
            UUID.fromString("f9728f95-96db-4cf4-a9c1-13113635d312"),
            Treasure(Gem.HEMATITE, Gem.HACKMANITE),
            createTiles()[0][2]
        )
    }

    fun createPlayer3(): Player {
        return Player(
            UUID.fromString("f25bc452-5ccc-4d29-8ad1-a76f89f42c24"),
            Treasure(Gem.ALEXANDRITE, Gem.ZIRCON),
            createTiles()[6][6]
        )
    }

    private fun changeTile(tiles: Array<Array<GameTile>>, pos: Coordinates, func: (GameTile) -> GameTile) {
        changeTile(tiles, pos.row.value, pos.col.value, func)
    }

    private fun changeTile(tiles: Array<Array<GameTile>>, row:Int, col: Int, func: (GameTile) -> GameTile) {
        tiles[row][col] = func(tiles[row][col])
    }
}