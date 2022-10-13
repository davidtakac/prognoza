package hr.dtakac.prognoza.widget

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.color.ColorProviders
import androidx.glance.layout.*
import androidx.glance.text.FontStyle
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.entities.forecast.units.TemperatureUnit
import hr.dtakac.prognoza.presentation.asGlanceString
import hr.dtakac.prognoza.presentation.forecast.getShortTime
import hr.dtakac.prognoza.presentation.forecast.getTemperature
import hr.dtakac.prognoza.presentation.forecast.toDrawableId

@Composable
fun EmptyWidget(colors: ColorProviders) {
    Text(
        // Glance does not support stringResource
        text = LocalContext.current.getString(R.string.widget_empty),
        style = TextStyle(color = colors.onSurface, fontSize = 14.sp)
    )
}

@Composable
fun PlaceAndTempWidget(
    placeName: String,
    currentTemperature: String,
    colors: ColorProviders,
    placeSize: TextUnit = 16.sp,
    temperatureSize: TextUnit = 32.sp,
    modifier: GlanceModifier = GlanceModifier
) {
    Column(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Text(
            placeName,
            style = TextStyle(
                color = colors.onSurface,
                fontSize = placeSize,
                fontWeight = FontWeight.Normal,
                fontStyle = FontStyle.Normal
            ),
            maxLines = 1
        )
        Text(
            currentTemperature,
            style = TextStyle(
                color = colors.onSurface,
                fontSize = temperatureSize,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Normal
            ),
            maxLines = 1
        )
    }
}

@Composable
fun PlaceAndTempAndIconWidget(
    placeName: String,
    currentTemperature: String,
    @DrawableRes
    iconResId: Int,
    colors: ColorProviders,
    modifier: GlanceModifier = GlanceModifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        PlaceAndTempWidget(
            placeName = placeName,
            currentTemperature = currentTemperature,
            colors = colors,
            modifier = GlanceModifier.defaultWeight()
        )
        Image(
            provider = ImageProvider(iconResId),
            contentDescription = null,
            modifier = GlanceModifier.size(64.dp)
        )
    }
}

@Composable
fun PlaceAndTempAndIconAndHoursWidget(
    placeName: String,
    currentTemperature: String,
    @DrawableRes
    iconResId: Int,
    hours: List<WidgetHour>,
    temperatureUnit: TemperatureUnit,
    colors: ColorProviders,
    modifier: GlanceModifier = GlanceModifier
) {
    Column(modifier = modifier) {
        PlaceAndTempAndIconWidget(
            placeName = placeName,
            currentTemperature = currentTemperature,
            iconResId = iconResId,
            colors = colors,
            modifier = GlanceModifier.fillMaxWidth()
        )
        Spacer(modifier = GlanceModifier.height(16.dp))
        HoursRow(
            data = hours,
            temperatureUnit = temperatureUnit,
            colors = colors,
            modifier = GlanceModifier.fillMaxWidth()
        )
    }
}

@Composable
private fun HoursRow(
    data: List<WidgetHour>,
    temperatureUnit: TemperatureUnit,
    colors: ColorProviders,
    modifier: GlanceModifier = GlanceModifier
) {
    Row(modifier = modifier) {
        data.forEachIndexed { idx, hour ->
            val temperature = getTemperature(
                temperature = hour.temperature,
                unit = temperatureUnit
            ).asGlanceString()
            val iconResId = hour.description.toDrawableId()
            val time = getShortTime(time = hour.dateTime).asGlanceString()

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = when (idx) {
                    0 -> GlanceModifier.padding(end = 8.dp)
                    data.lastIndex -> GlanceModifier.padding(start = 8.dp)
                    else -> GlanceModifier.padding(horizontal = 8.dp)
                }
            ) {
                Text(
                    text = temperature,
                    maxLines = 1,
                    style = TextStyle(
                        color = colors.onSurface,
                        fontSize = 14.sp
                    ),
                    modifier = GlanceModifier.padding(bottom = 2.dp)
                )
                Image(
                    provider = ImageProvider(iconResId),
                    contentDescription = null,
                    modifier = GlanceModifier.size(32.dp)
                )
                Text(
                    text = time,
                    maxLines = 1,
                    style = TextStyle(
                        color = colors.onSurface,
                        fontSize = 14.sp
                    ),
                    modifier = GlanceModifier.padding(top = 2.dp)
                )
            }
        }
    }
}