package hr.dtakac.prognoza.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import java.lang.IllegalStateException

class Event<T>(private val value: T) {
    var isConsumed: Boolean = false
        private set

    fun consume(): T = if (!isConsumed) {
        isConsumed = true
        value
    } else throw IllegalStateException("Tried to consume already consumed event.")

    fun peek(): T = value
}

fun simpleEvent(): Event<Unit> = Event(Unit)

@Composable
fun <T> OnEvent(event: Event<T>?, block: (T) -> Unit) {
    LaunchedEffect(event) {
        if (event?.isConsumed == false) {
            block(event.consume())
        }
    }
}