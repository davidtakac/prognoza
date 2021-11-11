package hr.dtakac.prognoza.forecast.composable.coming

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
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
import com.google.accompanist.flowlayout.FlowRow
import hr.dtakac.prognoza.core.composable.ContentLoader
import hr.dtakac.prognoza.core.formatting.*
import hr.dtakac.prognoza.core.model.ui.MeasurementUnit
import hr.dtakac.prognoza.core.model.ui.RepresentativeWeatherDescription
import hr.dtakac.prognoza.core.theme.PrognozaTheme
import hr.dtakac.prognoza.forecast.R
import hr.dtakac.prognoza.forecast.composable.common.*
import hr.dtakac.prognoza.forecast.model.*
import java.time.ZonedDateTime

@ExperimentalAnimationApi
@Composable
fun ComingForecast(
    forecast: ComingForecastUiModel,
    outdatedForecast: OutdatedForecastUiModel?,
    isLoading: Boolean,
    emptyForecast: EmptyForecastUiModel?,
    expandedHourIndices: List<Int>,
    onDayClicked: (Int) -> Unit,
    onTryAgainClicked: () -> Unit,
    onPickAPlaceClicked: () -> Unit,
    preferredMeasurementUnit: MeasurementUnit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (forecast is ComingForecastUiModel.Success) {
            LazyColumn(modifier = Modifier.fillMaxHeight()) {
                itemsIndexed(forecast.days) { index, day ->
                    if (index == 0 && outdatedForecast != null) {
                        ComingForecastOutdatedMessage(outdatedForecast = outdatedForecast)
                    }
                    ExpandableDay(
                        isExpanded = index in expandedHourIndices,
                        dayUiModel = day,
                        onClick = { onDayClicked(index) },
                        modifier = Modifier.padding(
                            start = 16.dp,
                            end = 16.dp,
                            top = 16.dp,
                            bottom = if (index == forecast.days.lastIndex) 16.dp else 0.dp
                        ),
                        isTomorrow = index == 0,
                        preferredMeasurementUnit = preferredMeasurementUnit
                    )
                    if (index == forecast.days.lastIndex) {
                        MetNorwayOrganizationCredit()
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
fun ExpandableDay(
    isTomorrow: Boolean,
    isExpanded: Boolean,
    dayUiModel: DayUiModel,
    preferredMeasurementUnit: MeasurementUnit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = PrognozaTheme.shapes.medium,
        color = PrognozaTheme.colors.surface,
        contentColor = PrognozaTheme.colors.onSurface,
        elevation = 2.dp,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .animateContentSize()
                .clickable { onClick() }
                .padding(16.dp)
        ) {
            DaySummary(
                time = dayUiModel.time,
                isTomorrow = isTomorrow,
                representativeWeatherDescription = dayUiModel.representativeWeatherDescription,
                totalPrecipitation = dayUiModel.totalPrecipitationAmount,
                lowestTemperature = dayUiModel.lowTemperature,
                highestTemperature = dayUiModel.highTemperature,
                unit = preferredMeasurementUnit
            )
            if (isExpanded) {
                Spacer(Modifier.height(16.dp))
                DayDetails(
                    maxWindSpeed = dayUiModel.maxWindSpeed,
                    maxWindSpeedFromDirection = dayUiModel.windFromCompassDirection,
                    maxHumidity = dayUiModel.maxHumidity,
                    maxPressure = dayUiModel.maxPressure,
                    unit = preferredMeasurementUnit
                )
            }
        }
    }
}

@Composable
fun DaySummary(
    time: ZonedDateTime,
    isTomorrow: Boolean,
    representativeWeatherDescription: RepresentativeWeatherDescription?,
    totalPrecipitation: Double?,
    lowestTemperature: Double?,
    highestTemperature: Double?,
    unit: MeasurementUnit
) {
    CompositionLocalProvider(LocalTextStyle provides PrognozaTheme.typography.subtitle1) {
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Max)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(horizontalAlignment = Alignment.Start) {
                if (isTomorrow) {
                    Text(formatTomorrowTime())
                } else {
                    Text(formatDaySummaryTime(time))
                }
                Spacer(Modifier.height(4.dp))
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    DaySummaryPrecipitation(
                        precipitationMetric = totalPrecipitation,
                        preferredUnit = unit
                    )
                    Text(formatRepresentativeWeatherIconDescription(representativeWeatherDescription))
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = rememberImagePainter(
                        data = representativeWeatherDescription?.weatherDescription?.iconResourceId
                            ?: R.drawable.ic_cloud_off
                    ),
                    contentDescription = null,
                    modifier = Modifier.size(64.dp)
                )
                Spacer(Modifier.width(16.dp))
                DaySummaryLowestAndHighestTemperature(
                    lowestTemperature = lowestTemperature,
                    highestTemperature = highestTemperature,
                    unit = unit
                )
            }
        }
    }
}

@Composable
fun DaySummaryLowestAndHighestTemperature(
    lowestTemperature: Double?,
    highestTemperature: Double?,
    unit: MeasurementUnit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.End
    ) {
        Text(
            formatTemperatureValue(
                temperature = highestTemperature,
                unit = unit
            )
        )
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

@Composable
fun DayDetails(
    maxWindSpeed: Double?,
    maxWindSpeedFromDirection: Int?,
    maxHumidity: Double?,
    maxPressure: Double?,
    unit: MeasurementUnit
) {
    CompositionLocalProvider(LocalTextStyle provides PrognozaTheme.typography.subtitle2) {
        FlowRow(
            mainAxisSpacing = 8.dp,
            crossAxisSpacing = 8.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            DetailsItem(
                iconId = R.drawable.ic_air,
                labelId = R.string.max_wind,
                text = formatWindWithDirection(
                    windSpeed = maxWindSpeed,
                    windFromCompassDirection = maxWindSpeedFromDirection,
                    windSpeedUnit = unit
                )
            )
            DetailsItem(
                iconId = R.drawable.ic_water_drop,
                labelId = R.string.max_humidity,
                text = formatHumidityValue(
                    relativeHumidity = maxHumidity
                )
            )
            DetailsItem(
                iconId = R.drawable.ic_speed,
                labelId = R.string.max_pressure,
                text = formatPressureValue(
                    pressure = maxPressure,
                    unit = unit
                )
            )
        }
    }
}

@Composable
fun ComingForecastOutdatedMessage(outdatedForecast: OutdatedForecastUiModel) {
    var showDialog by remember { mutableStateOf(false) }
    Surface(
        shape = PrognozaTheme.shapes.medium,
        color = PrognozaTheme.colors.surface,
        contentColor = PrognozaTheme.colors.onSurface,
        elevation = 2.dp,
        modifier = Modifier
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = 16.dp
            )
            .fillMaxWidth()
    ) {
        OutdatedForecastMessage(
            outdatedForecastUiModel = outdatedForecast,
            showDialog = showDialog,
            modifier = Modifier
                .clickable { showDialog = true }
                .padding(16.dp),
            onDialogConfirm = { showDialog = false },
            onDialogDismiss = { showDialog = false }
        )
    }
}