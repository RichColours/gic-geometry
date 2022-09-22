package blockley.gic.specific.states

import badCommand
import blockley.gic.engine.State
import blockley.gic.engine.StateEngineHook
import blockley.gic.engine.StringHandler
import blockley.gic.specific.GeometryGameContext
import blockley.gic.geometry.Point
import newLine
import pointsToListString

class CreateCustomShapeState(
    engineHook: StateEngineHook<GeometryGameContext>,
    private val hitTestState: State<GeometryGameContext>
) : State<GeometryGameContext>(engineHook) {

    private val thisState = this

    override fun onEntry() {
        if (!engineHook.context.convexShape.isComplete) {
            engineHook.output(pleaseEnterCoordNString(engineHook.context.convexShape.numPoints + 1))
        } else {
            engineHook.output(pleaseEnterCoordNOrFinalise(engineHook.context.convexShape.numPoints + 1))
        }
    }

    override fun onString(s: String) {
        object : StringHandler() {

            override fun onCoord(x: Int, y: Int) {
                val p = Point(x, y)

                val worked = engineHook.context.convexShape.tryAddPoint(p)

                if (worked) {
                    // No specific message for the point being added.
                } else {
                    engineHook.output(newCoordInvalid(p))
                }

                if (engineHook.context.convexShape.isComplete) {
                    engineHook.output(shapeValidAndComplete())
                } else {
                    engineHook.output(shapeIncomplete())
                }

                engineHook.output(pointsToListString(engineHook.context.convexShape.toList()))

                engineHook.changeToState(thisState)
            }

            override fun onHash() {
                if (engineHook.context.convexShape.isComplete) {
                    engineHook.output(yourFinalisedShapeIs())
                    engineHook.output(pointsToListString(engineHook.context.convexShape.toList()))
                    engineHook.changeToState(hitTestState)
                } else {
                    engineHook.output(shapeIncomplete())
                    engineHook.output(pleaseEnterCoordNString(engineHook.context.convexShape.numPoints + 1))
                }
            }

            override fun onElse(original: String) {
                engineHook.output(badCommand())
            }
        }
            .handle(s)
    }

    private fun pleaseEnterCoordNString(n: Int): String {
        return "Please enter coordinates $n in x y format$newLine"
    }

    private fun pleaseEnterCoordNOrFinalise(n: Int): String {
        return "Please enter # to finalize your shape or enter coordinates $n in x y format$newLine"
    }

    private fun shapeIncomplete(): String {
        return "Your current shape is incomplete$newLine"
    }

    private fun shapeValidAndComplete(): String {
        return "Your current shape is valid and is complete$newLine"
    }

    private fun newCoordInvalid(p: Point): String {
        return """
            New coordinates(${p.x},${p.y}) is invalid!!! 
            Not adding new coordinates to the current shape.
            
        """.trimIndent()
    }

    private fun yourFinalisedShapeIs(): String {
        return "Your finalised shape is$newLine"
    }
}
