package hr.dtakac.prognoza.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.*
import androidx.glance.*
import androidx.glance.appwidget.*
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.color.ColorProviders
import androidx.glance.color.dynamicThemeColorProviders
import androidx.glance.layout.*
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.domain.usecase.GetForecast
import hr.dtakac.prognoza.domain.usecase.GetForecastResult
import hr.dtakac.prognoza.entities.forecast.Forecast
import hr.dtakac.prognoza.entities.forecast.ForecastDescription
import hr.dtakac.prognoza.entities.forecast.units.*
import hr.dtakac.prognoza.entities.forecast.wind.Wind
import hr.dtakac.prognoza.presentation.asGlanceString
import hr.dtakac.prognoza.presentation.forecast.*
import kotlinx.coroutines.*
import java.lang.IllegalStateException
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.inject.Inject

class ForecastWidget : GlanceAppWidget() {

    companion object {
        // https://developer.android.com/develop/ui/views/appwidgets/layouts#anatomy_determining_size
        private val tiny = DpSize(width = 57.dp, height = 102.dp) // 1x1
        private val small = DpSize(width = 130.dp, height = 102.dp) // 2x1
        private val normal = DpSize(width = 130.dp, height = 220.dp) // 2x2
        private val normalWide = DpSize(width = 203.dp, height = 220.dp) // 3x2
        private val normalExtraWide = DpSize(width = 276.dp, height = 220.dp) // 4x2
    }

    override val sizeMode: SizeMode = SizeMode.Responsive(
        setOf(tiny, small, normal, normalWide, normalExtraWide)
    )

    override val stateDefinition: GlanceStateDefinition<*> = PreferencesGlanceStateDefinition

    @Composable
    override fun Content() {
        val colors = dynamicThemeColorProviders()
        val prefs = currentState<Preferences>()
        val size = LocalSize.current

        Box(
            modifier = GlanceModifier
                .appWidgetBackgroundRadius()
                .background(colors.surface)
                .appWidgetBackground()
                .padding(16.dp)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            val state = ForecastWidgetReceiver.getWidgetState(prefs)
            if (state !is ForecastWidgetState.Success) {
                Empty(colors)
            } else {
                val placeName = state.placeName
                val temperatureUnit = state.temperatureUnit
                val icon = state.description.toDrawableId()
                val currentTemperature = getTemperature(
                    temperature = state.temperature,
                    unit = temperatureUnit
                ).asGlanceString()

                when (size) {
                    tiny -> PlaceAndTemp(
                        placeName = placeName,
                        currentTemperature = currentTemperature,
                        colors = colors
                    )
                    small -> PlaceAndTempAndIcon(
                        placeName = placeName,
                        currentTemperature = currentTemperature,
                        iconResId = icon,
                        colors = colors,
                        modifier = GlanceModifier.fillMaxSize()
                    )
                    else -> PlaceAndTempAndIconAndHours(
                        placeName = placeName,
                        currentTemperature = currentTemperature,
                        iconResId = icon,
                        hours = state.hours.take(
                            when (size) {
                                normal -> 3
                                normalWide -> 5
                                normalExtraWide -> 6
                                else -> throw IllegalStateException("Unsupported widget size.")
                            }
                        ),
                        temperatureUnit = temperatureUnit,
                        colors = colors
                    )
                }
            }
        }
    }

    @Composable
    private fun Empty(colors: ColorProviders) {
        Text(
            // Glance does not support stringResource
            text = LocalContext.current.getString(R.string.widget_empty),
            style = TextStyle(color = colors.onSurface, fontSize = 14.sp)
        )
    }

    @Composable
    private fun PlaceAndTemp(
        placeName: String,
        currentTemperature: String,
        colors: ColorProviders,
        modifier: GlanceModifier = GlanceModifier
    ) {
        Column(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
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
            Text(
                currentTemperature,
                style = TextStyle(
                    color = colors.onSurface,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Normal
                ),
                maxLines = 1
            )
        }
    }

