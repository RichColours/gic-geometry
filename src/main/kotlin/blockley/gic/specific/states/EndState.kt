package blockley.gic.specific.states

import blockley.gic.engine.State
import blockley.gic.engine.StateEngineHook
import blockley.gic.specific.GeometryGameContext

class EndState(
    engineHook: StateEngineHook<GeometryGameContext>,
) : State<GeometryGameContext>(engineHook) {

    override fun onEntry() {
        engineHook.output(
            """
                Thank you for playing the GIC geometry puzzle app
                Have a nice day!
                
            """.trimIndent()
        )
        engineHook.end()
    }
}
