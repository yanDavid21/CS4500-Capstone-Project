package Players

import Common.RowAction
import Common.TestData
import Common.board.Coordinates
import Common.board.RowPosition
import Common.player.Player
import Common.tile.Degree
import Common.tile.HorizontalDirection
import Common.tile.Path
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals


internal class EuclidTest: AbstractOrderingStrategyTests() {

    override fun createStrategy(player: Player): MazeStrategy {
        return Euclid(player)
    }

    @Test
    fun testGetToClosestToTreasureWhenTreasureNotReachable() {
        val move = getMoveWithPlayerAtPosition(
            playerPosition = Coordinates.fromRowAndValue(1, 1),
            treasurePosition = Coordinates.fromRowAndValue(5, 5),
            homePosition = Coordinates.fromRowAndValue(5, 5),
            makeBoard(),
            TestData.createSpareTile(Path.VERTICAL),
            null
        )

        val expected = RowAction(
            RowPosition(4),
            HorizontalDirection.RIGHT,
            Degree.ZERO,
            Coordinates.fromRowAndValue(4, 5)
        )

        assertEquals(expected, move)
    }

}