package Common


enum class GameState {
    SLIDE_INSERT, MOVE;

    companion object {
        val INITIAL = SLIDE_INSERT
    }
}