package Client.javafx


import Common.PublicGameState
import Common.board.Board
import Common.board.ColumnPosition
import Common.board.Coordinates
import Common.board.RowPosition
import Common.player.BaseColor
import Common.player.HexColor
import Common.tile.GameTile
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.layout.HBox
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.shape.Rectangle
import javafx.scene.text.Font
import javafx.scene.text.Text
import javafx.scene.text.TextAlignment
import testing.TestUtils

/**
 * Draws a public game state. Including all the tiles with their gems, players and homes (if applicable) and
 * a spare tile next to it.
 */
fun renderGameState(gameState: PublicGameState): Pair<VBox, StackPane> {
    val board = gameState.board
    val playerDataMap = gameState.publicPlayerData
    val playerDataMapCurrentPosition = playerDataMap.mapValues { (_, value) -> Pair(value.currentPosition, value.color) }
    val playerDataMapHomePosition = playerDataMap.mapValues { (_, value) -> Pair(value.homePosition, value.color) }

    val boardImage = drawBoard(board, mapPositionToPlayerColors(playerDataMapCurrentPosition),
        mapPositionToPlayerColors(playerDataMapHomePosition))

    val spareTileImage = drawTile(gameState.spareTile, null, mapOf(), mapOf())

    return Pair(boardImage, spareTileImage)
}

private fun mapPositionToPlayerColors(playerDataMap: Map<String, Pair<Coordinates, Common.player.Color>>):  Map<Coordinates, MutableList<Color>>{
    val coordsToColors = mutableMapOf<Coordinates, MutableList<Color>>()

    playerDataMap.values.forEach { (coords, color) ->
        if (coordsToColors.containsKey(coords)) {
            coordsToColors[coords]?.add(javaFxColorFromColor(color))
        } else {
            coordsToColors[coords] = mutableListOf(javaFxColorFromColor(color))
        }
    }
    return coordsToColors
}

private fun javaFxColorFromColor(color: Common.player.Color): Color {
    return when(color) {
        is HexColor -> throw NotImplementedError("error")
        BaseColor.RED -> Color.RED
        BaseColor.BLUE -> Color.BLUE
        BaseColor.PURPLE -> Color.PURPLE
        BaseColor.YELLOW -> Color.YELLOW
        BaseColor.PINK -> Color.PINK
        BaseColor.WHITE -> Color.WHITE
        BaseColor.GREEN -> Color.GREEN
        BaseColor.ORANGE -> Color.ORANGE
        BaseColor.BLACK -> Color.BLACK
    }
}


private fun drawBoard(board: Board, playerPosToColor: Map<Coordinates, List<Color>>,
                      playerHomeToColor: Map<Coordinates, List<Color>>): VBox {
    return VBox().apply {
        RowPosition.getAll().forEach { rowPosition ->
            val row = drawRow(TestUtils.getTilesInRow(rowPosition.value, board), rowPosition,
                playerPosToColor, playerHomeToColor)
            children.add(row)
        }
    }
}

private fun drawRow(tiles: Array<GameTile>, rowPosition: RowPosition,  playerPosToColor: Map<Coordinates, List<Color>>
                    ,playerHomeToColor: Map<Coordinates, List<Color>>): Node {
    return HBox().apply {
        tiles.forEachIndexed { index, tile ->
            children.add(drawTile(tile, Coordinates(rowPosition, ColumnPosition(index)), playerPosToColor, playerHomeToColor)) }
    }
}

fun drawTile(tile: GameTile, tileCoord: Coordinates?, playerColorAndPos: Map<Coordinates, List<Color>>,
             playerHomeToColor: Map<Coordinates, List<Color>>): StackPane {
    val TILE_WIDTH = 100.0
    val TILE_HEIGHT = 100.0
    val BORDER_WIDTH = 5.0
    val stack = StackPane()
    val base = Rectangle()
    base.width = TILE_WIDTH
    base.height = TILE_HEIGHT


    // border
    playerHomeToColor[tileCoord]?.let {
        val background = Rectangle()
        background.fill = Color.WHITE
        background.width = TILE_WIDTH - BORDER_WIDTH
        background.height = TILE_HEIGHT - BORDER_WIDTH
        base.fill = it[0]
        stack.children.addAll(base, background)
    } ?: run {
        base.fill = Color.WHITE
        stack.children.add(base)
    }


    val text = Text()
    text.text = tile.toString()
    text.font = Font.font(75.0)
    text.fill = Color.BLACK
    text.textAlignment = TextAlignment.CENTER

    stack.children.add(text)
    stack.alignment = Pos.CENTER

    playerColorAndPos[tileCoord]?.let {
        val initCircleSize = 25.0
        val maxDifference = 5.0
        var circleSize = initCircleSize
        val difference = (initCircleSize / it.size).coerceAtMost(maxDifference)
        it.forEach { color ->
            val icon = Circle(circleSize)
            icon.fill = color
            stack.children.add(icon)
            circleSize -= difference
        }
    }
    return stack
}