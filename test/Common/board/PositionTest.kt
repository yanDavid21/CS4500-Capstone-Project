package Common.board

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
internal class PositionTest {
    /**
     * Need to test get min and max, and checkbounds
     */

    @Test
    fun testGetMin() {
        // ARRANGE
        val rowPosition = RowPosition(4)
        val colPosition = RowPosition(4)
        // ACT
        val getMinRow = rowPosition.min
        val getMinCol = colPosition.min
        // ASSERT
        assertEquals(getMinRow, 0)
        assertEquals(getMinCol, 0)
    }

    @Test
    fun testGetMax() {
        // ARRANGE
        val rowPosition = RowPosition(4)
        val colPosition = RowPosition(4)
        // ACT
        val getMaxRow = rowPosition.max
        val getMaxCol = colPosition.max
        // ASSERT
        assertEquals(getMaxRow, 6)
        assertEquals(getMaxCol, 6)
    }

    @Test
    fun testCheckBounds() {
        // ASSERT
        assertDoesNotThrow {
            RowPosition(0)
            RowPosition(5)
            RowPosition(6)
            ColumnPosition(0)
            ColumnPosition(5)
            ColumnPosition(6)
        }
        assertThrows<IllegalArgumentException> { RowPosition(8) }
        assertThrows<IllegalArgumentException> { RowPosition(7) }
        assertThrows<IllegalArgumentException> { ColumnPosition(-1) }
        assertThrows<IllegalArgumentException> { ColumnPosition(-1) }
    }
}