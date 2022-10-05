package hr.dtakac.prognoza.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val ShowDelayMillis = 300L
private const val HideDelayMillis = 1000L

class PrognozaLoadingIndicatorState(
    private val scope: CoroutineScope
) {
    var isVisible: Boolean = false
        private set

    private var showJob: Job? = null
    private var visibleTimestampMillis: Long? = null

    fun showLoadingIndicator() {
        clearShowJob()
        showJob = scope.launch {
            delay(ShowDelayMillis)
            isVisible = true
            visibleTimestampMillis = System.currentTimeMillis()
        }
    }

    fun hideLoadingIndicator() {
        showJob?.let { showJob ->
            if (showJob.isActive) {
                clearShowJob()
            } else {
                scope.launch {
                    visibleTimestampMillis?.let {
                        val currentMillis = System.currentTimeMillis()
                        val durationVisibleMillis = currentMillis - it

                        if (durationVisibleMillis < HideDelayMillis) {
                            // Make loading indicator show for at least HideDelayMillis
                            delay(HideDelayMillis - durationVisibleMillis)
                        }

                        isVisible = false
                    }.also { clearShowJob() }
                }
            }
        }
    }

    private fun clearShowJob() {
        showJob?.cancel()
        visibleTimestampMillis = null
    }
}

@Composable
fun rememberPrognozaLoadingIndicatorState(scope: CoroutineScope = rememberCoroutineScope()) =
    remember {
        PrognozaLoadingIndicatorState(scope)
    }