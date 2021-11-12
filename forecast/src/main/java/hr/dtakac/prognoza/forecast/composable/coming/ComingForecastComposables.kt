package hr.dtakac.prognoza.forecast.composable.coming

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import hr.dtakac.prognoza.core.composable.ContentLoader
import hr.dtakac.prognoza.core.model.ui.MeasurementUnit
import hr.dtakac.prognoza.forecast.composable.common.*
import hr.dtakac.prognoza.forecast.uistate.*

@ExperimentalAnimationApi
@Composable
fun ComingForecast(
    forecast: ComingForecastUiState,
    outdatedForecast: OutdatedForecastUiState?,
    isLoading: Boolean,
    emptyForecast: EmptyForecastUiState?,
    expandedInstantIndices: List<Int>,
    onInstantClicked: (Int) -> Unit,
    onTryAgainClicked: () -> Unit,
    onPickAPlaceClicked: () -> Unit,
    preferredUnit: MeasurementUnit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (forecast is ComingForecastUiState.Success) {
            LazyColumn(modifier = Modifier.fillMaxHeight()) {
                ExpandableInstants(
                    instants = forecast.instants,
                    expandedInstantIndices = expandedInstantIndices,
                    preferredUnit = preferredUnit,
                    onInstantClicked = onInstantClicked
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