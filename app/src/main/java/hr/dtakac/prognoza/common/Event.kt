package hr.dtakac.prognoza.common

class Event<T>(
    private var value: T
) {
    var isConsumed: Boolean = false
        private set

    fun consume(): T {
        isConsumed = true
        return value
    }

    fun peekValue(): T {
        return value
    }
}