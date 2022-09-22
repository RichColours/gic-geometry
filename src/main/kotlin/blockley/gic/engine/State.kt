package blockley.gic.engine

/**
 * Models an exclusive state in the state machine.
 * A state responds to being 'entered' (moved to from some other state, or re-entered from the same state).
 * A state can generate output actions such as tell the game engine to change to another state, or yield some textual output.
 * Importantly it can tell the game engine to signal to its outside that the game has ended.
 */
open class State<T> constructor(
    protected val engineHook: StateEngineHook<T>
) {

    open fun onEntry() {
    }

    open fun onString(s: String) {
    }
}
