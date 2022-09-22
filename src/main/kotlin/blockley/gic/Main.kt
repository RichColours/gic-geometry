package blockley.gic

import blockley.gic.specific.GeometryGame

class Main {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {

            var gameContinue = true

            val game = GeometryGame(
                {
                    print(it)
                },
                {
                    gameContinue = false
                }
            )

            do {
                val lineInput = readLine()!!
                game.inputString(lineInput)

            } while (gameContinue)

            // The End
        }
    }
}
