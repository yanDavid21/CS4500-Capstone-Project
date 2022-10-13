package Common.tile.treasure

data class Treasure(val gem1: Gem, val gem2: Gem) {

    override fun equals(other: Any?): Boolean {
        if (other is Treasure) {
            return (other.gem1 == gem1 && other.gem2 == gem2) || (other.gem1 == gem2 && other.gem2 == gem1)
        }
        return false
    }

    override fun hashCode(): Int {
        return gem1.hashCode() + gem2.hashCode()
    }
}