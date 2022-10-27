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
): TextResource = TextResource.fromStringId(
    id = when (unit) {
        SpeedUnit.KPH -> R.string.template_wind_kmh
        SpeedUnit.MPH -> R.string.template_wind_mph
        SpeedUnit.MPS -> R.string.template_wind_mps
        SpeedUnit.KNOTS -> R.string.template_wind_knots
    },
    TextResource.fromNumber(
        wind.speed.run {
            when (unit) {
                SpeedUnit.KPH -> kilometersPerHour
                SpeedUnit.MPH -> milesPerHour
                SpeedUnit.MPS -> metersPerSecond
                SpeedUnit.KNOTS -> knots
            }
        },
        decimalPlaces = 2
    )
)

fun getPrecipitation(
    precipitation: Length,
    unit: LengthUnit
): TextResource = TextResource.fromStringId(
    id = when (unit) {
        LengthUnit.MM -> R.string.template_precipitation_mm
        LengthUnit.IN -> R.string.template_precipitation_in
        LengthUnit.CM -> R.string.template_precipitation_cm
    },
    TextResource.fromNumber(
        precipitation.run {
            when (unit) {
                LengthUnit.MM -> millimeters
                LengthUnit.IN -> inches
                LengthUnit.CM -> centimeters
            }
        }, decimalPlaces = 2
    )
)