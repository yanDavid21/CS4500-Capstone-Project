package Referee

data class WinningPlayers(
    val playerNames: Set<String>
) {
    private val outcomes = playerNames.associate { player -> Pair(player, false) }




}
