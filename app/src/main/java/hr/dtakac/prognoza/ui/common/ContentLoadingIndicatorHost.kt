package hr.dtakac.prognoza.ui.common

import androidx.compose.runtime.*
import kotlinx.coroutines.*

private const val ShowDelayMillis = 500L
private const val HideDelayMillis = 500L

private class ContentLoadingIndicatorState {
    private val _isVisible: MutableState<Boolean> = mutableStateOf(false)
    val isVisible: Boolean by _isVisible

    private var startTime: Long = -1L
    private var delayedShow: Job? = null
    private var delayedHide: Job? = null

    fun show(scope: CoroutineScope) {
        cancelDelayedHide()
        delayedShow = scope.launch {
            showActual()
        }
    }

    fun hide(scope: CoroutineScope) {
        if (delayedShow?.isActive == true) {
            // Show didn't happen yet, so just cancel it
            cancelDelayedShow()
        } else {
            // Show did happen, so hide it after some time
            delayedShow = null
            delayedHide = scope.launch {
                hideActual()
            }
        }
    }

    private suspend fun showActual() {
        startTime = -1
        delay(ShowDelayMillis)
        startTime = System.currentTimeMillis()
        _isVisible.value = true
    }

    private suspend fun hideActual() {
        val diff = System.currentTimeMillis() - startTime
        if (diff < HideDelayMillis && startTime != -1L) {
            // Ensure that indicator is visible for at least HideDelayMillis
            delay(HideDelayMillis - diff)
        }
        _isVisible.value = false
    }

    private fun cancelDelayedShow() {
        delayedShow?.cancel()
        delayedShow = null
        startTime = -1
    }

    private fun cancelDelayedHide() {
        delayedHide?.cancel()
        delayedHide = null
        startTime = -1
    }
}

@Composable
fun ContentLoadingIndicatorHost(
    isLoading: Boolean,
    contentLoadingIndicator: @Composable (isVisible: Boolean) -> Unit
) {
    val state = remember { ContentLoadingIndicatorState() }
    LaunchedEffect(isLoading) {
        if (isLoading) state.show(this)
        else state.hide(this)
    }
    contentLoadingIndicator(state.isVisible)
}