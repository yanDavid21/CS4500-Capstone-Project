/*
init {
        /*
        1. Create 50 random tiles
        2. Select one to be the spareTile
        3. Make tiles out of the remaining 49,
        4. To create players, select n unmovable tiles, n other random tiles (for treasures)
            make players with them
         */
        val gems = Gem.values().toMutableList()
        gems.shuffle()
        val tiles = Array(7) { rowIndex ->
            Array(7) { colIndex ->
                {
                    val firstGem = gems[()]
                }
            }
        }

        for (tileIndex in 0 until 50) {
            val firstGem = gems[tileIndex * 2]
            val secondGem = gems[tileIndex * 2 + 1]
            val treasure = Treasure(firstGem, secondGem)

            val tile = GameTile(randomPath(), randomDegree(),  treasure)
        }



        for (gemIndex in gems.indices) {
            val firstGem = gems[gemIndex]
            val secondGem = gems[gemIndex + 1]
        }

        if (playerIDs.isEmpty()) {
            throw IllegalArgumentException("Can not have 0 players! No fun.")
        }
        players = playerIDs.map { createPlayer(it)}.toSet()
        currentPlayer = players.first()
    }

    private fun randomPath(): Path {

    }

    private fun randomDegree(): Degree {

    }

 */