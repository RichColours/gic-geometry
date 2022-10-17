package blockley.gic.geometry

import blockley.gic.geometry.Geometry.deriveYEqMXPlusC
import blockley.gic.geometry.Geometry.isPointWithinConvexShape
import blockley.gic.geometry.Geometry.junctionDirection
import blockley.gic.geometry.Geometry.lineAngleDegrees
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class GeometryTest {

    @Test
    fun testLineAngleDegrees0deg() {
        assertEquals(0.0, lineAngleDegrees(Line(Point(0, 0), Point(0, 10))))
    }

    @Test
    fun testLineAngleDegrees45deg() {
        assertEquals(45.0, lineAngleDegrees(Line(Point(5, 5), Point(10, 10))))
    }

    @Test
    fun testLineAngleDegrees90deg() {
        assertEquals(90.0, lineAngleDegrees(Line(Point(0, 0), Point(1, 0))))
    }

    @Test
    fun testLineAngleDegrees135deg() {
        assertEquals(135.0, lineAngleDegrees(Line(Point(0, 0), Point(3, -3))))
    }

    @Test
    fun testLineAngleDegrees180deg() {
        assertEquals(180.0, lineAngleDegrees(Line(Point(0, 0), Point(0, -10))))
    }

    @Test
    fun testLineAngleDegrees225deg() {
        assertEquals(225.0, lineAngleDegrees(Line(Point(0, 0), Point(-4, -4))))
    }

    @Test
    fun testLineAngleDegrees270deg() {
        assertEquals(270.0, lineAngleDegrees(Line(Point(0, 0), Point(-100, 0))))
    }

    @Test
    fun testLineAngleDegrees315deg() {
        assertEquals(315.0, lineAngleDegrees(Line(Point(100, 100), Point(99, 101))))
    }

    @Test
    fun testJunctionDiffStraight() {
        assertEquals(
            Geometry.JunctionDirection.STRAIGHT,
            junctionDirection(
                Line(Point(0, 0), Point(1, 1)),
                Line(Point(1, 1), Point(2, 2))
            )
        )
    }

    @Test
    fun testJunctionDiffStraight2() {
        assertEquals(
            Geometry.JunctionDirection.STRAIGHT,
            junctionDirection(
                Line(Point(0, 0), Point(1, 1)),
                Line(Point(1, 1), Point(-10, -10))
            )
        )
    }

    @Test
    fun testJunctionDiffLeft() {
        assertEquals(
            Geometry.JunctionDirection.LEFT,
            junctionDirection(
                Line(Point(0, 0), Point(1, 1)),
                Line(Point(1, 1), Point(2, 3))
            )
        )
    }

    @Test
    fun testJunctionDiffLeft2() {
        assertEquals(
            Geometry.JunctionDirection.LEFT,
            junctionDirection(
                Line(Point(0, 0), Point(1, 1)),
                Line(Point(1, 1), Point(-1, 0))
            )
        )
    }

    @Test
    fun testJunctionDiffRight() {
        assertEquals(
            Geometry.JunctionDirection.RIGHT,
            junctionDirection(
                Line(Point(0, 0), Point(1, 1)),
                Line(Point(1, 1), Point(3, 2))
            )
        )
    }

    @Test
    fun testJunctionDiffRight2() {
        assertEquals(
            Geometry.JunctionDirection.RIGHT,
            junctionDirection(
                Line(Point(0, 0), Point(-1, 0)),
                Line(Point(-1, 0), Point(1, 1))
            )
        )
    }

    private val convexShapes = listOf(
        listOf(Point(0, 0), Point(1, 0), Point(1, 1)), // triangle
        listOf(Point(0, 0), Point(-1, 0), Point(-1, 1)), // triangle
        listOf(Point(10, 10), Point(11, 20), Point(10, 30), Point(9, 20)), // tall diamond
        listOf(Point(10, 0), Point(20, 1), Point(30, 3), Point(40, 6), Point(50, 10), Point(60, 15), Point(70, 21)) // curving upward with straight line back
    )

    private val notConvexShapes = listOf(
        listOf(Point(0, 0), Point(100, 100), Point(150, 150)),
        listOf(Point(0, 0), Point(100, 100), Point(-150, -150)),
        listOf(Point(10, 10), Point(12, 20), Point(10, 30), Point(11, 20)), // tall diamond but left side indented
        listOf(Point(10, 10), Point(11, 20), Point(10, 30), Point(12, 20)), // tall diamond but left side indented (reverse direction)
        listOf(Point(2, 2), Point(1, -2), Point(-2, 0), Point(2, 0), Point(-1, -2)) // diamond-ish
    )

    @Test
    fun testForConvexProperty() {
        convexShapes.forEach {
            assertTrue {
                Geometry.isConvexShape(Shape(it))
            }
        }
    }

    @Test
    fun testForNonConvexProperty() {
        notConvexShapes.forEach {
            assertFalse {
                Geometry.isConvexShape(Shape(it))
            }
        }
    }

    @Test
    fun testDeriveYEqMXPlusC_1() {

        val line = Line(Point(2, 2), Point(10, 10))
        val mc = deriveYEqMXPlusC(line)

        assertEquals(1.0, mc.m)
        assertEquals(0.0, mc.c)
    }

    @Test
    fun testDeriveYEqMXPlusC_2() {

        val line = Line(Point(5, 4), Point(10, 9))
        val mc = deriveYEqMXPlusC(line)

        assertEquals(1.0, mc.m)
        assertEquals(-1.0, mc.c)
    }

    @Test
    fun testDeriveYEqMXPlusC_3() {

        val line = Line(Point(-5, -6), Point(10, 9))
        val mc = deriveYEqMXPlusC(line)

        assertEquals(1.0, mc.m)
        assertEquals(-1.0, mc.c)
    }

    @Test
    fun testPointWithinSquare() {

        val points = listOf(
            Point(-2, 2), Point(2, 2), Point(2, -2), Point(-2, -2)
        )

        val inPoints = listOf(
            Point(0, 0),
            Point(1, 1),
            Point(2, 0)
        )

        val outPoints = listOf(
            Point(10, 0),
            Point(3, 0),
            Point(0, -3),
            Point(-3, 0),
            Point(0, 3)
        )

        inPoints.forEach {
            assertTrue(
                isPointWithinConvexShape(Shape(points), it)
            )
        }

        outPoints.forEach {
            assertFalse(
                isPointWithinConvexShape(Shape(points), it)
            )
        }
    }

    @Test
    fun testCheckTheThreePointStraightLineShape() {

        val line1 = Line(Point(0, 0), Point(1, 1))
        val line2 = Line(Point(1, 1), Point(2, 2))

        val dir = junctionDirection(line1, line2)

        assertEquals(Geometry.JunctionDirection.STRAIGHT, dir)

    }

    @Test
    fun testIsConvexShapeForStraightThreePointLine() {

        val p1 = Point(0, 0)
        val p2 = Point(1, 1)
        val p3 = Point(2, 2)

        assertFalse(
            Geometry.isConvexShape(Shape(listOf(p1, p2, p3)))
        )
    }
}
