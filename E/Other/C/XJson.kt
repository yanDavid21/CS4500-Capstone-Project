package Other.C

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonArray
import java.io.Reader

/**
 * Reads JSON objects from a reader.
 * Converts every JSON object into its corresponding CardinalCharacter.
 */
fun readFromInputStream(input: Reader): JsonArray {
    val cardinalCharacters = mutableListOf<CardinalCharacters>()

    val text = input.readText()

    val split = text.split("{")
    for (segment in split.subList(1, split.size)) {
        cardinalCharacters.add(
            Json.decodeFromString("{$segment")
        )
    }

    return cardinalCharacters.mapToJSONArray()
}

/**
 * Accepts a series of well-formed and valid json objects.
 * Returns a JSONArray of each object's cardinal character.
 */
fun List<CardinalCharacters>.mapToJSONArray(): JsonArray {
    val outputList = this.map(::cardinalCharacterToString)
    
    return Json.encodeToJsonElement(outputList).jsonArray
}

/**
 * Converts a CardinalCharacter to its string representation. Throws an error if not one of the valid enumerations of
 * CardinalCharacter.
 */
fun cardinalCharacterToString(cardinalChar: CardinalCharacters): String {
    return when(cardinalChar) {
        CardinalCharacters(Horizontal.LEFT, Vertical.UP) -> "┘"
        CardinalCharacters(Horizontal.RIGHT, Vertical.UP) -> "└"
        CardinalCharacters(Horizontal.LEFT, Vertical.DOWN) -> "┐"
        CardinalCharacters(Horizontal.RIGHT, Vertical.DOWN) -> "┌"
        else -> throw IllegalStateException("Can not reach this branch.")
    }
}

