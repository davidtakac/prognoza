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
import java.math.RoundingMode
import java.text.DecimalFormat
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.roundToInt

fun Resources.formatTemperatureValue(temperature: Float?, unit: MeasurementUnit): String {
    val formatter = DecimalFormat.getInstance(Locale.getDefault()).apply {
        maximumFractionDigits = 0
        isParseIntegerOnly = true
        roundingMode = RoundingMode.HALF_UP
    }
    return when {
        temperature == null -> {
            getString(R.string.placeholder_temperature)
        }
        unit == MeasurementUnit.IMPERIAL -> {
            getString(
                R.string.template_temperature_universal,
                formatter.format(temperature.degreesCelsiusToDegreesFahrenheit())
            )
        }
        else -> {
            getString(
                R.string.template_temperature_universal,
                formatter.format(temperature)
            )
        }
    }
}

fun Resources.formatPrecipitationValue(precipitation: Float?, unit: MeasurementUnit): Spannable {
    val formatter = DecimalFormat.getInstance(Locale.getDefault()).apply {
        maximumFractionDigits = 2
        roundingMode = RoundingMode.HALF_UP
    }
    return if (precipitation == null) {
        SpannableString(getString(R.string.placeholder_precipitation_none))
    } else if (precipitation == 0f) {
        SpannableString(getString(R.string.placeholder_precipitation_none))
    } else if (unit == MeasurementUnit.IMPERIAL) {
        val convertedPrecipitation = precipitation.millimetresToInches()
        if (convertedPrecipitation.isPrecipitationAmountSignificant()) {
            SpannableStringBuilder()
                .color(getColor(R.color.precipitation, null)) {
                    append(
                        getString(
                            R.string.template_precipitation_imperial,
                            formatter.format(convertedPrecipitation)
                        )
                    )
                }
        } else {
            SpannableString(
                getString(
                    R.string.placeholder_precipitation_insignificant_imperial,
                    formatter.format(0.01f)
                )
            )
        }
    } else {
        if (precipitation.isPrecipitationAmountSignificant()) {
            SpannableStringBuilder()
                .color(getColor(R.color.precipitation, null)) {
                    append(
                        getString(
                            R.string.template_precipitation_metric,
                            formatter.format(precipitation)
                        )
                    )
                }
                .toSpannable()
        } else {
            SpannableString(
                getString(
                    R.string.placeholder_precipitation_insignificant_metric,
                    formatter.format(0.01f)
                )
            )
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
    val formatter = DecimalFormat.getInstance(Locale.getDefault()).apply {
        maximumFractionDigits = 1
        roundingMode = RoundingMode.HALF_UP
    }
    return if (windSpeed != null) {
        if (unit == MeasurementUnit.IMPERIAL) {
            getString(
                R.string.template_wind_imperial,
                formatter.format(windSpeed.metersPerSecondToMilesPerHour())
            )
        } else {
            getString(
                R.string.template_wind_metric,
                formatter.format(windSpeed.metersPerSecondToKilometresPerHour())
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
    val formatter = DecimalFormat.getInstance(Locale.getDefault()).apply {
        maximumFractionDigits = 0
        isParseIntegerOnly = true
        roundingMode = RoundingMode.HALF_UP
    }
    return if (relativeHumidity != null) {
        getString(
            R.string.template_humidity_universal,
            formatter.format(relativeHumidity.roundToInt())
        )
    } else {
        getString(R.string.placeholder_humidity)
    }
}

fun Resources.formatPressureValue(pressure: Float?, unit: MeasurementUnit): String {
    val formatter = DecimalFormat.getInstance(Locale.getDefault()).apply {
        when (unit) {
            MeasurementUnit.IMPERIAL -> {
                maximumFractionDigits = 2
            }
            MeasurementUnit.METRIC -> {
                maximumFractionDigits = 0
                isParseIntegerOnly = true
            }
        }
        roundingMode = RoundingMode.HALF_UP
    }
    return if (pressure != null) {
        if (unit == MeasurementUnit.IMPERIAL) {
            getString(
                R.string.template_pressure_imperial,
                formatter.format(pressure.hectoPascalToPsi())
            )
        } else {
            getString(
                R.string.template_pressure_metric,
                formatter.format(pressure)
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
        null, 0f -> {
            SpannableString(getString(R.string.placeholder_total_precipitation_none))
        }
        else -> {
            SpannableStringBuilder()
                .append(formatPrecipitationValue(precipitation, unit))
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
        null, 0f -> {
            SpannableString(getString(R.string.precipitation_forecast_none))
        }
        else -> {
            SpannableStringBuilder()
                .append(formatPrecipitationValue(precipitationForecast, unit))
                .append(getString(R.string.amount_of_precipitation_forecast))
                .toSpannable()
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