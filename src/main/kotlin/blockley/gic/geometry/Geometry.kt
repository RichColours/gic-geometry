package blockley.gic.geometry

import kotlin.math.atan
import kotlin.random.Random

/**
 * Library of geometry functions, which are composed to form more complex functions.
 */
object Geometry {

    const val DOUBLE_TOLERANCE = 0.001

    enum class JunctionDirection { LEFT, RIGHT, STRAIGHT }

    data class MC constructor(
        val m: Double,
        val c: Double
    ) {

        fun y(x: Double): Double {
            return m * x + c
        }
    }

    fun hasNoRepeatPoints(points: List<Point>): Boolean {

        val pointsSet = mutableSetOf<Point>()
        pointsSet.addAll(points)
        return pointsSet.size == points.size
    }

    fun lineAngleDegrees(l: Line): Double {

        val o = l.end.y.toDouble() - l.start.y // Y diff
        val a = l.end.x.toDouble() - l.start.x // X diff

        val deg = if (a == 0.0) {
            if (o == 0.0) {
                throw Error("Line is a dot, it has no angle")
            } else if (o > 0.0) {
                0.0
            } else {
                180.0
            }
        } else if (a > 0.0) {
            90.0 - Math.toDegrees(atan(o / a))
        } else { // a < 0.0
            270 - Math.toDegrees(atan(o / a))
        }

        return deg
    }

    /**
     * Clockwise angle of the joint between these connected lines.
     */
    fun junctionAngle(l1: Line, l2: Line): Double {

        if (l2.start != l1.end) throw Error("Line 2 does not start at Line 1 end")

        val l1Angle = lineAngleDegrees(l1)
        val l2Angle = lineAngleDegrees(l2)

        val l2Diff = ((l2Angle - l1Angle) + 360.0) % 360.0

        return l2Diff
    }

    fun junctionDirection(l1: Line, l2: Line): JunctionDirection {

        val l2Diff = junctionAngle(l1, l2)

        return if (l2Diff == 0.0 || l2Diff == 180.0)
            JunctionDirection.STRAIGHT
        else if (l2Diff > 0.0 && l2Diff < 180.0)
            JunctionDirection.RIGHT
        else
            JunctionDirection.LEFT
    }

    /**
     * Requirementw:
     * * 3+ points
     * * All junctions non-straight
     * * All angles equal 360 ± tolerance, decided by calculating in either direction (as junction angle calculator only counts in one direction).
     */
    fun isConvexShape(shape: Shape): Boolean {

        if (shape.points.size < 3)
            throw Error("Need 3+ points to be a reasonable shape.")

        if (!hasNoRepeatPoints(shape.points))
            return false

        val points = shape.points
        val pointsBackwards = shape.points.asReversed()

        // Derive lines forwards
        val linesForward = points.mapIndexed { index, point ->
            val nextPointIndex = (index + 1) % points.size
            val nextPoint = points[nextPointIndex]
            Line(point, nextPoint)
        }

        // Derive lines backwards
        val linesBackward = pointsBackwards.mapIndexed { index, point ->
            val nextPointIndex = (index + 1) % pointsBackwards.size
            val nextPoint = pointsBackwards[nextPointIndex]
            Line(point, nextPoint)
        }

        // All angles in one direction
        val junctionAnglesForward = linesForward.mapIndexed { index, line ->
            val nextLineIndex = (index + 1) % linesForward.size
            val nextLine = linesForward[nextLineIndex]
            junctionAngle(line, nextLine)
        }

        // All angles in the other direction
        val junctionAnglesBackward = linesBackward.mapIndexed { index, line ->
            val nextLineIndex = (index + 1) % linesBackward.size
            val nextLine = linesBackward[nextLineIndex]
            junctionAngle(line, nextLine)
        }

        val forwardAnglesSum = junctionAnglesForward.sum()
        val backwardAnglesSum = junctionAnglesBackward.sum()

        if (junctionAnglesForward.any { it == 180.0 || it == 0.0 })
            return false

        return equalToWithTolerance(forwardAnglesSum, 360.0, DOUBLE_TOLERANCE) || equalToWithTolerance(backwardAnglesSum, 360.0, DOUBLE_TOLERANCE)
    }

