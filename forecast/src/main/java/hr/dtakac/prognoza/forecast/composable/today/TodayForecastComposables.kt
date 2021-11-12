package hr.dtakac.prognoza.forecast.composable.today

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hr.dtakac.prognoza.core.composable.ContentLoader
import hr.dtakac.prognoza.core.formatting.formatDaySummaryTime
import hr.dtakac.prognoza.core.model.ui.MeasurementUnit
import hr.dtakac.prognoza.core.theme.PrognozaTheme
import hr.dtakac.prognoza.forecast.composable.common.*
import hr.dtakac.prognoza.forecast.model.TodaySummaryUiModel
import hr.dtakac.prognoza.forecast.uistate.*

@ExperimentalAnimationApi
@Composable
fun TodayForecast(
    forecast: TodayForecastUiState,
    outdatedForecast: OutdatedForecastUiState?,
    isLoading: Boolean,
    emptyForecast: EmptyForecastUiState?,
    expandedHourIndices: List<Int>,
    onHourClicked: (Int) -> Unit,
    onTryAgainClicked: () -> Unit,
    onPickAPlaceClicked: () -> Unit,
    preferredUnit: MeasurementUnit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (forecast is TodayForecastUiState.Success) {
            LazyColumn(Modifier.fillMaxHeight()) {
                item {
                    TodaySummary(
                        todaySummaryUiModel = forecast.todaySummary,
                        outdatedForecast = outdatedForecast
                    )
                }
                ExpandableInstants(
                    instants = forecast.instants,
                    expandedInstantIndices = expandedHourIndices,
                    preferredUnit = preferredUnit,
                    onInstantClicked = { onHourClicked(it) }
                )
            }
        }
        ContentLoader(isLoading = isLoading)
        when (emptyForecast) {
            is EmptyForecastUiState.BecauseReason -> EmptyForecastBecauseReason(
                emptyForecast = emptyForecast,
                isLoading = isLoading,
                onTryAgainClicked = onTryAgainClicked
            )
            is EmptyForecastUiState.BecauseNoSelectedPlace -> EmptyForecastBecauseNoSelectedPlace(
                isLoading = isLoading,
                onPickAPlaceClicked = onPickAPlaceClicked
            )
        }
    }
}

@Composable
fun TodaySummary(
    todaySummaryUiModel: TodaySummaryUiModel,
    outdatedForecast: OutdatedForecastUiState?
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = formatDaySummaryTime(time = todaySummaryUiModel.time),
            style = PrognozaTheme.typography.h4
        )
    }
}