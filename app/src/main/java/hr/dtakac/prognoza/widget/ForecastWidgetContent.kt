package hr.dtakac.prognoza.widget

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
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
fun PlaceTempWidget(
    placeName: String,
    currentTemperature: String,
    colors: ColorProviders,
    modifier: GlanceModifier = GlanceModifier
) {
    Column(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            placeName,
            style = TextStyle(
                color = colors.onSurface,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                fontStyle = FontStyle.Normal
            ),
            maxLines = 1
        )
        Text(
            currentTemperature,
            style = TextStyle(
                color = colors.onSurface,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Normal
            ),
            maxLines = 1
        )
    }
}

@Composable
fun PlaceTempIconWidget(
    placeName: String,
    currentTemperature: String,
    @DrawableRes
    iconResId: Int,
    colors: ColorProviders,
    modifier: GlanceModifier = GlanceModifier
) {
    Column(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            placeName,
            style = TextStyle(
                color = colors.onSurface,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                fontStyle = FontStyle.Normal
            ),
            maxLines = 1
        )
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                currentTemperature,
                style = TextStyle(
                    color = colors.onSurface,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Normal
                ),
                maxLines = 1
            )
            Image(
                provider = ImageProvider(iconResId),
                contentDescription = null,
                modifier = GlanceModifier.size(48.dp)
            )
        }
    }
}

@Composable
fun PlaceTempIconHoursWidget(
    placeName: String,
    currentTemperature: String,
    @DrawableRes
    iconResId: Int,
    hours: List<WidgetHour>,
    temperatureUnit: TemperatureUnit,
    colors: ColorProviders,
    modifier: GlanceModifier = GlanceModifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            placeName,
            style = TextStyle(
                color = colors.onSurface,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                fontStyle = FontStyle.Normal
            ),
            maxLines = 1
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                currentTemperature,
                style = TextStyle(
                    color = colors.onSurface,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Normal
                ),
                maxLines = 1
            )
            Image(
                provider = ImageProvider(iconResId),
                contentDescription = null,
                modifier = GlanceModifier.size(48.dp)
            )
        }
        Spacer(modifier = GlanceModifier.height(16.dp))
        HoursRow(
            data = hours,
            temperatureUnit = temperatureUnit,
            colors = colors
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
                    0 -> GlanceModifier.padding(end = 6.dp)
                    data.lastIndex -> GlanceModifier.padding(start = 6.dp)
                    else -> GlanceModifier.padding(horizontal = 6.dp)
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