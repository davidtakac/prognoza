package hr.dtakac.prognoza.forecast.composable.tomorrow

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import hr.dtakac.prognoza.core.composable.ContentLoader
import hr.dtakac.prognoza.core.formatting.formatDaySummaryTime
import hr.dtakac.prognoza.core.formatting.formatRepresentativeWeatherIconDescription
import hr.dtakac.prognoza.core.formatting.formatTemperatureValue
import hr.dtakac.prognoza.core.model.ui.MeasurementUnit
import hr.dtakac.prognoza.core.theme.PrognozaTheme
import hr.dtakac.prognoza.forecast.R
import hr.dtakac.prognoza.forecast.composable.common.*
import hr.dtakac.prognoza.forecast.model.*

@ExperimentalAnimationApi
@Composable
fun TomorrowForecast(
    forecast: TomorrowForecastUiModel,
    outdatedForecast: OutdatedForecastUiModel?,
    isLoading: Boolean,
    emptyForecast: EmptyForecastUiModel?,
    expandedHourIndices: List<Int>,
    onHourClicked: (Int) -> Unit,
    onTryAgainClicked: () -> Unit,
    onPickAPlaceClicked: () -> Unit,
    preferredMeasurementUnit: MeasurementUnit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (forecast is TomorrowForecastUiModel.Success) {
            LazyColumn(Modifier.fillMaxHeight()) {
                item {
                    TomorrowSummaryHeader(
                        dayUiModel = forecast.summary,
                        outdatedForecastUiModel = outdatedForecast,
                        preferredUnit = preferredMeasurementUnit
                    )
                }
                itemsIndexed(forecast.hours) { index, hour ->
                    ExpandableHour(
                        hour = hour,
                        isExpanded = index in expandedHourIndices,
                        onClick = { onHourClicked(index) },
                        preferredMeasurementUnit = preferredMeasurementUnit
                    )
                    if (index == forecast.hours.lastIndex) {
                        MetNorwayOrganizationCredit()
                    } else {
                        Divider()
                    }
                }
            }
        }
        ContentLoader(isLoading = isLoading)
        when (emptyForecast) {
            is EmptyForecastBecauseReason -> EmptyForecastBecauseReason(
                emptyForecast = emptyForecast,
                isLoading = isLoading,
                onTryAgainClicked = onTryAgainClicked
            )
            is EmptyForecastBecauseNoSelectedPlace -> EmptyForecastBecauseNoSelectedPlace(
                isLoading = isLoading,
                onPickAPlaceClicked = onPickAPlaceClicked
            )
        }
    }
}

@Composable
fun TomorrowSummaryHeader(
    dayUiModel: DayUiModel,
    outdatedForecastUiModel: OutdatedForecastUiModel?,
    preferredUnit: MeasurementUnit
) {
    Surface(
        shape = PrognozaTheme.shapes.medium,
        color = PrognozaTheme.colors.surface,
        contentColor = PrognozaTheme.colors.onSurface,
        elevation = 2.dp,
        modifier = Modifier.padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(horizontalAlignment = Alignment.Start) {
                Text(formatDaySummaryTime(dayUiModel.time))
                CompositionLocalProvider(LocalTextStyle provides PrognozaTheme.typography.h4) {
                    TomorrowLowestAndHighestTemperature(
                        lowestTemperature = dayUiModel.lowTemperature,
                        highestTemperature = dayUiModel.highTemperature,
                        unit = preferredUnit
                    )
                }
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    DaySummaryPrecipitation(
                        precipitationMetric = dayUiModel.totalPrecipitationAmount,
                        preferredUnit = preferredUnit
                    )
                    Text(formatRepresentativeWeatherIconDescription(dayUiModel.representativeWeatherDescription))
                }
            }
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxHeight()
            ) {
                Image(
                    painter = rememberImagePainter(
                        dayUiModel.representativeWeatherDescription?.weatherDescription?.iconResourceId
                            ?: R.drawable.ic_cloud_off
                    ),
                    contentDescription = null,
                    modifier = Modifier.size(86.dp)
                )
                if (outdatedForecastUiModel != null) {
                    var showDialog by remember { mutableStateOf(false) }
                    OutdatedForecastMessage(
                        outdatedForecastUiModel = outdatedForecastUiModel,
                        showDialog = showDialog,
                        modifier = Modifier.clickable { showDialog = true },
                        onDialogConfirm = { showDialog = false },
                        onDialogDismiss = { showDialog = false }
                    )
                }
            }
        }
    }
}

@Composable
fun TomorrowLowestAndHighestTemperature(
    lowestTemperature: Double?,
    highestTemperature: Double?,
    unit: MeasurementUnit
) {
    Row {
        Text(
            formatTemperatureValue(
                temperature = highestTemperature,
                unit = unit
            )
        )
        Spacer(Modifier.width(8.dp))
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Text(
                formatTemperatureValue(
                    temperature = lowestTemperature,
                    unit = unit
                )
            )
        }
    }
}