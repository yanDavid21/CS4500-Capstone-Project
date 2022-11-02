package Client.javafx

import Common.GameState
import Referee.ObserverMechanism

class JavaFXObserverView: ObserverMechanism {

    private lateinit var features: MazeObserverInterface

    init {

    }

    fun giveFeatures(features: MazeObserverInterface) {
        this.features = features
    }


    override fun updateState(newState: GameState) {
        TODO("Not yet implemented")
    }

    override fun gameOver() {
        TODO("Not yet implemented")
    }
}