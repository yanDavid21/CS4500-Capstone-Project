package Common.referee

import Common.Referee
import Common.TestData
import Common.board.Board
import Common.board.ColumnPosition
import Common.board.Coordinates
import Common.board.RowPosition
import Common.tile.Degree
import Common.tile.GameTile
import Common.tile.HorizontalDirection
import Common.tile.VerticalDirection
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import testing.TestUtils
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
        val player1 = TestData.createPlayer1()
        val player2 = TestData.createPlayer2()
        val board = TestData.createBoard()
        val referee = Referee(board, TestData.createSpareTile(), listOf(player1, player2))

        referee.moveActivePlayer(Coordinates.fromRowAndValue(1, 0))

        assertEquals(Coordinates.fromRowAndValue(1,0), player1.currentPosition)

        referee.moveActivePlayer(Coordinates.fromRowAndValue(0, 1))

        assertEquals(Coordinates.fromRowAndValue(0, 1), player2.currentPosition)
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
        val referee = TestData.createRefereeWithOnePlayer(player)

        referee.moveActivePlayer(Coordinates.fromRowAndValue(1,0))

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
        val newBoard = referee.getBoard()
        Assertions.assertArrayEquals(expectedNewRow, TestUtils.getTilesInRow(2, newBoard))

        val expectedNewCol = arrayOf(
            tiles[1][6], tiles[2][5], tiles[3][6], tiles[4][6], tiles[5][6], tiles[6][6], tileAtEndOfRow
        )
        referee.slideColumnAndInsertSpare(ColumnPosition(6), VerticalDirection.UP, Degree.ZERO)

        Assertions.assertArrayEquals(expectedNewCol, TestUtils.getTilesInCol(6, referee.getBoard()))
    }

    @Test
    fun tesSlideAndInsertPlayerDislodged() {
        val tiles = TestData.createTiles()
        val spareTileToBeInserted = TestData.createSpareTile()
        val board = Board(tiles)
        val player = TestData.createPlayer3()

        val referee = Referee(board, spareTileToBeInserted, listOf(player))

        referee.slideColumnAndInsertSpare(ColumnPosition(6), VerticalDirection.DOWN, Degree.ZERO)

        assertEquals(Coordinates.fromRowAndValue(0, 6), player.currentPosition)

    }

    @Test
    fun testKickoutActivePlayer() {
        val player1 = TestData.createPlayer1()
        val player2 = TestData.createPlayer2()
        val player3 = TestData.createPlayer3()

        val referee = Referee(TestData.createBoard(), TestData.createSpareTile(), listOf(player1, player2, player3))

        // kick out player1
        referee.kickOutActivePlayer()

        referee.moveActivePlayer(Coordinates.fromRowAndValue(0, 1))
        assertEquals(Coordinates.fromRowAndValue(0, 1), player2.currentPosition)

        // kick out player 3, player 2 can be move again
        referee.kickOutActivePlayer()

        referee.moveActivePlayer(Coordinates.fromRowAndValue(0, 2))
        assertEquals(Coordinates.fromRowAndValue(0, 2), player2.currentPosition)
    }

    @Test
    fun testKickoutLastPlayer() {
        //TODO: assertTrue(ask matthias in class === get fucked publicly)
    }

    @Test
    fun testInsertRotateZeroDegrees() {
        val player1 = TestData.createPlayer1()
        val player2 = TestData.createPlayer2()
        val player3 = TestData.createPlayer3()

        val referee = Referee(TestData.createBoard(), TestData.createSpareTile(), listOf(player1, player2, player3))
        referee.slideRowAndInsertSpare(RowPosition(0), HorizontalDirection.RIGHT, Degree.ZERO)
        val tile = referee.getBoard().getTile(Coordinates(RowPosition(0), ColumnPosition(0)))
        assertEquals(tile, GameTile(tile.path, Degree.ZERO, tile.treasure))
    }


    @Test
    fun testInsertRotateNinetyDegrees() {
        val player1 = TestData.createPlayer1()
        val player2 = TestData.createPlayer2()
        val player3 = TestData.createPlayer3()

        val referee = Referee(TestData.createBoard(), TestData.createSpareTile(), listOf(player1, player2, player3))
        referee.slideRowAndInsertSpare(RowPosition(0), HorizontalDirection.RIGHT, Degree.NINETY)
        val tile = referee.getBoard().getTile(Coordinates(RowPosition(0), ColumnPosition(0)))
        assertEquals(tile, GameTile(tile.path, Degree.NINETY, tile.treasure))
    }



    @Test
    fun testInsertRotateOneEightyDegrees() {
        val player1 = TestData.createPlayer1()
        val player2 = TestData.createPlayer2()
        val player3 = TestData.createPlayer3()

        val referee = Referee(TestData.createBoard(), TestData.createSpareTile(), listOf(player1, player2, player3))
        referee.slideRowAndInsertSpare(RowPosition(0), HorizontalDirection.RIGHT, Degree.ONE_EIGHTY)
        val tile = referee.getBoard().getTile(Coordinates(RowPosition(0), ColumnPosition(0)))
        assertEquals(tile, GameTile(tile.path, Degree.ONE_EIGHTY, tile.treasure))
    }


    @Test
    fun testInsertRotateTwoSeventyDegrees() {
        val player1 = TestData.createPlayer1()
        val player2 = TestData.createPlayer2()
        val player3 = TestData.createPlayer3()

        val referee = Referee(TestData.createBoard(), TestData.createSpareTile(), listOf(player1, player2, player3))
        referee.slideRowAndInsertSpare(RowPosition(0), HorizontalDirection.RIGHT, Degree.TWO_SEVENTY)
        val tile = referee.getBoard().getTile(Coordinates(RowPosition(0), ColumnPosition(0)))
        assertEquals(tile, GameTile(tile.path, Degree.TWO_SEVENTY, tile.treasure))
    }


    @Test
    fun testInsertRotateThreeSixtyDegrees() {
        val player1 = TestData.createPlayer1()
        val player2 = TestData.createPlayer2()
        val player3 = TestData.createPlayer3()

        val referee = Referee(TestData.createBoard(), TestData.createSpareTile(), listOf(player1, player2, player3))
        referee.slideRowAndInsertSpare(RowPosition(0), HorizontalDirection.RIGHT, Degree.ZERO.add(Degree.TWO_SEVENTY).add(Degree.NINETY))
        val tile = referee.getBoard().getTile(Coordinates(RowPosition(0), ColumnPosition(0)))
        assertEquals(tile, GameTile(tile.path, Degree.ZERO, tile.treasure))
    }

    @Test
    fun testPassCurrentPlayer() {
        val player1 = TestData.createPlayer1()
        val player2 = TestData.createPlayer2()

        val referee = Referee(TestData.createBoard(), TestData.createSpareTile(), listOf(player1, player2))

        referee.passCurrentPlayer()

        referee.moveActivePlayer(Coordinates.fromRowAndValue(0, 1))
        assertEquals(Coordinates.fromRowAndValue(0, 1),  player2.currentPosition)

        referee.passCurrentPlayer()
        referee.moveActivePlayer(Coordinates.fromRowAndValue(0,2))
        assertEquals(Coordinates.fromRowAndValue(0,2), player2.currentPosition)
    }

    @Test
    fun testCannotUndoLastAction() {
        assert(false)
    }
}