package testing

import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import java.io.InputStreamReader
import java.io.Reader

data class BoardObj(
    val connectors: List<List<String>>,
    val treasures: List<List<List<String>>>)


fun readAllFromStream(inputStream: Reader): List<BoardObj> {

    val jsonReader = JsonReader(inputStream)
    jsonReader.isLenient = true
    val gson = Gson()
    val list = mutableListOf<BoardObj>()
    while (jsonReader.hasNext()) {
        list.add(gson.fromJson(jsonReader, BoardObj::class.java))
    }
    return list
}

fun main() {
    println(readAllFromStream(InputStreamReader(System.`in`)))
}