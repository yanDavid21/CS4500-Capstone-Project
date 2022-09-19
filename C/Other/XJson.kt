package C.Other

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonArray
import java.io.InputStreamReader


/**
 * A JSON representation of characters, contains horizontal and vertical information.
 */
@Serializable
data class CardinalCharacters(
    val horizontal: Horizontal,
    val vertical: Vertical,
)

@Serializable
enum class Horizontal {
    LEFT, RIGHT
}

@Serializable
enum class Vertical {
    UP, DOWN
}

/**
 * Accepts a series of well-formed and valid json objects.  For each object, computes the corresponding cardinal
 * character prints out a JSON array of this collection
 */
fun jsonToAcceptableCharacter(json: List<CardinalCharacters>): JsonArray {
    val outputList = mutableListOf<String>()

    json.forEach {
        outputList.add(when(it) {
            CardinalCharacters(Horizontal.LEFT, Vertical.UP) -> "┘"
            CardinalCharacters(Horizontal.RIGHT, Vertical.UP) -> "└"
            CardinalCharacters(Horizontal.LEFT, Vertical.DOWN) -> "┐"
            CardinalCharacters(Horizontal.RIGHT, Vertical.DOWN) -> "┌"
            else -> throw IllegalStateException("Can not reach this branch.")
        })
    }

    return Json.encodeToJsonElement(outputList).jsonArray
}

/**
 * Reads JSON objects from STDIN, prints to STDOUT.
 */
fun main(args: Array<String>) {

    val cardinalCharacters = mutableListOf<CardinalCharacters>()

    val text = InputStreamReader(System.`in`).readText()

    val split = text.split("{")
    for (segment in split.subList(1, split.size)) {
        cardinalCharacters.add(
            Json.decodeFromString("{$segment")
        )
    }

    println(jsonToAcceptableCharacter(cardinalCharacters))
}




