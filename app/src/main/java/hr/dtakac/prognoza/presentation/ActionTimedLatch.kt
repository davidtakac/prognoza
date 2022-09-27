package hr.dtakac.prognoza.presentation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ActionTimedLatch(private val scope: CoroutineScope) {
    private var delayedStart: Job? = null

    fun start(
        millisBeforeAction: Long = 300L,
        action: () -> Unit
    ) {
        delayedStart?.cancel()
        delayedStart = scope.launch {
            delay(millisBeforeAction)
            action()
        }
    }

    fun stop(
        millisBeforeAction: Long = 1000L,
        action: () -> Unit
    ) {
        delayedStart?.let { job ->
            if (job.isActive) {
                // If stopped during delayed start, just cancel the start entirely
                job.cancel()
                delayedStart = null
            } else {
                // If stopped after delayed start is over, i.e. after start, then wait for
                // some time before stopping
                scope.launch {
                    delay(millisBeforeAction)
                    delayedStart = null
                    action()
                }
            }
        } // If delayed start never happened, do nothing
    }
}