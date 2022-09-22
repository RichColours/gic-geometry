package blockley.gic.engine

/**
 * Interface to actions that a State can invoke on the game engine to affect change.
 */
interface StateEngineHook<T> {
    val context: T
    fun changeToState(state: State<T>)
    fun output(s: String)
    fun end()
}
