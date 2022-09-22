package blockley.gic.specific.states

import blockley.gic.engine.State
import blockley.gic.engine.StateEngineHook
import blockley.gic.specific.GeometryGameContext
import newLine
import pointsToListString

class CreateRandomShapeState(
    engineHook: StateEngineHook<GeometryGameContext>,
    private val hitTestState: State<GeometryGameContext>
) : State<GeometryGameContext>(engineHook) {

    override fun onEntry() {

        engineHook.context.convexShape.clearAndFillWithRandomShape()

        engineHook.output(yourRandomShapeIs())
        engineHook.output(pointsToListString(engineHook.context.convexShape.toList()))

        engineHook.changeToState(hitTestState)
    }

    private fun yourRandomShapeIs(): String {
        return "Your random shape is$newLine"
    }
}
