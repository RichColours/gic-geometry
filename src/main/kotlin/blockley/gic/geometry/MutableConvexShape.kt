package blockley.gic.geometry

import blockley.gic.geometry.Geometry.generateRandomConvexShape

/**
 * Needs to cope with the first two points creating a valid convex shape.
 *
 * 0, 1, 2 valid points is incomplete
 * 3..8 valid points
 */
class MutableConvexShape {

    private val points = mutableListOf<Point>()

    val numPoints get() = this.points.size

    val isComplete get() = this.points.size >= 3

    fun tryAddPoint(p: Point):Boolean {

        return if (!Geometry.hasNoRepeatPoints(points)) {
            false

        } else {

            if (numPoints < 2) {
                points.add(p)
                true

            } else {

                val pointListWithNewPoint = mutableListOf<Point>()
                pointListWithNewPoint.addAll(this.points)
                pointListWithNewPoint.add(p)

                if (!Geometry.isConvexShape(Shape(pointListWithNewPoint))) {
                    false

                } else {

                    points.add(p)
                    true
                }
            }
        }
    }

    fun testShapeContainsPoint(p: Point): Boolean {

        return Geometry.isPointWithinConvexShape(Shape(points), p)
    }

    fun clearAndFillWithRandomShape() {
        points.clear()

        points.addAll(
            generateRandomConvexShape(MAX_XY_DISTANCE_FROM_ORIGIN, MAX_POINTS).points
        )
    }

    fun toList(): List<Point> {
        return points.toList()
    }

    companion object {
        private const val MAX_POINTS = 8
        private const val MAX_XY_DISTANCE_FROM_ORIGIN = 100
    }
}
