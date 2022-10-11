package Common


enum class GameState {
    SLIDE, INSERT, MOVE;

    companion object {
        val INITIAL = SLIDE
    }
}