package hr.dtakac.prognoza.widget

import androidx.datastore.preferences.core.*
import hr.dtakac.prognoza.domain.usecase.GetForecastResult
import hr.dtakac.prognoza.entities.forecast.ForecastDescription
import hr.dtakac.prognoza.entities.forecast.units.*
import hr.dtakac.prognoza.entities.forecast.wind.Wind
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

class ForecastWidgetStateRepository(
    private val prefs: MutablePreferences,
) {
    fun setState(forecastResultSuccess: GetForecastResult.Success?) {
        if (forecastResultSuccess != null) {
            val forecast = forecastResultSuccess.forecast
            val placeName = forecastResultSuccess.placeName
            val temperatureUnit = forecastResultSuccess.temperatureUnit
            val precipitationUnit = forecastResultSuccess.precipitationUnit
            val windUnit = forecastResultSuccess.windUnit

            prefs[isEmptyKey] = false
            prefs[currTempCelsiusKey] = forecast.current.temperature.celsius.toFloat()
            prefs[placeNameKey] = placeName
            prefs[lowTempCelsiusKey] = forecast.today.lowTemperature.celsius.toFloat()
            prefs[highTempCelsiusKey] = forecast.today.highTemperature.celsius.toFloat()
            prefs[descriptionOrdinalKey] = forecast.current.description.ordinal
            prefs[temperatureUnitOrdinalKey] = temperatureUnit.ordinal
            prefs[precipUnitKey] = precipitationUnit.ordinal
            prefs[precipMillimetersKey] =
                forecast.current.precipitation.millimeters.toFloat()
            prefs[windMetersPerSecondKey] =
                forecast.current.wind.speed.metersPerSecond.toFloat()
            prefs[windDegreesKey] =
                forecast.current.wind.fromDirection.degrees.toFloat()
            prefs[windUnitOrdinalKey] = windUnit.ordinal

            val widgetHours = forecast.today.hourly.map {
                WidgetHourSerialized(
                    dateTimeEpochMillis = it.dateTime.toInstant().toEpochMilli(),
                    temperatureCelsius = it.temperature.celsius,
                    descriptionOrdinal = it.description.ordinal
                )
            }
            //prefs[hours] = gson.toJson(widgetHours) todo fix
        } else {
            prefs[isEmptyKey] = true
        }
    }

    fun getState(): ForecastWidgetState {
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
            hours = listOf()/* todo fix gson.fromJson<List<WidgetHourSerialized>>(
                prefs[hours]!!,
                object : TypeToken<List<WidgetHourSerialized>>() {}.type
            ).map {
                WidgetHour(
                    dateTime = Instant.ofEpochMilli(it.dateTimeEpochMillis)
                        .atZone(ZoneId.systemDefault()),
                    temperature = Temperature(it.temperatureCelsius, TemperatureUnit.C),
                    description = ForecastDescription.values()[it.descriptionOrdinal]
                )
            }*/
        )
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