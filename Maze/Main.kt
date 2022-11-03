import Client.javafx.CommandLineRefereeApp
import javafx.application.Application.launch
import testing.ObserverIntegrationTests

fun main(args: Array<String>) {
    when (args[0]) {
        "xgames" -> ObserverIntegrationTests.run()
        "xgames-with-observer" -> launch(CommandLineRefereeApp::class.java)
    }
}