package Common.referee

import Common.TestData
import Common.board.Coordinates
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

        assertEquals(setOf(TestData.player1), tiles[1][0].getPlayers())

        referee.moveActivePlayer(Coordinates.fromRowAndValue(0, 1))

        assertEquals(setOf(TestData.player2), tiles[0][1].getPlayers())
    }


    @Test
    fun testMovePlayerToTheSameTile() {
        val referee = TestData.createReferee()

        assertThrows<IllegalArgumentException>("Can not move active player to (0,0).") {
            referee.moveActivePlayer(Coordinates.fromRowAndValue(0, 0))
        }
    }

    @Test
    fun testMovePlayerToGoalTile() {

    }

    @Test
    fun testMovePlayerToTreasureTile() {

    }

    @Test
    fun testSlideHorizontalSlideVertical() {

    }

    @Test
    fun tesSlideAndInsertPlayerDislodged() {

    }

    @Test
    fun testKickoutActivePlayer() {
        val referee = TestData.createReferee()
        referee.kickOutActivePlayer()

    }

}