package C.Other

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

fun main(args: Array<String>) {
    println(Json.decodeFromString<Map<String, String>>("{\"a\":\"b\"}"))
}




