package Common.tile

data class Treasure(val gem1: Gem, val gem2: Gem) {

    init {
        if (gem1 == gem2) {
            throw IllegalStateException("Treasures must be a pair of unique gems.")
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other is Treasure) {
            return (other.gem1 == gem1 || other.gem1 == gem2) && (other.gem2 == gem1 || other.gem2 == gem2)
        }
        return false
    }

    override fun hashCode(): Int {
        return gem1.hashCode() + gem2.hashCode()
    }

}