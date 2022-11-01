package Referee

import javafx.application.Application
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.layout.VBox
import javafx.stage.Stage
import java.util.concurrent.CountDownLatch

class RunGui: Application() {

    private var parentNode: Scene

    init {
        parentNode = initialScene
        singletonInstance = this
    }

    override fun start(primaryStage: Stage) {
        primaryStage.run {
            scene = parentNode
            show()
        }
        waitForInitLatch.countDown() // countDownLatch releases allowing other threads to access this instance
    }

    fun setParentNode(node: Parent) {
        parentNode.root = node
    }

    companion object {
        private lateinit var singletonInstance: RunGui
        private var initialScene: Scene = Scene(VBox())
        private var waitForInitLatch = CountDownLatch(1);


        fun getSingletonInstance(): RunGui {
            try {
                //ensures a proper instance by waiting until the object has been fully initialized
                waitForInitLatch.await();
            } catch (e: InterruptedException) {
                e.printStackTrace();
            }
            return singletonInstance
        }

    }
}