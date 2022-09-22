import blockley.gic.geometry.Point

/**
 * Create the nice point list that's used around the game.
 */
fun pointsToListString(points: List<Point>): String {

    val sb = StringBuilder()

    points.forEachIndexed { index, point ->
        sb.appendLine("${index + 1}(${point.x}, ${point.y})")
    }

    return sb.toString()
}

fun badCommand(): String {
    return "Bad input, try again$newLine"
}

val newLine: String = System.lineSeparator()