    @Composable
    private fun PlaceAndTempAndIcon(
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
            PlaceAndTemp(
                placeName = placeName,
                currentTemperature = currentTemperature,
                colors = colors,
                modifier = GlanceModifier.defaultWeight()
            )
            Image(
                provider = ImageProvider(iconResId),
                contentDescription = null,
                modifier = GlanceModifier.size(58.dp)
            )
        }
    }

    @Composable
    private fun PlaceAndTempAndIconAndHours(
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
            PlaceAndTempAndIcon(
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
            data.forEach { hour ->
                val temperature = getTemperature(
                    temperature = hour.temperature,
                    unit = temperatureUnit
                ).asGlanceString()
                val iconResId = hour.description.toDrawableId()
                val time = getShortTime(time = hour.dateTime).asGlanceString()

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = GlanceModifier.defaultWeight()
                ) {
                    Text(
                        text = temperature,
                        maxLines = 1,
                        style = TextStyle(
                            color = colors.onSurfaceVariant,
                            fontSize = 14.sp
                        ),
                        modifier = GlanceModifier.padding(bottom = 2.dp)
                    )
                    Image(
                        provider = ImageProvider(iconResId),
                        contentDescription = null,
                        modifier = GlanceModifier.size(24.dp)
                    )
                    Text(
                        text = time,
                        maxLines = 1,
                        style = TextStyle(
                            color = colors.onSurfaceVariant,
                            fontSize = 14.sp
                        ),
                        modifier = GlanceModifier.padding(top = 2.dp)
                    )
                }
            }
        }
    }
}

