package hr.dtakac.prognoza.forecast.composable.common

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.google.accompanist.flowlayout.FlowRow
import hr.dtakac.prognoza.core.formatting.*
import hr.dtakac.prognoza.core.model.ui.MeasurementUnit
import hr.dtakac.prognoza.core.model.ui.WeatherDescription
import hr.dtakac.prognoza.core.theme.PrognozaTheme
import hr.dtakac.prognoza.core.utils.contentAlphaForPrecipitation
import hr.dtakac.prognoza.forecast.R
import hr.dtakac.prognoza.forecast.model.HourUiModel
import java.time.ZonedDateTime

@Composable
fun ExpandableHour(
    isExpanded: Boolean,
    hour: HourUiModel,
    preferredMeasurementUnit: MeasurementUnit,
    onClick: () -> Unit
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
            unit = preferredMeasurementUnit
        )
        if (isExpanded) {
            Spacer(modifier = Modifier.height(8.dp))
            HourDetails(
                feelsLike = hour.feelsLike,
                windSpeed = hour.windSpeed,
                windFromCompassDirection = hour.windFromCompassDirection,
                pressure = hour.airPressureAtSeaLevel,
                humidity = hour.relativeHumidity,
                unit = preferredMeasurementUnit
            )
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
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = formatHourTime(time = time),
                style = PrognozaTheme.typography.subtitle1
            )
            Spacer(modifier = Modifier.width(16.dp))
            CompositionLocalProvider(
                LocalContentAlpha provides contentAlphaForPrecipitation(
                    precipitation = precipitation,
                    unit = unit
                )
            ) {
                Text(
                    text = formatPrecipitationValue(
                        precipitation = precipitation,
                        unit = unit
                    ),
                    style = PrognozaTheme.typography.subtitle2
                )
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = formatTemperatureValue(
                    temperature = temperature,
                    unit = unit
                ),
                style = PrognozaTheme.typography.subtitle1
            )
            Spacer(modifier = Modifier.width(16.dp))
            Image(
                painter = rememberImagePainter(
                    data = weatherDescription?.iconResourceId
                        ?: R.drawable.ic_cloud_off
                ),
                contentDescription = null,
                modifier = Modifier.size(size = 36.dp)
            )
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

@Composable
fun DetailsItem(
    @DrawableRes iconId: Int,
    @StringRes labelId: Int,
    text: String
) {
    Surface(
        shape = PrognozaTheme.shapes.small,
        color = PrognozaTheme.colors.surface,
        elevation = 2.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(
                top = 8.dp,
                bottom = 8.dp,
                start = 12.dp,
                end = 12.dp
            )
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = iconId),
                    contentDescription = null,
                    modifier = Modifier.size(size = 18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Text(
                        text = stringResource(id = labelId),
                        style = PrognozaTheme.typography.subtitle2
                    )
                }
            }
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = text,
                style = PrognozaTheme.typography.subtitle2
            )
        }
    }
}