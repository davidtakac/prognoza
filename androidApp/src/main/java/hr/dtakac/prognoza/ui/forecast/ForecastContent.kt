package hr.dtakac.prognoza.ui.forecast

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.unit.dp
import hr.dtakac.prognoza.presentation.asString
import hr.dtakac.prognoza.presentation.forecast.*
import hr.dtakac.prognoza.ui.common.*

@Composable
fun ForecastContent(
    state: ForecastState,
    onMenuClick: () -> Unit = {}
) {
    val backgroundColor = MaterialTheme.colorScheme.background
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind { drawRect(color = backgroundColor) }
            .padding(WindowInsets.navigationBars.asPaddingValues())
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            val toolbarState = rememberAppToolbarState()
            AppToolbarWithLoadingIndicator(
                state = toolbarState,
                placeName = state.forecast?.current?.place?.asString() ?: "",
                date = state.forecast?.current?.date?.asString() ?: "",
                temperature = state.forecast?.current?.temperature?.asString() ?: "",
                isLoading = state.isLoading,
                onNavigationClick = onMenuClick
            )
            if (state.forecast == null) {
                if (state.error != null) {
                    ForecastFullScreenError(
                        error = state.error.asString(),
                        modifier = Modifier
                            .padding(24.dp)
                            .fillMaxSize()
                    )
                }
            } else {
                Box {
                    ForecastDataList(
                        data = state.forecast,
                        isPlaceVisible = { toolbarState.setTitleVisible(!it) },
                        isDateVisible = { toolbarState.setSubtitleVisible(!it) },
                        isTemperatureVisible = { toolbarState.setEndVisible(!it) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (state.error != null) {
                        ForecastSnackBarError(
                            error = state.error.asString(),
                            modifier = Modifier
                                .padding(16.dp)
                                .align(Alignment.BottomCenter)
                        )
                    }
                }
            }
        }
    }
}