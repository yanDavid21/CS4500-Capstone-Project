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
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken



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

fun decodeJSONFromStream(reader: Reader, writer: Writer) {
    val jsonReader = JsonReader(reader)
    return try {
        while (jsonReader.hasNext()) {
            val jsonObject: CardinalCharacters? = readMessage(jsonReader)
            jsonObject?.let {
                writer.write(cardinalCharacterToString(it))
                writer.flush()
            }
        }
    } finally {
        reader.close()
    }
}

fun readMessage(jsonReader: JsonReader):CardinalCharacters? {
    val vertical:Vertical? = null
    val horizontal:Horizontal? = null

    reader.beginObject()
    while (reader.hasNext()) {
        val name: String = reader.nextName()
        when (name) {
            "HORIZONTAL" -> {
                horizontal = reader.nextString()
            }
            "VERTICAL" -> {
                vertical = reader.nextString()
            }
            else -> {
                reader.skipValue()
            }
        }
    }
    reader.endObject()
    return vertical?.let {
        hortizontal?.let {
            return CardinalCharacters(vertical, horizontal)
        }
    }
}

/**
 * Reads JSON objects from STDIN, prints to STDOUT.
 */
fun main(args: Array<String>) {

    decodeJSONFromStream(java.io.InputStreamReader(System.`in`, "UTF-8"), OutputStreamWriter(java.lang.System.out))

}




