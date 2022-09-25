package Other

import Other.C.readFromInputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.ServerSocket

const val MAX_PORT_NUMBER = 60000
const val MIN_PORT_NUMBER = 10000

/**
 * Exposes a TCP Socket on the specified port. This port handles a stream of well-formed JSON and returns the
 * corresponding string of acceptable characters on the outstream when the TCP instream connection is closed.
 */
fun main(args: Array<String>) {
    val portNumber = asValidatedPortNumber(args)

    // accept only one client
    val clientSocket = ServerSocket(portNumber).accept()
    val socketInputStreamReader = InputStreamReader(clientSocket.getInputStream())

    val cardinalCharacterJSONArr = readFromInputStream(socketInputStreamReader)

    val socketOutputStreamWriter =  OutputStreamWriter(clientSocket.getOutputStream())
    socketOutputStreamWriter.write(cardinalCharacterJSONArr.toString())

    clientSocket.close()
}

/**
 * Validates that the port number argument is within the acceptable range (10000, 60000) inclusive).
 */
fun asValidatedPortNumber(args: Array<String>): Int {
    validateArgs(args)
    val portNumberArgument = args[0]
    val portNumber = Integer.valueOf(portNumberArgument)

    if (portNumber in MIN_PORT_NUMBER..MAX_PORT_NUMBER) {
        return portNumber
    }
    throw IllegalArgumentException("Port number must be a valid integer wihtin $MIN_PORT_NUMBER and $MAX_PORT_NUMBER" +
            " inclusive.")
}

/**
 * Ensures only one command line argument is supplied.
 */
fun validateArgs(args: Array<String>) {
    if (args.size != 1) {
        throw IllegalArgumentException("Expected only one command line argument.")
    }
}

