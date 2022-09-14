package C.Other


import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import java.io.InputStreamReader
import java.util.*

enum class Vertical { UP, DOWN }
enum class Horizontal { LEFT, RIGHT }

@kotlinx.serialization.Serializable
data class CharacterAsJson(val vertical: Vertical, val horizontal: Horizontal)

enum class AcceptableCharacter(val symbol: String) {
    LEFT_UP("┘"), LEFT_DOWN("┐"), RIGHT_UP("└"), RIGHT_DOWN("┌");

    companion object {
        fun fromJson(json: CharacterAsJson): String {
            return when (json) {
                CharacterAsJson(Vertical.DOWN, Horizontal.LEFT) -> LEFT_DOWN
                CharacterAsJson(Vertical.DOWN, Horizontal.RIGHT) -> RIGHT_DOWN
                CharacterAsJson(Vertical.UP, Horizontal.LEFT) -> LEFT_UP
                CharacterAsJson(Vertical.UP, Horizontal.RIGHT) -> RIGHT_UP
                else -> throw IllegalArgumentException("Can not reach this branch.")
            }.symbol
        }
    }
}

object XJson {

    fun collectJsonSeries(input: Readable, output: Appendable) {
        val scanner = Scanner(input)

        val collectedStrings = ArrayList<String>()

        while (scanner.hasNextLine()) {
            val line = scanner.nextLine()

            val characterAsJson = Json.decodeFromString<CharacterAsJson>(line)
            val acceptableCharacter = AcceptableCharacter.fromJson(characterAsJson)
            collectedStrings.add(acceptableCharacter)
        }

        output.append(
            Json.encodeToJsonElement(collectedStrings).toString()
        )
    }
}

fun main(args: Array<String>) {
    XJson.collectJsonSeries(InputStreamReader(System.`in`), System.out)
}




