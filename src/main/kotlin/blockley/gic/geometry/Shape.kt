package blockley.gic.geometry

class Shape constructor(
    val points: List<Point>
) {

    fun getLine(n: Int): Line {
        return Line(points[n], points[n + 1])
    }
}
