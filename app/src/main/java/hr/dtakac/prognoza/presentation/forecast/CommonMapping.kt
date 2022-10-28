package hr.dtakac.prognoza.presentation.forecast

import android.text.format.DateUtils
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.entities.forecast.units.*
import hr.dtakac.prognoza.entities.forecast.wind.Wind
import hr.dtakac.prognoza.presentation.TextResource
import java.time.ZonedDateTime

fun getLowHighTemperature(
    lowTemperature: Temperature,
    highTemperature: Temperature,
    temperatureUnit: TemperatureUnit
): TextResource = TextResource.fromStringId(
    id = R.string.template_high_low_temperature,
    getTemperature(highTemperature, temperatureUnit),
    getTemperature(lowTemperature, temperatureUnit)
)

fun getShortTime(time: ZonedDateTime): TextResource = TextResource.fromEpochMillis(
    millis = time.toInstant().toEpochMilli(),
    flags = DateUtils.FORMAT_SHOW_TIME or DateUtils.FORMAT_ABBREV_TIME
)

fun getTemperature(
    temperature: Temperature,
    unit: TemperatureUnit
): TextResource = TextResource.fromStringId(
    id = R.string.template_temperature_degrees,
    TextResource.fromNumber(
        temperature.run {
            when (unit) {
                TemperatureUnit.C -> celsius
                TemperatureUnit.F -> fahrenheit
            }
        }
    )
)

fun getWind(
    wind: Wind,
    unit: SpeedUnit
): TextResource = when (unit) {
    SpeedUnit.KPH -> TextResource.fromStringId(
        R.string.template_wind_kmh,
        TextResource.fromNumber(wind.speed.kilometersPerHour, decimalPlaces = 0)
    )
    SpeedUnit.MPH -> TextResource.fromStringId(
        R.string.template_wind_mph,
        TextResource.fromNumber(wind.speed.milesPerHour, decimalPlaces = 0)
    )
    SpeedUnit.MPS -> TextResource.fromStringId(
        R.string.template_wind_mps,
        TextResource.fromNumber(wind.speed.metersPerSecond, decimalPlaces = 0)
    )
    SpeedUnit.KNOTS -> TextResource.fromStringId(
        R.string.template_wind_knots,
        TextResource.fromNumber(wind.speed.knots, decimalPlaces = 1)
    )
}

fun getPrecipitation(
    precipitation: Length,
    unit: LengthUnit
): TextResource = when (unit) {
    LengthUnit.MM -> TextResource.fromStringId(
        R.string.template_precipitation_mm,
        TextResource.fromNumber(precipitation.millimeters, decimalPlaces = 1)
    )
    LengthUnit.IN -> TextResource.fromStringId(
        R.string.template_precipitation_in,
        TextResource.fromNumber(precipitation.inches, decimalPlaces = 2)
    )
    LengthUnit.CM -> TextResource.fromStringId(
        R.string.template_precipitation_cm,
        TextResource.fromNumber(precipitation.centimeters, decimalPlaces = 2)
    )
}