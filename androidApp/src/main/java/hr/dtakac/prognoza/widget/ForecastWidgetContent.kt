package hr.dtakac.prognoza.widget

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.*
import androidx.glance.action.Action
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.CircularProgressIndicator
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.color.ColorProviders
import androidx.glance.color.dynamicThemeColorProviders
import androidx.glance.layout.*
import androidx.glance.text.FontStyle
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.presentation.TextResource
import hr.dtakac.prognoza.presentation.asGlanceString
import hr.dtakac.prognoza.presentation.forecast.getTemperature
import hr.dtakac.prognoza.shared.entity.TemperatureUnit
import hr.dtakac.prognoza.ui.MainActivity
import hr.dtakac.prognoza.ui.theme.asGlanceWeatherIconResId
import kotlin.math.floor

@Composable
fun ForecastWidgetContent(
    state: ForecastWidgetState = currentState(),
    colors: ColorProviders = dynamicThemeColorProviders(),
    size: DpSize = LocalSize.current,
    onClick: Action = actionStartActivity<MainActivity>()
) {
    Box(
        modifier = GlanceModifier
            .appWidgetBackgroundRadius()
            .background(colors.surface)
            .appWidgetBackground()
            .padding(8.dp)
            .fillMaxSize()
    ) {
        when (state) {
            ForecastWidgetState.Error,
            ForecastWidgetState.Unavailable -> EmptyWidget(colors)
            ForecastWidgetState.Loading -> LoadingWidget()
            is ForecastWidgetState.Success -> SuccessWidget(
                state = state,
                colors = colors,
                size = size
            )
        }
        // This box is a workaround to make the entire widget clickable
        Box(modifier = GlanceModifier.fillMaxSize().clickable(onClick)) {}
    }
}


@Composable
private fun EmptyWidget(colors: ColorProviders) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = GlanceModifier.fillMaxSize()
    ) {
        Text(
            // Glance does not support stringResource
            text = LocalContext.current.getString(R.string.widget_empty),
            style = TextStyle(color = colors.onSurface, fontSize = 14.sp)
        )
    }
}

@Composable
private fun LoadingWidget() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = GlanceModifier.fillMaxSize()
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun SuccessWidget(
    state: ForecastWidgetState.Success,
    colors: ColorProviders,
    size: DpSize
) {
    val placeName = state.placeName
    val temperatureUnit = state.temperatureUnit
    val icon = state.description.asGlanceWeatherIconResId()
    val currentTemperature = getTemperature(
        temperature = state.temperature,
        unit = temperatureUnit
    ).asGlanceString()

    when (size) {
        ForecastWidget.tiny -> TinyWidget(
            iconResId = icon,
            currentTemperature = currentTemperature,
            textColor = colors.onSurface,
            modifier = GlanceModifier.fillMaxSize()
        )
        ForecastWidget.small -> SmallWidget(
            placeName = placeName,
            currentTemperature = currentTemperature,
            iconResId = icon,
            textColor = colors.onSurface,
            modifier = GlanceModifier.fillMaxSize()
        )
        else -> NormalWidget(
            placeName = placeName,
            currentTemperature = currentTemperature,
            iconResId = icon,
            hours = state.hours,
            temperatureUnit = temperatureUnit,
            availableWidth = size.width - 16.dp,
            textColor = colors.onSurface,
            modifier = GlanceModifier.fillMaxSize()
        )
    }
}

@Composable
private fun TinyWidget(
    @DrawableRes iconResId: Int,
    currentTemperature: String,
    textColor: ColorProvider,
    modifier: GlanceModifier = GlanceModifier
) {
    Column(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            currentTemperature,
            style = TextStyle(
                color = textColor,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Normal
            ),
            maxLines = 1
        )
        Image(
            provider = ImageProvider(iconResId),
            contentDescription = null,
            modifier = GlanceModifier.size(30.dp)
        )
    }
}

@Composable
private fun SmallWidget(
    placeName: String,
    currentTemperature: String,
    @DrawableRes
    iconResId: Int,
    textColor: ColorProvider,
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
                color = textColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                fontStyle = FontStyle.Normal
            ),
            maxLines = 1,
            modifier = GlanceModifier.padding(bottom = 4.dp)
        )
        Row {
            Text(
                currentTemperature,
                style = TextStyle(
                    color = textColor,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Normal
                ),
                maxLines = 1,
                modifier = GlanceModifier.padding(end = 4.dp)
            )
            Image(
                provider = ImageProvider(iconResId),
                contentDescription = null,
                modifier = GlanceModifier.size(44.dp)
            )
        }
    }
}

@Composable
private fun NormalWidget(
    placeName: String,
    currentTemperature: String,
    @DrawableRes
    iconResId: Int,
    availableWidth: Dp,
    hours: List<WidgetHour>,
    temperatureUnit: TemperatureUnit,
    textColor: ColorProvider,
    modifier: GlanceModifier = GlanceModifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalAlignment = Alignment.CenterVertically
    ) {
        SmallWidget(
            placeName = placeName,
            currentTemperature = currentTemperature,
            iconResId = iconResId,
            textColor = textColor
        )
        Row(modifier = GlanceModifier.padding(top = 16.dp)) {
            val hourWidth = remember { 48.dp }
            val numHours = remember { floor(availableWidth / hourWidth).toInt() }
            hours.take(numHours).forEachIndexed { _, hour ->
                Hour(
                    temperature = getTemperature(
                        temperature = hour.temperature,
                        unit = temperatureUnit
                    ).asGlanceString(),
                    iconResId = hour.description.asGlanceWeatherIconResId(),
                    time = TextResource.fromShortTime(hour.epochMillis).asGlanceString(),
                    textColor = textColor,
                    modifier = GlanceModifier.width(hourWidth)
                )
            }
        }
    }
}

@Composable
private fun Hour(
    temperature: String,
    @DrawableRes iconResId: Int,
    time: String,
    textColor: ColorProvider,
    modifier: GlanceModifier = GlanceModifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = temperature,
            maxLines = 1,
            style = TextStyle(
                color = textColor,
                fontSize = 14.sp
            ),
            modifier = GlanceModifier.padding(bottom = 4.dp)
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
                color = textColor,
                fontSize = 14.sp
            ),
            modifier = GlanceModifier.padding(top = 4.dp)
        )
    }
}