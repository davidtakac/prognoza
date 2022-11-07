package hr.dtakac.prognoza.ui.forecast

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.map

class ForecastContentState(
    val lazyListState: LazyListState,
    val toolbarState: ToolbarState,
    val todayHoursState: TodayHoursState
)

// todo: maybe move this to prognoza toolbar?
// todo: maybe add the loading this as well?
class ToolbarState {
    private val _isTitleVisible: MutableState<Boolean> = mutableStateOf(false)
    val isTitleVisible: State<Boolean> get() = _isTitleVisible

    private val _isSubtitleVisible: MutableState<Boolean> = mutableStateOf(false)
    val isSubtitleVisible: State<Boolean> get() = _isSubtitleVisible

    private val _isEndVisible: MutableState<Boolean> = mutableStateOf(false)
    val isEndVisible: State<Boolean> get() = _isEndVisible

    fun setTitleVisible(isVisible: Boolean) {
        _isTitleVisible.value = isVisible
    }

    fun setSubtitleVisible(isVisible: Boolean) {
        _isSubtitleVisible.value = isVisible
    }

    fun setEndVisible(isVisible: Boolean) {
        _isEndVisible.value = isVisible
    }
}

// Based on this answer https://stackoverflow.com/a/70901858
class TodayHoursState {
    private val _timeWidth: MutableState<Dp> = mutableStateOf(0.dp)
    val timeWidth: State<Dp> get() = _timeWidth

    private val _precipitationWidth: MutableState<Dp> = mutableStateOf(0.dp)
    val precipitationWidth: State<Dp> get() = _precipitationWidth

    private val _temperatureWidth: MutableState<Dp> = mutableStateOf(0.dp)
    val temperatureWidth: State<Dp> get() = _temperatureWidth

    fun stretchTimeTo(width: Dp) {
        stretchWidthTo(_timeWidth, width)
    }

    fun stretchPrecipitationTo(width: Dp) {
        stretchWidthTo(_precipitationWidth, width)
    }

    fun stretchTemperatureTo(width: Dp) {
        stretchWidthTo(_temperatureWidth, width)
    }

    private fun stretchWidthTo(
        state: MutableState<Dp>,
        width: Dp
    ) {
        if (width > state.value) {
            state.value = width
        }
    }
}

@Composable
fun rememberToolbarState() = remember {
    ToolbarState()
}

@Composable
fun rememberTodayHoursLazyTableState() = remember {
    TodayHoursState()
}

@Composable
fun rememberForecastContentState(
    lazyListState: LazyListState = rememberLazyListState(),
    toolbarState: ToolbarState = rememberToolbarState(),
    todayHoursState: TodayHoursState = rememberTodayHoursLazyTableState()
): ForecastContentState {
    LaunchedEffect(lazyListState) {
        snapshotFlow { lazyListState.layoutInfo }
            .distinctUntilChanged()
            .map { layoutInfo ->
                Triple(
                    layoutInfo.keyVisibilityPercent("place") != 0f,
                    layoutInfo.keyVisibilityPercent("time") != 0f,
                    layoutInfo.keyVisibilityPercent("temperature") > 50f
                )
            }
            .distinctUntilChanged()
            // Drops the initial visibility changes while the list initializes
            .drop(1)
            .collect { (isListPlaceVisible, isListDateVisible, isListTemperatureVisible) ->
                with(toolbarState) {
                    setTitleVisible(!isListPlaceVisible)
                    setSubtitleVisible(!isListDateVisible)
                    setEndVisible(!isListTemperatureVisible)
                }
            }
    }

    return remember {
        ForecastContentState(
            lazyListState = lazyListState,
            toolbarState = toolbarState,
            todayHoursState = todayHoursState
        )
    }
}