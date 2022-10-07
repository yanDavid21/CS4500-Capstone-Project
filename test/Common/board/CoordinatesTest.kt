package Common.board

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

internal class CoordinatesTest {

    private val minX = Position.MIN_X_INDEX
    private val maxX = Position.MAX_X_INDEX
    private val minY = Position.MIN_Y_INDEX
    private val maxY = Position.MAX_Y_INDEX

    private val xInterval = "[$minX, $maxX]"
    private val yInterval = "[$minY, $maxY]"

    private val someRowPosition = RowPosition(2)
    private val someColPosition = ColumnPosition(3)
    private val someCoordinate = Coordinates(someRowPosition, someColPosition)

    @Test
    fun testCreateRowPosition() {
        val rowPosition = RowPosition(3)
        assertEquals(3, rowPosition.value)
    }

    @Test
    fun testCreateInvalidRowPosition() {
        assertThrows<IllegalArgumentException>("Position should be in the interval $yInterval, given ${maxY + 2}.") {
            RowPosition(Position.MAX_Y_INDEX + 2)
        }

        assertThrows<IllegalArgumentException>("Position should be in the interval $yInterval, given ${minY - 2}.") {
            RowPosition(Position.MIN_Y_INDEX - 2)
        }
    }

    @Test
    fun testCreateColPosition() {
        val colPosition = ColumnPosition(3)
        assertEquals(3, colPosition.value)
    }

    @Test
    fun testCreateInvalidColumn() {
        assertThrows<IllegalArgumentException>("Position should be in the interval $xInterval, given ${maxX + 2}.") {
            RowPosition(Position.MAX_X_INDEX + 2)
        }

        assertThrows<IllegalArgumentException>("Position should be in the interval $xInterval, given ${minX - 3}.") {
            RowPosition(Position.MIN_X_INDEX - 3)
        }
    }

    @Test
    fun testCopyWithNewRow() {
        assertEquals(Coordinates(RowPosition(minY), someColPosition), someCoordinate.copyWithNewRow(minY))
    }

    @Test
    fun testCopyWithNewCol() {
        assertEquals(Coordinates(someRowPosition, ColumnPosition(maxX)), someCoordinate.copyWithNewCol(maxX))
    }

    @Test
    fun testCopyWithNewColInvalid() {
        assertThrows<java.lang.IllegalArgumentException> { someCoordinate.copyWithNewCol(-5)  }
        assertThrows<java.lang.IllegalArgumentException> { someCoordinate.copyWithNewCol(maxX + 10)  }
    }

    @Test
    fun testCopyWithNewRowInvalid() {
        assertThrows<java.lang.IllegalArgumentException> { someCoordinate.copyWithNewRow(-5)  }
        assertThrows<java.lang.IllegalArgumentException> { someCoordinate.copyWithNewRow(maxY + 10)  }
    }
}