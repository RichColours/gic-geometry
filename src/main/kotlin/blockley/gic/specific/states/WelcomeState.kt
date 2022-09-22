package blockley.gic.specific.states

import badCommand
import blockley.gic.engine.State
import blockley.gic.engine.StateEngineHook
import blockley.gic.engine.StringHandler
import blockley.gic.specific.GeometryGameContext

class WelcomeState(
    engineHook: StateEngineHook<GeometryGameContext>,
    private val createCustomShapeState: State<GeometryGameContext>,
    private val createRandomShapeState: State<GeometryGameContext>
) : State<GeometryGameContext>(engineHook) {

    override fun onEntry() {
        engineHook.output(welcome())
    }

    override fun onString(s: String) {
        object : StringHandler() {

            override fun onNumber(v: Int) {

                when (v) {
                    1 -> {
                        engineHook.changeToState(createCustomShapeState)
                    }
                    2 -> {
                        engineHook.changeToState(createRandomShapeState)
                    }
                    else -> throw UnhandledException()
                }
            }

            override fun onElse(original: String) {
                engineHook.output(badCommand())
            }
        }
            .handle(s)
    }

    private fun welcome(): String {
        return """
            Welcome to the GIC geometry puzzle app
            [1] Create a custom shape
            [2] Generate a random shape
            
            """.trimIndent()
    }
}
