package hr.dtakac.prognoza.base

class Event<T>(
    private var value: T
) {
    var isConsumed: Boolean = false
        private set

    fun getValue(): T {
        isConsumed = true
        return value
    }

    fun peekValue(): T {
        return value
    }
}