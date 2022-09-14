package B.Other

import java.io.InputStreamReader
import java.io.Reader

/**
 * Entrypoint, routes input and output from stdin.
 */
fun mainz() {
    B.readLinesAndAppendChars(InputStreamReader(System.`in`), System.out)
}

object B {
    /**
     * Reads all lines from an input stream and, if, all lines are one of "┘", "┐", "└", "┌" (including quotes),
     * collects them intro a single string, otherwise prints 'unacceptable input'.
     */
    fun readLinesAndAppendChars(readable: Reader, output: Appendable) {
        val lines = readable.readText().split("\n")

        val stringBuilder = StringBuilder()

        var failed = false
        for (nextLine in lines) {
            if (matchesChar(nextLine)) {
                stringBuilder.append(nextLine.removeSurrounding("\""))
            } else {
                failed = true
                break
            }
        }

        if (failed) {
            output.append("unacceptable input")
        } else {
            output.append(stringBuilder.toString())
        }
    }

    /**
     * Checks if line is one of "┘", "┐", "└", "┌" (including quotes)
     */
    fun matchesChar(line: String): Boolean {
        return when(line) {
            "\"┘\"","\"┐\"", "\"└\"", "\"┌\"" -> true
            else -> false
        }
    }
}
