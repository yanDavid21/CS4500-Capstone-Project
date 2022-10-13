package Common.tile.treasure

import org.junit.jupiter.api.Test

internal class TreasureTest {

    @Test
    fun testTreasureUnordered() {
        val g1 = Gem.APATITE
        val g2 = Gem.HEMATITE

        val treasure1 = Treasure(g1, g2)
        val treasure1Rev = Treasure(g1, g2)

        assert(treasure1 == treasure1Rev)
    }

    @Test
    fun testTreasureEqual() {
        val g1 = Gem.APATITE
        val g2 = Gem.HEMATITE
        val g3 = Gem.APLITE

        assert(Treasure(g1, g2) != Treasure(g2, g3))
        assert(Treasure(g1, g2) == Treasure(g2, g1))
        assert(Treasure(g3, g1) == Treasure(g1, g3))
    }

}