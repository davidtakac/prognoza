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
                .background(colors.surface)
                .appWidgetBackgroundRadius()
                .appWidgetBackground()
                .padding(16.dp)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            val isEmpty = prefs[ForecastWidgetReceiver.isEmpty] ?: true

            if (isEmpty) {
                Empty(colors)
            } else {
                val placeName = prefs[ForecastWidgetReceiver.placeName]!!
                val temperatureUnit =
                    TemperatureUnit.values()[prefs[ForecastWidgetReceiver.temperatureUnitOrdinal]!!]
                val precipitationUnit =
                    LengthUnit.values()[prefs[ForecastWidgetReceiver.precipitationUnitOrdinal]!!]
                val windUnit = SpeedUnit.values()[prefs[ForecastWidgetReceiver.windUnitOrdinal]!!]
                val description =
                    ForecastDescription.values()[prefs[ForecastWidgetReceiver.descriptionOrdinal]!!]
                val icon = description.toDrawableId()

                val currentTemperature = getTemperature(
                    temperature = Temperature(
                        value = prefs[ForecastWidgetReceiver.currentTemperatureCelsius]!!.toDouble(),
                        unit = TemperatureUnit.C
                    ),
                    unit = temperatureUnit
                ).asGlanceString()

                val lowHighTemperature = getLowHighTemperature(
                    lowTemperature = Temperature(
                        value = prefs[ForecastWidgetReceiver.lowTemperatureCelsius]!!.toDouble(),
                        unit = TemperatureUnit.C
                    ),
                    highTemperature = Temperature(
                        value = prefs[ForecastWidgetReceiver.highTemperatureCelsius]!!.toDouble(),
                        unit = TemperatureUnit.C
                    ),
                    temperatureUnit = temperatureUnit
                ).asGlanceString()

                val precipitation = getPrecipitation(
                    precipitation = Length(
                        value = prefs[ForecastWidgetReceiver.precipitationMillimeters]!!.toDouble(),
                        unit = LengthUnit.MM
                    ),
                    unit = precipitationUnit
                ).asGlanceString()

                val wind = getWind(
                    wind = Wind(
                        speed = Speed(
                            value = prefs[ForecastWidgetReceiver.windMetersPerSecond]!!.toDouble(),
                            unit = SpeedUnit.MPS
                        ),
                        fromDirection = Angle(
                            value = prefs[ForecastWidgetReceiver.windFromDirectionDegrees]!!.toDouble(),
                            unit = AngleUnit.DEG
                        )
                    ),
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
                        description = TextResource.fromStringId(description.toStringId())
                            .asGlanceString(),
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
        super.onUpdate(context, appWidgetManager, appWidgetIds)
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
                    ) { prefs ->
                        prefs.toMutablePreferences().apply {
                            this[isEmpty] = false
                            this[currentTemperatureCelsius] =
                                result.forecast.current.temperature.celsius.toFloat()
                            this[placeName] = result.placeName
                            this[lowTemperatureCelsius] =
                                result.forecast.today.lowTemperature.celsius.toFloat()
                            this[highTemperatureCelsius] =
                                result.forecast.today.highTemperature.celsius.toFloat()
                            this[descriptionOrdinal] = result.forecast.current.description.ordinal
                            this[temperatureUnitOrdinal] = result.temperatureUnit.ordinal
                            this[precipitationUnitOrdinal] = result.precipitationUnit.ordinal
                            this[precipitationMillimeters] =
                                result.forecast.current.precipitation.millimeters.toFloat()
                            this[windMetersPerSecond] =
                                result.forecast.current.wind.speed.metersPerSecond.toFloat()
                            this[windFromDirectionDegrees] =
                                result.forecast.current.wind.fromDirection.degrees.toFloat()
                            this[windUnitOrdinal] = result.windUnit.ordinal
                        }
                    }
                } else {
                    updateAppWidgetState(
                        context,
                        PreferencesGlanceStateDefinition,
                        glanceId
                    ) { prefs ->
                        prefs.toMutablePreferences().apply {
                            this[isEmpty] = true
                        }
                    }
                }
                glanceAppWidget.update(context, glanceId)
            }
        }
    }

    companion object {
        val isEmpty = booleanPreferencesKey("widget_is_empty")
        val currentTemperatureCelsius = floatPreferencesKey("widget_curr_temp_celsius")
        val placeName = stringPreferencesKey("widget_place_name")
        val lowTemperatureCelsius = floatPreferencesKey("widget_low_temp_celsius")
        val highTemperatureCelsius = floatPreferencesKey("widget_high_temp_celsius")
        val precipitationMillimeters = floatPreferencesKey("widget_precipitation_millimeters")
        val precipitationUnitOrdinal = intPreferencesKey("widget_precipitation_unit_ordinal")
        val windMetersPerSecond = floatPreferencesKey("widget_wind_meters_per_second")
        val descriptionOrdinal = intPreferencesKey("widget_description_ordinal")
        val temperatureUnitOrdinal = intPreferencesKey("widget_temperature_unit_ordinal")
        val windUnitOrdinal = intPreferencesKey("widget_wind_unit_ordinal")
        val windFromDirectionDegrees = floatPreferencesKey("widget_wind_from_direction_degrees")
    }
}