@AndroidEntryPoint
class ForecastWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = ForecastWidget()

    @Inject
    lateinit var getForecast: GetForecast

    private val scope = MainScope()

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        refreshData(context)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == ForecastWidgetRefreshCallback.REFRESH_ACTION) {
            refreshData(context)
        }
    }

    // todo: schedule this to run every hour with 15min jitter
    private fun refreshData(context: Context) {
        scope.launch {
            val result = getForecast()
            val glanceIds = GlanceAppWidgetManager(context).getGlanceIds(ForecastWidget::class.java)

            glanceIds.forEach { glanceId ->
                if (result is GetForecastResult.Success) {
                    updateAppWidgetState(
                        context,
                        PreferencesGlanceStateDefinition,
                        glanceId
                    ) {
                        setWidgetStateSuccess(
                            prefs = it,
                            forecast = result.forecast,
                            placeName = result.placeName,
                            temperatureUnit = result.temperatureUnit,
                            windUnit = result.windUnit,
                            precipitationUnit = result.precipitationUnit
                        )
                    }
                } else {
                    updateAppWidgetState(
                        context,
                        PreferencesGlanceStateDefinition,
                        glanceId
                    ) { setWidgetStateEmpty(prefs = it) }
                }
                glanceAppWidget.update(context, glanceId)
            }
        }
    }

    companion object {
        fun getWidgetState(prefs: Preferences): ForecastWidgetState {
            val isEmpty = prefs[isEmptyKey] ?: true
            return if (isEmpty) ForecastWidgetState.Empty else ForecastWidgetState.Success(
                placeName = prefs[placeNameKey]!!,
                temperatureUnit = TemperatureUnit.values()[prefs[temperatureUnitOrdinalKey]!!],
                temperature = Temperature(
                    value = prefs[currTempCelsiusKey]!!.toDouble(),
                    unit = TemperatureUnit.C
                ),
                lowTemperature = Temperature(
                    value = prefs[lowTempCelsiusKey]!!.toDouble(),
                    unit = TemperatureUnit.C
                ),
                highTemperature = Temperature(
                    value = prefs[highTempCelsiusKey]!!.toDouble(),
                    unit = TemperatureUnit.C
                ),
                precipitationUnit = LengthUnit.values()[prefs[precipUnitKey]!!],
                precipitation = Length(
                    value = prefs[precipMillimetersKey]!!.toDouble(),
                    unit = LengthUnit.MM
                ),
                windUnit = SpeedUnit.values()[prefs[windUnitOrdinalKey]!!],
                wind = Wind(
                    speed = Speed(
                        value = prefs[windMetersPerSecondKey]!!.toDouble(),
                        unit = SpeedUnit.MPS
                    ),
                    fromDirection = Angle(
                        value = prefs[windDegreesKey]!!.toDouble(),
                        unit = AngleUnit.DEG
                    )
                ),
                description = ForecastDescription.values()[prefs[descriptionOrdinalKey]!!],
                // todo: inject a gson instance or just use the kotlin library?
                hours = Gson().fromJson<List<WidgetHourSerialized>>(
                    prefs[hours]!!,
                    object : TypeToken<List<WidgetHourSerialized>>() {}.type
                ).map {
                    WidgetHour(
                        dateTime = Instant.ofEpochMilli(it.dateTimeEpochMillis)
                            .atZone(ZoneId.systemDefault()),
                        temperature = Temperature(it.temperatureCelsius, TemperatureUnit.C),
                        description = ForecastDescription.values()[it.descriptionOrdinal]
                    )
                }
            )
        }

        fun setWidgetStateSuccess(
            prefs: Preferences,
            forecast: Forecast,
            placeName: String,
            temperatureUnit: TemperatureUnit,
            windUnit: SpeedUnit,
            precipitationUnit: LengthUnit
        ): Preferences = prefs.toMutablePreferences().apply {
            this[isEmptyKey] = false
            this[currTempCelsiusKey] = forecast.current.temperature.celsius.toFloat()
            this[placeNameKey] = placeName
            this[lowTempCelsiusKey] = forecast.today.lowTemperature.celsius.toFloat()
            this[highTempCelsiusKey] = forecast.today.highTemperature.celsius.toFloat()
            this[descriptionOrdinalKey] = forecast.current.description.ordinal
            this[temperatureUnitOrdinalKey] = temperatureUnit.ordinal
            this[precipUnitKey] = precipitationUnit.ordinal
            this[precipMillimetersKey] = forecast.current.precipitation.millimeters.toFloat()
            this[windMetersPerSecondKey] = forecast.current.wind.speed.metersPerSecond.toFloat()
            this[windDegreesKey] =
                forecast.current.wind.fromDirection.degrees.toFloat()
            this[windUnitOrdinalKey] = windUnit.ordinal

            val widgetHours = forecast.today.hourly.map {
                WidgetHourSerialized(
                    dateTimeEpochMillis = it.dateTime.toInstant().toEpochMilli(),
                    temperatureCelsius = it.temperature.celsius,
                    descriptionOrdinal = it.description.ordinal
                )
            }
            this[hours] = Gson().toJson(widgetHours)
        }

        fun setWidgetStateEmpty(prefs: Preferences): Preferences =
            prefs.toMutablePreferences().apply {
                this[isEmptyKey] = true
            }

        private val isEmptyKey = booleanPreferencesKey("widget_is_empty")
        private val currTempCelsiusKey = floatPreferencesKey("widget_curr_temp_celsius")
        private val placeNameKey = stringPreferencesKey("widget_place_name")
        private val lowTempCelsiusKey = floatPreferencesKey("widget_low_temp_celsius")
        private val highTempCelsiusKey = floatPreferencesKey("widget_high_temp_celsius")
        private val precipMillimetersKey = floatPreferencesKey("widget_precipitation_millimeters")
        private val precipUnitKey = intPreferencesKey("widget_precipitation_unit_ordinal")
        private val windMetersPerSecondKey = floatPreferencesKey("widget_wind_meters_per_second")
        private val descriptionOrdinalKey = intPreferencesKey("widget_description_ordinal")
        private val temperatureUnitOrdinalKey = intPreferencesKey("widget_temperature_unit_ordinal")
        private val windUnitOrdinalKey = intPreferencesKey("widget_wind_unit_ordinal")
        private val windDegreesKey = floatPreferencesKey("widget_wind_from_direction_degrees")
        private val hours = stringPreferencesKey("widget_hours")

        private data class WidgetHourSerialized(
            val dateTimeEpochMillis: Long,
            val temperatureCelsius: Double,
            val descriptionOrdinal: Int
        )
    }
}

sealed interface ForecastWidgetState {
    object Empty : ForecastWidgetState

    data class Success(
        val placeName: String,
        val temperatureUnit: TemperatureUnit,
        val temperature: Temperature,
        val lowTemperature: Temperature,
        val highTemperature: Temperature,
        val precipitationUnit: LengthUnit,
        val precipitation: Length,
        val windUnit: SpeedUnit,
        val wind: Wind,
        val description: ForecastDescription,
        val hours: List<WidgetHour>
    ) : ForecastWidgetState
}

data class WidgetHour(
    val dateTime: ZonedDateTime,
    val temperature: Temperature,
    val description: ForecastDescription
)