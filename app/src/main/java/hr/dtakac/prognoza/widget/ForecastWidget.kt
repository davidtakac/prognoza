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
import dagger.hilt.android.AndroidEntryPoint
import hr.dtakac.prognoza.domain.usecase.GetForecast
import hr.dtakac.prognoza.domain.usecase.GetForecastResult
import hr.dtakac.prognoza.entities.forecast.Forecast
import hr.dtakac.prognoza.entities.forecast.ForecastDescription
import hr.dtakac.prognoza.entities.forecast.units.*
import hr.dtakac.prognoza.entities.forecast.wind.Wind
import hr.dtakac.prognoza.presentation.TextResource
import hr.dtakac.prognoza.presentation.asGlanceString
import hr.dtakac.prognoza.presentation.forecast.*
import kotlinx.coroutines.*
import javax.inject.Inject

class ForecastWidget : GlanceAppWidget() {

    companion object {
        // https://developer.android.com/develop/ui/views/appwidgets/layouts#anatomy_determining_size
        private val small = DpSize(width = 130.dp, height = 102.dp) // 2x1
        private val normal = DpSize(width = 130.dp, height = 220.dp) // 2x2
    }

    override val sizeMode: SizeMode = SizeMode.Responsive(
        setOf(small, normal)
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

            if (state is ForecastWidgetState.Empty) {
                Empty(colors)
            } else if (state is ForecastWidgetState.Success) {
                val placeName = state.placeName
                val temperatureUnit = state.temperatureUnit
                val precipitationUnit = state.precipitationUnit
                val windUnit = state.windUnit
                val icon = state.description.toDrawableId()

                val description = TextResource.fromStringId(
                    state.description.toStringId()
                ).asGlanceString()

                val currentTemperature = getTemperature(
                    temperature = state.temperature,
                    unit = temperatureUnit
                ).asGlanceString()

                val lowHighTemperature = getLowHighTemperature(
                    lowTemperature = state.lowTemperature,
                    highTemperature = state.highTemperature,
                    temperatureUnit = temperatureUnit
                ).asGlanceString()

                val precipitation = getPrecipitation(
                    precipitation = state.precipitation,
                    unit = precipitationUnit
                ).asGlanceString()

                val wind = getWind(
                    wind = state.wind,
                    unit = windUnit
                ).asGlanceString()

                when (size) {
                    small -> PlaceTempAndIcon(
                        placeName = placeName,
                        currentTemperature = currentTemperature,
                        iconResId = icon,
                        colors = colors,
                        modifier = GlanceModifier.fillMaxSize()
                    )
                    normal -> Normal(
                        placeName = placeName,
                        currentTemperature = currentTemperature,
                        lowHighTemperature = lowHighTemperature,
                        precipitation = precipitation,
                        wind = wind,
                        description = description,
                        iconResId = icon,
                        colors = colors
                    )
                }

            }
        }
    }

    @Composable
    private fun Empty(colors: ColorProviders) {
        // todo: make actual empty screen
        Text("empty")
    }

    @Composable
    private fun PlaceTempAndIcon(
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
            Column(
                modifier = GlanceModifier.defaultWeight(),
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
            Image(
                provider = ImageProvider(iconResId),
                contentDescription = null,
                modifier = GlanceModifier.size(58.dp)
            )
        }
    }

    @Composable
    private fun Normal(
        placeName: String,
        currentTemperature: String,
        description: String,
        lowHighTemperature: String,
        wind: String,
        precipitation: String,
        @DrawableRes
        iconResId: Int,
        colors: ColorProviders,
        modifier: GlanceModifier = GlanceModifier
    ) {
        Column(modifier = modifier) {
            PlaceTempAndIcon(
                placeName = placeName,
                currentTemperature = currentTemperature,
                iconResId = iconResId,
                colors = colors,
                modifier = GlanceModifier.fillMaxWidth()
            )
            Spacer(modifier = GlanceModifier.height(16.dp))
            DescriptionLowHighTempWindAndPrecipitation(
                description = description,
                lowHighTemperature = lowHighTemperature,
                wind = wind,
                precipitation = precipitation,
                colors = colors,
                modifier = GlanceModifier
                    .appWidgetInnerRadius()
                    .background(colors.surfaceVariant)
                    .padding(12.dp)
            )
        }
    }

    @Composable
    private fun DescriptionLowHighTempWindAndPrecipitation(
        description: String,
        lowHighTemperature: String,
        wind: String,
        precipitation: String,
        colors: ColorProviders,
        modifier: GlanceModifier = GlanceModifier
    ) {
        Column(modifier = modifier) {
            Row(modifier = GlanceModifier.fillMaxWidth()) {
                Text(
                    description,
                    style = TextStyle(
                        color = colors.onSurfaceVariant,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        fontStyle = FontStyle.Normal
                    ),
                    maxLines = 1,
                    modifier = GlanceModifier.defaultWeight()
                )
                Spacer(modifier = GlanceModifier.width(8.dp))
                Text(
                    lowHighTemperature,
                    style = TextStyle(
                        color = colors.onSurfaceVariant,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        fontStyle = FontStyle.Normal
                    ),
                    maxLines = 1
                )
            }
            Spacer(modifier = GlanceModifier.height(8.dp))
            Row(modifier = GlanceModifier.fillMaxWidth()) {
                Text(
                    wind,
                    style = TextStyle(
                        color = colors.onSurfaceVariant,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        fontStyle = FontStyle.Normal
                    ),
                    maxLines = 1,
                    modifier = GlanceModifier.defaultWeight()
                )
                Spacer(modifier = GlanceModifier.width(8.dp))
                Text(
                    precipitation,
                    style = TextStyle(
                        color = colors.onSurfaceVariant,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        fontStyle = FontStyle.Normal
                    ),
                    maxLines = 1
                )
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
                        value = prefs[windFromDirectionDegreesKey]!!.toDouble(),
                        unit = AngleUnit.DEG
                    )
                ),
                description = ForecastDescription.values()[prefs[descriptionOrdinalKey]!!]
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
            this[windFromDirectionDegreesKey] = forecast.current.wind.fromDirection.degrees.toFloat()
            this[windUnitOrdinalKey] = windUnit.ordinal
        }

        fun setWidgetStateEmpty(prefs: Preferences): Preferences = prefs.toMutablePreferences().apply {
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
        private val windFromDirectionDegreesKey = floatPreferencesKey("widget_wind_from_direction_degrees")
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
        val description: ForecastDescription
    ) : ForecastWidgetState
}