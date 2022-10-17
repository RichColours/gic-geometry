package blockley.gic.geometry

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MutableConvexShapeTest {

    @Test
    fun testThirdPointOfStraightLineNotValid() {

        val p1 = Point(0, 0)
        val p2 = Point(1, 1)
        val p3 = Point(2, 2)

        val mcs = MutableConvexShape()

        assertTrue(mcs.tryAddPoint(p1))
        assertTrue(mcs.tryAddPoint(p2))
        assertFalse(mcs.tryAddPoint(p3))
    }

    @Test
    fun testTriangleIsValid() {

        val p1 = Point(0, 0)
        val p2 = Point(0, 1)
        val p3 = Point(1, 1)

        val mcs = MutableConvexShape()

        assertTrue(mcs.tryAddPoint(p1))
        assertTrue(mcs.tryAddPoint(p2))
        assertTrue(mcs.tryAddPoint(p3))
    }
}
