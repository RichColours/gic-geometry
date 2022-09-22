package blockley.gic.specific

import blockley.gic.engine.State
import blockley.gic.engine.StateEngineHook
import blockley.gic.specific.states.*

class GeometryGame constructor(
    private val outputStringObserver: (s: String) -> Unit,
    private val gameEndObserver: () -> Unit
) {

    private var theContext = GeometryGameContext() // Needs a name different from 'context' for the annonymous object
    private var currentState: State<GeometryGameContext>

    private val engineHook = object : StateEngineHook<GeometryGameContext> {

        override val context: GeometryGameContext
            get() = theContext

        override fun changeToState(state: State<GeometryGameContext>) {
            currentState = state
            currentState.onEntry()
        }

        override fun output(s: String) {
            outputStringObserver(s)
        }

        override fun end() {
            gameEndObserver()
        }
    }

    init {
        val endState = EndState(engineHook)

        val hitTestState = HitTestState(engineHook, endState)

        val createCustomShapeState = CreateCustomShapeState(engineHook, hitTestState)

        val createRandomShapeState = CreateRandomShapeState(engineHook, hitTestState)

        val welcomeState = WelcomeState(engineHook, createCustomShapeState, createRandomShapeState)

        currentState = welcomeState
        engineHook.changeToState(welcomeState)
    }

    /**
     * Ticks the game along.
     */
    fun inputString(s: String) {
        this.currentState.onString(s)
    }
}
