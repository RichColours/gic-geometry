package blockley.gic.specific.states

import badCommand
import blockley.gic.engine.State
import blockley.gic.engine.StateEngineHook
import blockley.gic.engine.StringHandler
import blockley.gic.specific.GeometryGameContext
import blockley.gic.geometry.Point
import newLine
import pointsToListString

class HitTestState(
    engineHook: StateEngineHook<GeometryGameContext>,
    private val endState: State<GeometryGameContext>
) : State<GeometryGameContext>(engineHook) {

    private val thisState = this

    override fun onEntry() {
        engineHook.output(pleaseKeyInTestOrEnd())
    }

    override fun onString(s: String) {
        object : StringHandler() {

            override fun onCoord(x: Int, y: Int) {
                val p = Point(x, y)

                val isWithin = engineHook.context.convexShape.testShapeContainsPoint(p)

                engineHook.output(yourFinalisedShapeIs())
                engineHook.output(pointsToListString(engineHook.context.convexShape.toList()))

                if (isWithin) {
                    engineHook.output(coordsWithin(p))
                } else {
                    engineHook.output(coordsOutside(p))
                }

                engineHook.changeToState(thisState)
            }

            override fun onHash() {
                engineHook.changeToState(endState)
            }

            override fun onElse(original: String) {
                engineHook.output(badCommand())
            }
        }
            .handle(s)
    }

    private fun pleaseKeyInTestOrEnd(): String {
        return "Please key in test coordinates in x y format or enter # to quit the game$newLine"
    }

    private fun yourFinalisedShapeIs(): String {
        return "Your finalised shape is$newLine"
    }

    private fun coordsWithin(p: Point): String {
        return "Coordinates (${p.x} ${p.y}) is within your finalized shape$newLine"
    }

    private fun coordsOutside(p: Point): String {
        return "Sorry, coordinates (${p.x} ${p.y}) is outside of your finalized shape$newLine"
    }
}
