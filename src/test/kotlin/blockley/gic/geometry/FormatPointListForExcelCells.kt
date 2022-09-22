package blockley.gic.geometry

object FormatPointListForExcelCells {

    @JvmStatic
    fun main(args: Array<String>) {

        println("Enter a point list that came from the Geometry game:")

        val sb = StringBuilder()
        var line: String?

        do {
            line = readLine()
            sb.appendLine(line)

        } while (!line.isNullOrBlank())

        sb.trim()

        val lines = sb.split(System.lineSeparator())

        val nonBlankLines = lines.filter { it.isNotBlank() }

        val outputLines = nonBlankLines.map {
            val parts = "\\d+\\((-?\\d+), (-?\\d+)\\)".toRegex().matchEntire(it)
            "${parts!!.groupValues[1]}\t${parts.groupValues[2]}"
        }

        print(outputLines.joinToString(System.lineSeparator()))
    }
}