    fun generateRandomConvexShape(maxXYDistanceFromOrigin: Int, maxPoints: Int): Shape {

        tailrec fun generateRandomConvexShapeInner(maxXYDistanceFromOrigin: Int, numPoints: Int): Shape {

            val points = 0.until(numPoints).map {
                Point(
                    Random.Default.nextInt(-maxXYDistanceFromOrigin, maxXYDistanceFromOrigin + 1),
                    Random.Default.nextInt(-maxXYDistanceFromOrigin, maxXYDistanceFromOrigin + 1)
                )
            }

            val maybeConvex = Shape(points)

            if (isConvexShape(maybeConvex))
                return maybeConvex

            return generateRandomConvexShapeInner(maxXYDistanceFromOrigin, maxPoints)
        }

        val numPoints = Random.Default.nextInt(3, maxPoints + 1)

        return generateRandomConvexShapeInner(maxXYDistanceFromOrigin, numPoints)
    }

    fun equalToWithTolerance(actual: Double, expected: Double, tolerance: Double): Boolean {
        return (expected - tolerance) <= actual && actual <= (expected + tolerance)
    }

    /**
     * Slope of line, including infinite if vertical up and -inf if vertical down.
     */
    fun deriveYEqMXPlusC(line: Line): MC {

        // Find y=mx+c
        val xDiff = line.end.x.toDouble() - line.start.x.toDouble()
        val yDiff = line.end.y.toDouble() - line.start.y.toDouble()

        val yPerX = yDiff / xDiff
        val c = line.start.y - (line.start.x * yPerX)
        return MC(yPerX, c)
    }

    /**
     * Is point "within" the line?
     *
     * "within" is a complicated predicate, depending on the slope and direction of the line.
     */
    fun isPointWithinLine(line: Line, p: Point): Boolean {

        val lineMC = deriveYEqMXPlusC(line)
        val lineAngle = lineAngleDegrees(line)

        if (lineMC.m == Double.POSITIVE_INFINITY) {

            // Upward vertical line: point on or to the right?
            return p.x >= line.start.x

        } else if (lineMC.m == Double.NEGATIVE_INFINITY) {

            // Downward vertical line: point on or to the left?
            return p.x <= line.start.x

        } else if (lineAngle > 0 && lineAngle < 180.0) {

            // "right half" of slope hemisphere, is point "on or below" the line?

            val lineY = lineMC.y(p.x.toDouble())
            return p.y <= lineY

        } else if (lineAngle > 180.0 || lineAngle == 0.0) {

            // "left half" of slope hemisphere, is point "on or above" the line?

            val lineY = lineMC.y(p.x.toDouble())
            return p.y >= lineY

        } else {
            throw Error("Uncovered case logic error.")
        }
    }

    /**
     * Is a point within this shape, given by a list of connected lines?
     *
     * Not performing shape validation, assuming the given shape to be a valid convex shape.
     */
    fun isPointWithinConvexShape(shape: Shape, p: Point): Boolean {

        // Determine natural shape direction
        val l1 = shape.getLine(0)
        val l2 = shape.getLine(1)
        val junctionDirection = junctionDirection(l1, l2)

        if (junctionDirection == JunctionDirection.STRAIGHT) throw Error("No junction should be STRAIGHT in this convex shape.")

        // Get all point in the RIGHT direction ... turning clockwise
        val points = if (junctionDirection == JunctionDirection.RIGHT) {
            shape.points
        } else {
            shape.points.asReversed()
        }

        val lines = points.mapIndexed { index, point ->
            val nextPointIndex = (index + 1) % points.size
            val nextPoint = points[nextPointIndex]
            Line(point, nextPoint)
        }

        val lineComparisons = lines.map { l ->
            isPointWithinLine(l, p)
        }

        val allWithin = lineComparisons.all { it }

        return allWithin
    }
}
