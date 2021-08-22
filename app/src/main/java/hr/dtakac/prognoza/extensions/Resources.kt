package hr.dtakac.prognoza.extensions

import android.content.res.Resources
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import androidx.core.text.color
import androidx.core.text.toSpannable
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.uimodel.MeasurementUnit
import hr.dtakac.prognoza.uimodel.RepresentativeWeatherDescription
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.roundToInt

fun Resources.formatTemperatureValue(temperature: Float?, unit: MeasurementUnit): String {
    return when {
        temperature == null -> {
            getString(R.string.placeholder_temperature)
        }
        unit == MeasurementUnit.IMPERIAL -> {
            getString(
                R.string.template_temperature_universal,
                temperature.degreesCelsiusToDegreesFahrenheit().roundToInt()
            )
        }
        else -> {
            getString(
                R.string.template_temperature_universal,
                temperature.roundToInt()
            )
        }
    }
}

fun Resources.formatPrecipitationValue(precipitation: Float?, unit: MeasurementUnit): String {
    return if (precipitation == null) {
        getString(R.string.placeholder_precipitation_none)
    } else if (precipitation == 0f) {
        getString(R.string.placeholder_precipitation_none)
    } else if (unit == MeasurementUnit.IMPERIAL) {
        val convertedPrecipitation = precipitation.millimetresToInches()
        if (convertedPrecipitation.isPrecipitationAmountSignificant()) {
            getString(
                R.string.template_precipitation_imperial,
                convertedPrecipitation
            )
        } else {
            getString(R.string.placeholder_precipitation_insignificant_imperial)
        }
    } else {
        if (precipitation.isPrecipitationAmountSignificant()) {
            getString(
                R.string.template_precipitation_metric,
                precipitation
            )
        } else {
            getString(R.string.placeholder_precipitation_insignificant_metric)
        }
    }
}

fun Resources.formatWeatherIconDescription(descriptionResourceId: Int?): String {
    return if (descriptionResourceId == null) {
        getString(R.string.placeholder_description)
    } else {
        getString(descriptionResourceId)
    }
}

fun Resources.formatWindSpeedValue(windSpeed: Float?, unit: MeasurementUnit): String {
    return if (windSpeed != null) {
        if (unit == MeasurementUnit.IMPERIAL) {
            getString(
                R.string.template_wind_imperial,
                windSpeed.metersPerSecondToMilesPerHour()
            )
        } else {
            getString(
                R.string.template_wind_metric,
                windSpeed.metersPerSecondToKilometresPerHour()
            )
        }
    } else {
        getString(R.string.placeholder_wind_speed)
    }
}

fun Resources.formatWindWithDirection(
    windSpeed: Float?,
    windFromCompassDirection: Int?,
    windSpeedUnit: MeasurementUnit
): String {
    return getString(
        R.string.template_wind_with_direction,
        formatWindSpeedValue(windSpeed, windSpeedUnit),
        getString(windFromCompassDirection ?: R.string.placeholder_wind_direction)
    )
}

fun Resources.formatHumidityValue(relativeHumidity: Float?): String {
    return if (relativeHumidity != null) {
        getString(
            R.string.template_humidity_universal,
            relativeHumidity.roundToInt()
        )
    } else {
        getString(R.string.placeholder_humidity)
    }
}

fun Resources.formatPressureValue(pressure: Float?, unit: MeasurementUnit): String {
    return if (pressure != null) {
        if (unit == MeasurementUnit.IMPERIAL) {
            getString(
                R.string.template_pressure_imperial,
                pressure.hectoPascalToPsi()
            )
        } else {
            getString(
                R.string.template_pressure_metric,
                pressure.roundToInt()
            )
        }
    } else {
        getString(R.string.placeholder_pressure)
    }
}

fun Resources.formatRepresentativeWeatherIconDescription(
    representativeWeatherDescription: RepresentativeWeatherDescription?
): String {
    val weatherIconDescriptionString =
        formatWeatherIconDescription(representativeWeatherDescription?.weatherDescription?.descriptionResourceId)
    return if (representativeWeatherDescription?.isMostly == true) {
        getString(
            R.string.template_mostly,
            weatherIconDescriptionString.lowercase()
        )
    } else {
        weatherIconDescriptionString
    }
}

fun Resources.formatTotalPrecipitation(
    precipitation: Float?,
    unit: MeasurementUnit
): Spannable {
    return when (precipitation) {
        null -> {
            SpannableString(getString(R.string.placeholder_total_precipitation_none))
        }
        0f -> {
            SpannableString(getString(R.string.placeholder_total_precipitation_none))
        }
        else -> {
            SpannableStringBuilder()
                .color(getColor(R.color.precipitation, null)) {
                    append(formatPrecipitationValue(precipitation, unit))
                }
                .append(getString(R.string.amount_of_precipitation))
                .toSpannable()
        }
    }
}

fun Resources.formatEmptyMessage(reasonResourceId: Int?): String {
    return getString(reasonResourceId ?: R.string.error_generic)
}

fun Resources.formatPrecipitationTwoHours(
    precipitationForecast: Float?,
    unit: MeasurementUnit
): Spannable {
    return when (precipitationForecast) {
        null -> {
            SpannableString(getString(R.string.precipitation_forecast_none))
        }
        0f -> {
            SpannableString(getString(R.string.precipitation_forecast_none))
        }
        else -> {
            SpannableStringBuilder()
                .color(getColor(R.color.precipitation, null)) {
                    append(formatPrecipitationValue(precipitationForecast, unit))
                }
                .append(getString(R.string.amount_of_precipitation_forecast))
        }
    }
}

fun Resources.formatFeelsLike(feelsLike: Float?, unit: MeasurementUnit): String {
    return getString(
        R.string.template_feels_like,
        if (feelsLike == null) {
            getString(R.string.placeholder_temperature)
        } else {
            formatTemperatureValue(feelsLike, unit)
        }
    )
}

fun Resources.formatDaySummaryTime(time: ZonedDateTime): String {
    val dateTimeFormatter = DateTimeFormatter.ofPattern("EEEE, d LLLL", Locale.getDefault())
    val nowAtStartOfDay = ZonedDateTime.now().atStartOfDay()
    return when (time.withZoneSameInstant(ZoneId.systemDefault()).atStartOfDay()) {
        nowAtStartOfDay -> {
            getString(R.string.today)
        }
        else -> {
            time.withZoneSameInstant(ZoneId.systemDefault())
                .format(dateTimeFormatter)
        }
    }
}