package hr.dtakac.prognoza.ui.common

import androidx.compose.runtime.*
import kotlinx.coroutines.*

private class ContentLoadingIndicatorState(
    private val deferVisibilityForMillis: Long,
    private val showForAtLeastMillis: Long
) {
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
        delay(deferVisibilityForMillis)
        startTime = System.currentTimeMillis()
        _isVisible.value = true
    }

    private suspend fun hideActual() {
        val diff = System.currentTimeMillis() - startTime
        if (diff < showForAtLeastMillis && startTime != -1L) {
            // Ensure that indicator is visible for at least HideDelayMillis
            delay(showForAtLeastMillis - diff)
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
    deferVisibilityForMillis: Long = 500L,
    showForAtLeastMillis: Long = 500L,
    contentLoadingIndicator: @Composable (isVisible: Boolean) -> Unit
) {
    val state = remember {
        ContentLoadingIndicatorState(deferVisibilityForMillis, showForAtLeastMillis)
    }
    LaunchedEffect(isLoading) {
        if (isLoading) state.show(this)
        else state.hide(this)
    }
    contentLoadingIndicator(state.isVisible)
}