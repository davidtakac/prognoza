package hr.dtakac.prognoza.forecast.composable.common

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.google.accompanist.flowlayout.FlowRow
import hr.dtakac.prognoza.core.formatting.*
import hr.dtakac.prognoza.core.model.ui.MeasurementUnit
import hr.dtakac.prognoza.core.model.ui.WeatherDescription
import hr.dtakac.prognoza.core.theme.PrognozaTheme
import hr.dtakac.prognoza.core.utils.isSameHourAsNow
import hr.dtakac.prognoza.forecast.R
import hr.dtakac.prognoza.forecast.model.InstantUiModel
import java.time.ZonedDateTime

fun LazyListScope.ExpandableInstants(
    instants: List<InstantUiModel>,
    expandedInstantIndices: List<Int>,
    preferredUnit: MeasurementUnit,
    onInstantClicked: (Int) -> Unit
) {
    itemsIndexed(instants) { index, hour ->
        ExpandableInstant(
            instant = hour,
            isExpanded = index in expandedInstantIndices,
            onClick = { onInstantClicked(index) },
            modifier = Modifier.padding(
                start = 16.dp,
                end = 16.dp,
                top = 8.dp,
                bottom = if (index == instants.lastIndex) 8.dp else 0.dp
            ),
            preferredUnit = preferredUnit
        )
        if (index == instants.lastIndex) {
            MetNorwayOrganizationCredit()
        }
    }
}

@Composable
fun ExpandableInstant(
    isExpanded: Boolean,
    instant: InstantUiModel,
    preferredUnit: MeasurementUnit,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier,
        elevation = 2.dp,
        shape = PrognozaTheme.shapes.medium,
        color = PrognozaTheme.colors.surface
    ) {
        Column(
            modifier = Modifier
                .animateContentSize()
                .clickable { onClick() }
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 8.dp,
                    bottom = 8.dp
                )
        ) {
            InstantSummary(
                modifier = Modifier.fillMaxWidth(),
                time = instant.time,
                nextInstantTime = instant.nextInstantTime,
                showNextInstantTime = instant.showNextInstantTime,
                precipitation = instant.precipitationAmount,
                temperature = instant.temperature,
                weatherDescription = instant.weatherDescription,
                unit = preferredUnit
            )
            if (isExpanded) {
                Spacer(Modifier.height(8.dp))
                InstantDetails(
                    feelsLike = instant.feelsLike,
                    windSpeed = instant.windSpeed,
                    windFromCompassDirection = instant.windCompassDirectionId,
                    pressure = instant.airPressure,
                    humidity = instant.relativeHumidity,
                    unit = preferredUnit
                )
            }
        }
    }
}

@Composable
fun InstantSummary(
    modifier: Modifier,
    time: ZonedDateTime,
    nextInstantTime: ZonedDateTime?,
    showNextInstantTime: Boolean,
    precipitation: Double?,
    temperature: Double?,
    weatherDescription: WeatherDescription?,
    unit: MeasurementUnit
) {
    CompositionLocalProvider(LocalTextStyle provides PrognozaTheme.typography.subtitle1) {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                val firstTime =
                    if (time.isSameHourAsNow()) formatNowTime() else formatHourTime(time)
                val secondTime = if (showNextInstantTime && nextInstantTime != null) formatHourTime(
                    nextInstantTime
                ) else null
                Text(
                    secondTime?.let {
                        stringResource(id = R.string.template_time_span, firstTime, secondTime)
                    } ?: firstTime
                )
                Spacer(Modifier.width(16.dp))
                CompositionLocalProvider(
                    LocalTextStyle provides PrognozaTheme.typography.subtitle2,
                    LocalContentAlpha provides ContentAlpha.medium
                ) {
                    HourSummaryPrecipitation(
                        precipitationMetric = precipitation,
                        preferredUnit = unit
                    )
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    formatTemperatureValue(
                        temperature = temperature,
                        unit = unit
                    )
                )
                Spacer(Modifier.width(16.dp))
                Image(
                    painter = rememberImagePainter(
                        weatherDescription?.iconId ?: R.drawable.ic_cloud_off
                    ),
                    contentDescription = null,
                    modifier = Modifier.size(36.dp)
                )
            }
        }
    }
}

@Composable
fun InstantDetails(
    feelsLike: Double?,
    windSpeed: Double?,
    windFromCompassDirection: Int?,
    pressure: Double?,
    humidity: Double?,
    unit: MeasurementUnit
) {
    CompositionLocalProvider(LocalTextStyle provides PrognozaTheme.typography.subtitle2) {
        FlowRow(
            mainAxisSpacing = 8.dp,
            crossAxisSpacing = 8.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            DetailsItem(
                iconId = R.drawable.ic_thermostat,
                labelId = R.string.feels_like,
                text = formatTemperatureValue(
                    temperature = feelsLike,
                    unit = unit
                )
            )
            DetailsItem(
                iconId = R.drawable.ic_air,
                labelId = R.string.wind,
                text = formatWindWithDirection(
                    windSpeed = windSpeed,
                    windFromCompassDirection = windFromCompassDirection,
                    windSpeedUnit = unit
                )
            )
            DetailsItem(
                iconId = R.drawable.ic_water_drop,
                labelId = R.string.humidity,
                text = formatHumidityValue(
                    relativeHumidity = humidity
                )
            )
            DetailsItem(
                iconId = R.drawable.ic_speed,
                labelId = R.string.pressure,
                text = formatPressureValue(
                    pressure = pressure,
                    unit = unit
                )
            )
        }
    }
}