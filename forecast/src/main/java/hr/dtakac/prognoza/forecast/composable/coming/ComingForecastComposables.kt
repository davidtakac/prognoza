package hr.dtakac.prognoza.forecast.composable.coming

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.google.accompanist.flowlayout.FlowRow
import hr.dtakac.prognoza.core.formatting.*
import hr.dtakac.prognoza.core.model.ui.MeasurementUnit
import hr.dtakac.prognoza.core.model.ui.RepresentativeWeatherDescription
import hr.dtakac.prognoza.core.theme.PrognozaTheme
import hr.dtakac.prognoza.forecast.R
import hr.dtakac.prognoza.forecast.composable.common.*
import hr.dtakac.prognoza.forecast.model.*
import java.time.ZonedDateTime

@Composable
fun ComingForecast(
    forecast: ComingForecastUiModel?,
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
        if (forecast != null) {
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
        if (isLoading) {
            CircularProgressIndicator()
        }
        if (emptyForecast != null) {
            if (emptyForecast is EmptyForecastBecauseReason) {
                val errorText = stringResource(
                    id = R.string.template_error_forecast_empty_reason,
                    stringResource(id = emptyForecast.reason ?: R.string.error_generic)
                )
                EmptyForecast(text = errorText) {
                    RefreshButton(
                        text = stringResource(id = R.string.button_try_again),
                        isLoading = isLoading,
                        onClick = onTryAgainClicked
                    )
                }
            } else {
                val errorText = stringResource(id = R.string.error_forecast_empty_no_selected_place)
                EmptyForecast(text = errorText) {
                    Button(onClick = onPickAPlaceClicked) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = stringResource(id = R.string.pick_a_place))
                        }
                    }
                }
            }
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
                Spacer(modifier = Modifier.height(16.dp))
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
    Row(
        modifier = Modifier
            .height(IntrinsicSize.Max)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(
            horizontalAlignment = Alignment.Start
        ) {
            if (isTomorrow) {
                TomorrowTime()
            } else {
                DaySummaryTime(time = time)
            }
            Spacer(modifier = Modifier.height(4.dp))
            TotalPrecipitation(totalPrecipitation = totalPrecipitation, unit = unit)
            RepresentativeWeatherDescription(representativeWeatherDescription = representativeWeatherDescription)
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberImagePainter(
                    data = representativeWeatherDescription?.weatherDescription?.iconResourceId
                        ?: R.drawable.ic_cloud_off
                ),
                contentDescription = null,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            DaySummaryLowestAndHighestTemperature(
                lowestTemperature = lowestTemperature,
                highestTemperature = highestTemperature,
                unit = unit
            )
        }
    }
}

@Composable
fun DaySummaryLowestAndHighestTemperature(
    lowestTemperature: Double?,
    highestTemperature: Double?,
    unit: MeasurementUnit
) {
    val typography = PrognozaTheme.typography.subtitle1
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.End
    ) {
        Text(
            text = formatTemperatureValue(
                temperature = highestTemperature,
                unit = unit
            ),
            style = typography,
            color = PrognozaTheme.textColors.highEmphasis
        )
        Text(
            text = formatTemperatureValue(
                temperature = lowestTemperature,
                unit = unit
            ),
            style = typography,
            color = PrognozaTheme.textColors.mediumEmphasis
        )
    }
}

@Composable
fun TomorrowTime() {
    Text(
        text = getTomorrowTime(),
        style = PrognozaTheme.typography.subtitle1,
        color = PrognozaTheme.textColors.highEmphasis
    )
}

@Composable
fun DayDetails(
    maxWindSpeed: Double?,
    maxWindSpeedFromDirection: Int?,
    maxHumidity: Double?,
    maxPressure: Double?,
    unit: MeasurementUnit
) {
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