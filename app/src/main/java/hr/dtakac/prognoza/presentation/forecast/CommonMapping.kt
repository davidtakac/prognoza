package hr.dtakac.prognoza.presentation.forecast

import android.text.format.DateUtils
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.entities.forecast.units.*
import hr.dtakac.prognoza.entities.forecast.Wind
import hr.dtakac.prognoza.presentation.TextResource
import java.math.BigDecimal
import java.math.RoundingMode
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
        BigDecimal(
            when (unit) {
                TemperatureUnit.C -> temperature.celsius
                TemperatureUnit.F -> temperature.fahrenheit
            }
        ).setScale(0, RoundingMode.HALF_EVEN)
    )
)

fun getWind(
    wind: Wind,
    unit: SpeedUnit
): TextResource {
    val roundingMode = RoundingMode.HALF_EVEN
    return when (unit) {
        SpeedUnit.KPH -> TextResource.fromStringId(
            R.string.template_wind_kmh,
            TextResource.fromNumber(
                BigDecimal(wind.speed.kilometersPerHour).setScale(
                    0,
                    roundingMode
                )
            )
        )
        SpeedUnit.MPH -> TextResource.fromStringId(
            R.string.template_wind_mph,
            TextResource.fromNumber(
                BigDecimal(wind.speed.milesPerHour).setScale(
                    0, roundingMode
                )
            )
        )
        SpeedUnit.MPS -> TextResource.fromStringId(
            R.string.template_wind_mps,
            TextResource.fromNumber(
                BigDecimal(wind.speed.metersPerSecond).setScale(
                    0,
                    roundingMode
                )
            )
        )
        SpeedUnit.KNOTS -> TextResource.fromStringId(
            R.string.template_wind_knots,
            TextResource.fromNumber(
                BigDecimal(wind.speed.knots).setScale(
                    1,
                    roundingMode)
            )
        )
    }
}

fun getPrecipitation(
    precipitation: Length,
    unit: LengthUnit
): TextResource {
    val roundingMode = RoundingMode.HALF_EVEN
    val zero = BigDecimal(0.0)
    return when (unit) {
        LengthUnit.MM -> BigDecimal(precipitation.millimeters)
            .setScale(1, roundingMode)
            .takeUnless { it.compareTo(zero) == 0 }
            ?.let {
                TextResource.fromStringId(
                    id = R.string.template_precipitation_mm,
                    TextResource.fromNumber(it)
                )
            }
            ?: TextResource.empty()

        LengthUnit.IN -> BigDecimal(precipitation.inches)
            .setScale(1, roundingMode)
            .takeUnless { it.compareTo(zero) == 0 }
            ?.let {
                TextResource.fromStringId(
                    id = R.string.template_precipitation_in,
                    TextResource.fromNumber(it)
                )
            }
            ?: TextResource.empty()

        LengthUnit.CM -> BigDecimal(precipitation.centimeters)
            .setScale(1, roundingMode)
            .takeUnless { it.compareTo(zero) == 0 }
            ?.let {
                TextResource.fromStringId(
                    id = R.string.template_precipitation_cm,
                    TextResource.fromNumber(it)
                )
            }
            ?: TextResource.empty()
    }
}