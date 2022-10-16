package Common.referee

import Common.Referee
import Common.TestData
import Common.board.Board
import Common.board.ColumnPosition
import Common.board.Coordinates
import Common.board.RowPosition
import Common.tile.Degree
import Common.tile.HorizontalDirection
import Common.tile.VerticalDirection
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

internal class RefereeTest {

    @Test
    fun testMovePlayerToUnreachabletile() {
        val referee = TestData.createReferee()

        assertThrows<IllegalArgumentException>("Can not move active player to (0,0)") {
            referee.moveActivePlayer(Coordinates.fromRowAndValue(0,0))
        }

        assertThrows<IllegalArgumentException>("Can not move active player to (1,1)") {
            referee.moveActivePlayer(Coordinates.fromRowAndValue(1,1))
        }
    }

    @Test
    fun testMovePlayer() {
        val tiles = TestData.createTiles()
        val referee = TestData.createReferee(tiles)

        referee.moveActivePlayer(Coordinates.fromRowAndValue(1, 0))

        assertEquals(setOf(TestData.createPlayer1()), tiles[1][0].getPlayersOnTile())

        referee.moveActivePlayer(Coordinates.fromRowAndValue(0, 1))

        assertEquals(setOf(TestData.createPlayer2()), tiles[0][1].getPlayersOnTile())
    }


    @Test
    fun testMovePlayerToTheSameTile() {
        val referee = TestData.createReferee()

        assertThrows<IllegalArgumentException>("Can not move active player to (0,0).") {
            referee.moveActivePlayer(Coordinates.fromRowAndValue(0, 0))
        }
    }

    @Test
    fun testMovePlayerToTreasureTile() {
        val player = TestData.createPlayer1()
        val referee = TestData.createRefereeWithOnePlayer(player, Coordinates.fromRowAndValue(1,0))

        referee.moveActivePlayer(Coordinates.fromRowAndValue(0,0))

        assert(player.treasureFound)
    }

    @Test
    fun testSlideHorizontalSlideVertical() {
        val tiles = TestData.createTiles()
        val spareTile = TestData.createSpareTile()
        val board = Board(tiles)
        val referee = Referee(board, spareTile, listOf())

        val tileAtEndOfRow = tiles[2][6]
        val expectedNewRow = arrayOf(
            spareTile, tiles[2][0], tiles[2][1], tiles[2][2], tiles[2][3], tiles[2][4], tiles[2][5]
        )

        referee.slideRowAndInsertSpare(RowPosition(2), HorizontalDirection.RIGHT, Degree.ZERO)
        Assertions.assertArrayEquals(expectedNewRow, tiles[2])

        val expectedNewCol = arrayOf(
            tiles[1][6], tiles[2][6], tiles[3][6], tiles[4][6], tiles[5][6], tiles[6][6], tileAtEndOfRow
        )
        referee.slideColumnAndInsertSpare(ColumnPosition(6), VerticalDirection.UP, Degree.ZERO)

        Assertions.assertArrayEquals(expectedNewCol, tiles.map { it[6] }.toTypedArray())
    }

    @Test
    fun tesSlideAndInsertPlayerDislodged() {
        val tiles = TestData.createTiles()
        val spareTileToBeInserted = TestData.createSpareTile()
        val board = Board(tiles)
        val player = TestData.createPlayer3()
        tiles[6][4].addPlayerToTile(player)
        val referee = Referee(board, spareTileToBeInserted, listOf(player))

        referee.slideColumnAndInsertSpare(ColumnPosition(4), VerticalDirection.DOWN, Degree.ZERO)

        assertEquals(setOf(player), spareTileToBeInserted.getPlayersOnTile())
        assertEquals(setOf(), tiles[6][4].getPlayersOnTile())

    }

    @Test
    fun testKickoutActivePlayer() {
        val referee = TestData.createReferee()

        referee.kickOutActivePlayer()
        assert(false)
    }

    @Test
    fun testInsertRotate() {
        assert(false)
    }

    @Test
    fun testPassCurrentPlayer() {
        assert(false)
    }
}