package hr.dtakac.prognoza.widget

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.*
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.*
import androidx.glance.color.dynamicThemeColorProviders
import androidx.glance.layout.*
import androidx.glance.text.*
import androidx.glance.unit.ColorProvider
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.presentation.TextResource
import hr.dtakac.prognoza.presentation.asGlanceString
import hr.dtakac.prognoza.presentation.forecast.getTemperature
import hr.dtakac.prognoza.shared.entity.TemperatureUnit
import hr.dtakac.prognoza.ui.MainActivity
import hr.dtakac.prognoza.ui.theme.asGlanceWeatherIconResId

class ForecastWidget : GlanceAppWidget() {
    companion object {
        private val tiny = DpSize(width = 40.dp, height = 40.dp)
        private val small = DpSize(width = 100.dp, height = 60.dp)

        private val hourWidth = 48.dp
        private val normalSizeToNumHours = mutableMapOf<DpSize, Int>().apply {
            val height = 160.dp
            val minNumHours = 2
            val maxNumHours = 10
            for (i in minNumHours..maxNumHours) {
                set(DpSize(width = (hourWidth * i), height = height), i)
            }
        }
    }

    override val sizeMode: SizeMode = SizeMode.Responsive(
        mutableSetOf<DpSize>().apply {
            add(tiny)
            add(small)
            addAll(normalSizeToNumHours.keys)
        }
    )

    override val stateDefinition = ForecastWidgetStateDefinition

    @Composable
    override fun Content() {
        val colors = dynamicThemeColorProviders()
        val state = currentState<ForecastWidgetState>()
        val onClick = actionStartActivity(MainActivity::class.java)
        val size = LocalSize.current
        val textColor = colors.onSurface
        val backgroundColor = colors.surface
        Box(
            modifier = GlanceModifier
                .appWidgetBackgroundRadius()
                .background(backgroundColor)
                .appWidgetBackground()
                .fillMaxSize()
        ) {
            when (state) {
                ForecastWidgetState.Error,
                ForecastWidgetState.Unavailable -> ForecastEmpty(
                    textColor = textColor,
                    modifier = GlanceModifier.fillMaxSize()
                )
                ForecastWidgetState.Loading -> ForecastLoading(
                    modifier = GlanceModifier.fillMaxSize()
                )
                is ForecastWidgetState.Success -> {
                    val placeName = state.placeName
                    val temperatureUnit = state.temperatureUnit
                    val icon = state.description.asGlanceWeatherIconResId()
                    val currentTemperature = getTemperature(
                        temperature = state.temperature,
                        unit = temperatureUnit
                    ).asGlanceString()

                    when (size) {
                        tiny -> ForecastTiny(
                            iconResId = icon,
                            currentTemperature = currentTemperature,
                            textColor = textColor,
                            modifier = GlanceModifier.fillMaxSize()
                        )
                        small -> ForecastSmall(
                            placeName = placeName,
                            currentTemperature = currentTemperature,
                            iconResId = icon,
                            textColor = textColor,
                            modifier = GlanceModifier.fillMaxSize()
                        )
                        else -> ForecastNormal(
                            placeName = placeName,
                            currentTemperature = currentTemperature,
                            iconResId = icon,
                            hours = state.hours.take(normalSizeToNumHours[size]!!),
                            temperatureUnit = temperatureUnit,
                            textColor = textColor,
                            modifier = GlanceModifier.fillMaxSize()
                        )
                    }
                }
            }
            // This box is a workaround to make the entire widget clickable
            Box(modifier = GlanceModifier.fillMaxSize().clickable(onClick)) {}
        }
    }

    @Composable
    private fun ForecastEmpty(
        textColor: ColorProvider,
        modifier: GlanceModifier = GlanceModifier
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
        ) {
            Text(
                // Glance does not support stringResource
                text = LocalContext.current.getString(R.string.widget_empty),
                style = TextStyle(color = textColor, fontSize = 14.sp),
                modifier = GlanceModifier.padding(8.dp)
            )
        }
    }

    @Composable
    private fun ForecastLoading(
        modifier: GlanceModifier = GlanceModifier
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
        ) {
            CircularProgressIndicator()
        }
    }

    @Composable
    private fun ForecastTiny(
        currentTemperature: String,
        @DrawableRes iconResId: Int,
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
                    fontSize = 18.sp,
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
    private fun ForecastSmall(
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
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    currentTemperature,
                    style = TextStyle(
                        color = textColor,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Normal
                    ),
                    maxLines = 1,
                    // Without this padding, the temperature and weather icon are too close
                    // when the weather icon is cloudy, for example
                    modifier = GlanceModifier.padding(end = 4.dp)
                )
                Image(
                    provider = ImageProvider(iconResId),
                    contentDescription = null,
                    modifier = GlanceModifier.size(36.dp)
                )
            }
        }
    }

    @Composable
    private fun ForecastNormal(
        placeName: String,
        currentTemperature: String,
        @DrawableRes
        iconResId: Int,
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
            ForecastSmall(
                placeName = placeName,
                currentTemperature = currentTemperature,
                iconResId = iconResId,
                textColor = textColor
            )
            Row(modifier = GlanceModifier.padding(top = 8.dp)) {
                hours.forEachIndexed { _, hour ->
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
}