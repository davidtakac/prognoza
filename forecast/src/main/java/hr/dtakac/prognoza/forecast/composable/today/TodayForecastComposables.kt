package hr.dtakac.prognoza.forecast.composable.today

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import hr.dtakac.prognoza.core.composable.ContentLoader
import hr.dtakac.prognoza.core.formatting.formatCurrentHourHeaderTime
import hr.dtakac.prognoza.core.formatting.formatFeelsLike
import hr.dtakac.prognoza.core.formatting.formatTemperatureValue
import hr.dtakac.prognoza.core.formatting.formatWeatherIconDescription
import hr.dtakac.prognoza.core.model.ui.MeasurementUnit
import hr.dtakac.prognoza.core.model.ui.WeatherDescription
import hr.dtakac.prognoza.core.theme.PrognozaTheme
import hr.dtakac.prognoza.core.utils.precipitationExists
import hr.dtakac.prognoza.forecast.R
import hr.dtakac.prognoza.forecast.composable.common.*
import hr.dtakac.prognoza.forecast.model.*

@ExperimentalAnimationApi
@Composable
fun TodayForecast(
    forecast: TodayForecastUiModel,
    outdatedForecast: OutdatedForecastUiModel?,
    isLoading: Boolean,
    emptyForecast: EmptyForecastUiModel?,
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
        if (forecast is TodayForecastUiModel.Success) {
            LazyColumn(Modifier.fillMaxHeight()) {
                item {
                    CurrentHourHeader(
                        currentHour = forecast.currentHour,
                        outdatedForecastUiModel = outdatedForecast,
                        preferredUnit = preferredUnit
                    )
                }
                ExpandableHours(
                    hours = forecast.otherHours,
                    expandedHourIndices = expandedHourIndices,
                    preferredUnit = preferredUnit,
                    onHourClicked = { onHourClicked(it) }
                )
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
fun CurrentHourHeader(
    currentHour: HourUiModel,
    outdatedForecastUiModel: OutdatedForecastUiModel?,
    preferredUnit: MeasurementUnit
) {
    Surface(
        shape = PrognozaTheme.shapes.medium,
        color = PrognozaTheme.colors.surface,
        contentColor = PrognozaTheme.colors.onSurface,
        elevation = 2.dp,
        modifier = Modifier.padding(
            start = 16.dp,
            end = 16.dp,
            top = 16.dp,
            bottom = 8.dp
        )
    ) {
        CompositionLocalProvider(LocalTextStyle provides PrognozaTheme.typography.subtitle1) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column(horizontalAlignment = Alignment.Start) {
                    Text(formatCurrentHourHeaderTime(currentHour.time))
                    Text(
                        text = formatTemperatureValue(
                            temperature = currentHour.temperature,
                            unit = preferredUnit
                        ),
                        style = PrognozaTheme.typography.h3
                    )
                    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                        CurrentHourHeaderDescription(
                            precipitation = currentHour.precipitationAmount,
                            weatherDescription = currentHour.weatherDescription,
                            unit = preferredUnit
                        )
                        Text(
                            formatFeelsLike(
                                feelsLike = currentHour.feelsLike,
                                unit = preferredUnit
                            )
                        )
                    }
                }
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    Image(
                        painter = rememberImagePainter(
                            currentHour.weatherDescription?.iconResourceId
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
}

@Composable
fun CurrentHourHeaderDescription(
    precipitation: Double?,
    weatherDescription: WeatherDescription?,
    unit: MeasurementUnit
) {
    if (precipitationExists(precipitation)) {
        TodayForecastHeaderPrecipitation(
            precipitationMetric = precipitation,
            preferredUnit = unit
        )
    } else {
        Text(formatWeatherIconDescription(weatherDescription?.descriptionResourceId))
    }
}