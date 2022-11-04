package Client.javafx


import Common.GameState
import Common.board.Board
import Common.board.ColumnPosition
import Common.board.Coordinates
import Common.board.RowPosition
import Common.player.BaseColor
import Common.player.HexColor
import Common.tile.GameTile
import Common.tile.treasure.Gem
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.image.Image
import javafx.scene.image.ImageView
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

const val TILE_WIDTH = 100.0
const val TILE_HEIGHT = 100.0
const val IMAGE_WIDTH = TILE_WIDTH / 5
const val BORDER_WIDTH = 5.0
const val BASE_TILE_PADDING = 1.0

/**
 * Draws a public game state. Including all the tiles with their gems, players and homes (if applicable) and
 * a spare tile next to it.
 */
fun renderGameState(gameState: GameState): Pair<VBox, StackPane> {
    val board = gameState.getBoard()
    val playerDataMap = gameState.getPlayersData()
    val playerDataMapCurrentPosition = playerDataMap.mapValues { (_, value) -> Pair(value.currentPosition, value.color) }
    val playerDataMapHomePosition = playerDataMap.mapValues { (_, value) -> Pair(value.homePosition, value.color) }

    val boardImage = drawBoard(board, mapPositionToPlayerColors(playerDataMapCurrentPosition),
        mapPositionToPlayerColors(playerDataMapHomePosition))

    val spareTileImage = drawTile(gameState.toPublicState().spareTile, null, mapOf(), mapOf())

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

<<<<<<< HEAD
fun drawTile(tile: GameTile, tileCoord: Coordinates?, playerColorAndPos: Map<Coordinates, List<Color>>,
=======
/**
 * Draws a single tile with the avatars and/or homes on it.
 */
private fun drawTile(tile: GameTile, tileCoord: Coordinates?, playerColorAndPos: Map<Coordinates, List<Color>>,
>>>>>>> 751654e... 6.6 - Finished gui, spare tile + cleaned up with purpose statements
             playerHomeToColor: Map<Coordinates, List<Color>>): StackPane {
    val stack = StackPane()
    stack.padding = Insets(BASE_TILE_PADDING, BASE_TILE_PADDING, BASE_TILE_PADDING, BASE_TILE_PADDING)
    stack.alignment = Pos.CENTER
    val base = getBaseTile()
    val gemOneStack = getImageFromGem(tile.treasure.gem1, Pos.TOP_LEFT)
    val gemTwoStack = getImageFromGem(tile.treasure.gem2, Pos.BOTTOM_RIGHT)

    // border if there is a home
    playerHomeToColor[tileCoord]?.let {
        val background = getBackgroundOfTileForBorder()
        base.fill = it[0]
        stack.children.addAll(base, background)
    } ?: run {
        base.fill = Color.WHITE
        stack.children.add(base)
    }

    val text = getPathAsTextComponent(tile)
    stack.children.add(text)
    playerColorAndPos[tileCoord]?.let {
        addPlayerIconsToTile(it, stack)
    }
    return StackPane(stack, gemOneStack, gemTwoStack)
}

/**
 * Gets the image of the given gem aligned in certain position.
 */
private fun getImageFromGem(gem: Gem, pos: Pos): StackPane {
    val gemImage = Image("gems/${gem.getImagePath()}",
        IMAGE_WIDTH, IMAGE_WIDTH, true, false)
    val gemStack = StackPane(ImageView(gemImage))
    gemStack.alignment = pos
    gemStack.maxHeight = TILE_HEIGHT
    gemStack.maxWidth = TILE_WIDTH
    return gemStack
}

/**
 * Gets the base rectangle to build the tile off of.
 */
private fun getBaseTile(): Rectangle {
    val base = Rectangle()
    base.width = TILE_WIDTH
    base.height = TILE_HEIGHT
    return base
}

/**
 * Gets a plain white background used in junction with another square to create a pseudo-border.
 */
private fun getBackgroundOfTileForBorder(): Rectangle {
    val background = Rectangle()
    background.fill = Color.WHITE
    background.width = TILE_WIDTH - BORDER_WIDTH
    background.height = TILE_HEIGHT - BORDER_WIDTH
    return background
}

/**
 * Returns a text component rendering of the given tile.
 */
private fun getPathAsTextComponent(tile: GameTile): Text {
    val text = Text()
    text.text = tile.toString()
    text.font = Font.font(75.0)
    text.fill = Color.BLACK
    text.textAlignment = TextAlignment.CENTER
    return text
}

/**
 * Given a stackpane, adds an icon for every color in the supplied list of colors
 */
private fun addPlayerIconsToTile(playerColors: List<Color>, stack: StackPane) {
    val initCircleSize = 25.0
    val maxDifference = 5.0
    var circleSize = initCircleSize
    val difference = (initCircleSize / playerColors.size).coerceAtMost(maxDifference)
    playerColors.forEach { color ->
        val icon = Circle(circleSize)
        icon.fill = color
        stack.children.add(icon)
        circleSize -= difference
    }
}