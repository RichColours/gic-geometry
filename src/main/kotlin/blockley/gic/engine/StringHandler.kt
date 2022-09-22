package blockley.gic.engine

/**
 * String handling by type delegation.
 *
 * Subclassses throwing UnhandledException will see onElse() called.
 */
open class StringHandler {

    class UnhandledException : Exception()

    private lateinit var original: String

    fun handle(s: String) {

        this.original = s

        if (s == "#") {
            this.onHash()
            return
        }

        if (s.matches("\\d+".toRegex())) {
            try {
                this.onNumber(s.toInt())
            } catch (e: UnhandledException) {
                onElse(this.original)
            }
            return
        }

        if (s.matches("(-?\\d+) (-?\\d+)".toRegex())) {
            val xy = s.split(' ')

            try {
                this.onCoord(xy[0].toInt(), xy[1].toInt())
            } catch (e: UnhandledException) {
                onElse(this.original)
            }
            return
        }

        onElse(s)
    }

    open fun onNumber(v: Int) {
        this.onElse(original)
    }

    open fun onHash() {
        this.onElse(this.original)
    }

    open fun onCoord(x: Int, y: Int) {
        this.onElse(this.original)
    }

    open fun onElse(original: String) {
    }
}
