package hr.dtakac.prognoza.composable.common

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.google.accompanist.flowlayout.FlowRow
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.common.utils.ComposeStringFormatting
import hr.dtakac.prognoza.model.ui.MeasurementUnit
import hr.dtakac.prognoza.model.ui.WeatherDescription
import hr.dtakac.prognoza.model.ui.cell.HourUiModel
import hr.dtakac.prognoza.theme.AppTheme
import java.time.ZonedDateTime

@Composable
fun ExpandableHour(
    isExpanded: Boolean,
    hour: HourUiModel,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .animateContentSize()
            .clickable { onClick.invoke() }
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
            unit = hour.displayDataInUnit
        )
        if (isExpanded) {
            Spacer(modifier = Modifier.height(8.dp))
            HourDetails(
                feelsLike = hour.feelsLike,
                windSpeed = hour.windSpeed,
                windFromCompassDirection = hour.windFromCompassDirection,
                pressure = hour.airPressureAtSeaLevel,
                humidity = hour.relativeHumidity,
                unit = hour.displayDataInUnit
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
                text = ComposeStringFormatting.formatHourTime(time = time),
                style = AppTheme.typography.subtitle1,
                color = AppTheme.textColors.highEmphasis
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = ComposeStringFormatting.formatPrecipitationValue(
                    precipitation = precipitation,
                    unit = unit
                ),
                style = AppTheme.typography.subtitle2
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = ComposeStringFormatting.formatTemperatureValue(
                    temperature = temperature,
                    unit = unit
                ),
                style = AppTheme.typography.subtitle1,
                color = AppTheme.textColors.highEmphasis
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
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        FlowRow(
            mainAxisSpacing = 8.dp,
            crossAxisSpacing = 8.dp
        ) {
            DetailsItem(
                iconId = R.drawable.ic_thermostat,
                labelId = R.string.feels_like,
                text = ComposeStringFormatting.formatTemperatureValue(
                    temperature = feelsLike,
                    unit = unit
                )
            )
            DetailsItem(
                iconId = R.drawable.ic_air,
                labelId = R.string.wind,
                text = ComposeStringFormatting.formatWindWithDirection(
                    windSpeed = windSpeed,
                    windFromCompassDirection = windFromCompassDirection,
                    windSpeedUnit = unit
                )
            )
            DetailsItem(
                iconId = R.drawable.ic_water_drop,
                labelId = R.string.humidity,
                text = ComposeStringFormatting.formatHumidityValue(
                    relativeHumidity = humidity
                )
            )
            DetailsItem(
                iconId = R.drawable.ic_speed,
                labelId = R.string.pressure,
                text = ComposeStringFormatting.formatPressureValue(
                    pressure = pressure,
                    unit = unit
                )
            )
        }
    }
}

@Composable
fun DetailsItem(
    @DrawableRes iconId: Int,
    @StringRes labelId: Int,
    text: String
) {
    Surface(
        shape = AppTheme.shapes.small,
        color = AppTheme.colors.surface,
        contentColor = AppTheme.colors.onSurface,
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
                DetailsIcon(id = iconId)
                Spacer(modifier = Modifier.width(4.dp))
                DetailsLabel(id = labelId)
            }
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = text,
                style = AppTheme.typography.subtitle2,
                color = AppTheme.textColors.highEmphasis
            )
        }
    }
}

@Composable
fun DetailsIcon(@DrawableRes id: Int) {
    Image(
        painter = rememberImagePainter(data = id),
        contentDescription = null,
        modifier = Modifier.size(size = 18.dp),
        colorFilter = ColorFilter.tint(color = AppTheme.textColors.highEmphasis)
    )
}

@Composable
fun DetailsLabel(@StringRes id: Int) {
    Text(
        text = stringResource(id = id),
        style = AppTheme.typography.subtitle2,
        color = AppTheme.textColors.mediumEmphasis
    )
}