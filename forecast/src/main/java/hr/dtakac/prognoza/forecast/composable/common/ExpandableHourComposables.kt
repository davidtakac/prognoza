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
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.google.accompanist.flowlayout.FlowRow
import hr.dtakac.prognoza.core.formatting.*
import hr.dtakac.prognoza.core.model.ui.MeasurementUnit
import hr.dtakac.prognoza.core.model.ui.WeatherDescription
import hr.dtakac.prognoza.core.theme.PrognozaTheme
import hr.dtakac.prognoza.core.utils.isSameHourAsNow
import hr.dtakac.prognoza.forecast.R
import hr.dtakac.prognoza.forecast.model.HourUiModel
import java.time.ZonedDateTime

fun LazyListScope.ExpandableHours(
    hours: List<HourUiModel>,
    expandedHourIndices: List<Int>,
    preferredUnit: MeasurementUnit,
    onHourClicked: (Int) -> Unit
) {
    itemsIndexed(hours) { index, hour ->
        ExpandableHour(
            hour = hour,
            isExpanded = index in expandedHourIndices,
            onClick = { onHourClicked(index) },
            modifier = Modifier.padding(
                start = 16.dp,
                end = 16.dp,
                top = 8.dp,
                bottom = if (index == hours.lastIndex) 8.dp else 0.dp
            ),
            preferredUnit = preferredUnit
        )
        if (index == hours.lastIndex) {
            MetNorwayOrganizationCredit()
        }
    }
}

@Composable
fun ExpandableHour(
    isExpanded: Boolean,
    hour: HourUiModel,
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
            HourSummary(
                modifier = Modifier.fillMaxWidth(),
                time = hour.time,
                precipitation = hour.precipitationAmount,
                temperature = hour.temperature,
                weatherDescription = hour.weatherDescription,
                unit = preferredUnit
            )
            if (isExpanded) {
                Spacer(Modifier.height(8.dp))
                HourDetails(
                    feelsLike = hour.feelsLike,
                    windSpeed = hour.windSpeed,
                    windFromCompassDirection = hour.windFromCompassDirection,
                    pressure = hour.airPressureAtSeaLevel,
                    humidity = hour.relativeHumidity,
                    unit = preferredUnit
                )
            }
        }
    }
}

@Composable
fun HourSummary(
    modifier: Modifier,
    time: ZonedDateTime,
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
                Text(if (time.isSameHourAsNow()) formatNowTime() else formatHourTime(time))
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
                        weatherDescription?.iconResourceId ?: R.drawable.ic_cloud_off
                    ),
                    contentDescription = null,
                    modifier = Modifier.size(36.dp)
                )
            }
        }
    }
}

@Composable
fun HourDetails(
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