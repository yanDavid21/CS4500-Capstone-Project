package C.Other

import Other.CardinalCharacters
import Other.Horizontal
import Other.Vertical
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonArray
import java.io.InputStreamReader

/**
 * Converts a CardinalCharacter to its string representation. Throws an error if not one of the valid enumerations of
 * CardinalCharacter.
 */
fun cardinalCharacterToString(cardinalChar: CardinalCharacters):String {
    return when(cardinalChar) {
        CardinalCharacters(Horizontal.LEFT, Vertical.UP) -> "┘"
        CardinalCharacters(Horizontal.RIGHT, Vertical.UP) -> "└"
        CardinalCharacters(Horizontal.LEFT, Vertical.DOWN) -> "┐"
        CardinalCharacters(Horizontal.RIGHT, Vertical.DOWN) -> "┌"
        else -> throw IllegalStateException("Can not reach this branch.")
    }
}

/**
 * Accepts a series of well-formed and valid json objects.  For each object, computes the corresponding cardinal
 * character prints out a JSON array of this collection.
 */
fun List<CardinalCharacters>.mapToJSONArray(json: List<CardinalCharacters>): JsonArray {
    val outputList = mutableListOf<String>()

    json.forEach {
        outputList.add(cardinalCharacterToString(it))
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

    println(cardinalCharacters.mapToJSONArray)
}




