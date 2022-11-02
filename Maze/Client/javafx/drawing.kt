package Client.javafx

import Common.GameState
import Common.board.Board
import Common.board.ColumnPosition
import Common.board.Coordinates
import Common.board.RowPosition
import Common.player.BaseColor
import Common.player.HexColor
import Common.tile.GameTile
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.layout.HBox
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.shape.Rectangle
import javafx.scene.text.Font
import javafx.scene.text.Text
import testing.TestUtils

fun renderGameState(gameState: GameState): Parent {
    val board = gameState.getBoard()
    val playerData = gameState.getPlayersData()

    val boardImage = drawBoard(board, playerData
        .mapKeys { (_, p) -> p.currentPosition }
        .mapValues { (_, p) -> javaFxColorFromColor(p.color) })

    val spareTileImage = drawTile(gameState.toPublicState().spareTile, null, mapOf())

    return HBox().apply{
        children.add(boardImage)

        children.add(spareTileImage)
    }
}

fun javaFxColorFromColor(color: Common.player.Color): Color {
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


private fun drawBoard(board: Board, payerColorAndPos: Map<Coordinates, Color>): Parent {
    return VBox().apply {
        RowPosition.getAll().forEach { rowPosition ->
            val row = drawRow(TestUtils.getTilesInRow(rowPosition.value, board), rowPosition, payerColorAndPos)
            children.add(row)
        }
    }
}

private fun drawRow(tiles: Array<GameTile>, rowPosition: RowPosition,  payerColorAndPos: Map<Coordinates, Color>): Node {
    return HBox().apply {
        tiles.forEachIndexed { index, tile ->
            children.add(drawTile(tile, Coordinates(rowPosition, ColumnPosition(index)), payerColorAndPos)) }
    }
}

fun drawTile(tile: GameTile, pos: Coordinates?, playerColorAndPos: Map<Coordinates, Color>?): Node {
    val stack = StackPane()
    val background = Rectangle()
    background.fill = Color.WHITE
    background.width = 100.0
    background.height = 100.0

    val text = Text()
    text.text = tile.toString()
    text.font = Font.font(100.0)
    text.fill = Color.BLACK

    stack.children.addAll(background, text)

    playerColorAndPos?.get(pos)?.let {
        val icon = Circle(25.0)
        icon.fill = it
        stack.children.add(icon)
    }
    return stack
}