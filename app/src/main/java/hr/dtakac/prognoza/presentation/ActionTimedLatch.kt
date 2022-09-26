package hr.dtakac.prognoza.presentation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ActionTimedLatch(private val scope: CoroutineScope) {
    private var actionStartJob: Job? = null

    fun start(
        millisBeforeAction: Long = 300L,
        action: () -> Unit
    ) {
        actionStartJob = scope.launch {
            delay(millisBeforeAction)
            action()
        }
    }

    fun stop(
        millisBeforeAction: Long = 1000L,
        action: () -> Unit
    ) {
        actionStartJob?.let { job ->
            if (job.isActive) {
                job.cancel()
                actionStartJob = null
            } else {
                scope.launch {
                    delay(millisBeforeAction)
                    actionStartJob = null
                    action()
                }
            }
        }
    }
}