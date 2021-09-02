package hr.dtakac.prognoza.extensions

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import androidx.core.text.color
import androidx.core.text.toSpannable
import com.google.android.material.color.MaterialColors
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.SIGNIFICANT_PRECIPITATION_IMPERIAL
import hr.dtakac.prognoza.SIGNIFICANT_PRECIPITATION_METRIC
import hr.dtakac.prognoza.uimodel.MeasurementUnit
import hr.dtakac.prognoza.uimodel.RepresentativeWeatherDescription
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*
import kotlin.math.roundToInt

fun Context.formatTemperatureValue(temperature: Float?, unit: MeasurementUnit): String {
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

fun Context.formatPrecipitationValue(
    precipitation: Float?,
    unit: MeasurementUnit,
    significantPrecipitationColor: Boolean = true
): Spannable {
    val formatter = DecimalFormat.getInstance(Locale.getDefault()).apply {
        maximumFractionDigits = when (unit) {
            MeasurementUnit.IMPERIAL -> {
                2
            }
            MeasurementUnit.METRIC -> {
                1
            }
        }
        roundingMode = RoundingMode.HALF_UP
    }
    return if (precipitation == null) {
        SpannableString(getString(R.string.placeholder_precipitation))
    } else if (precipitation == 0f) {
        SpannableString(getString(R.string.placeholder_precipitation))
    } else if (unit == MeasurementUnit.IMPERIAL) {
        val convertedPrecipitation = precipitation.millimetresToInches()
        if (convertedPrecipitation >= SIGNIFICANT_PRECIPITATION_IMPERIAL) {
            val builder = SpannableStringBuilder()
            val precipitationString = getString(
                R.string.template_precipitation_imperial,
                formatter.format(convertedPrecipitation)
            )
            if (significantPrecipitationColor) {
                builder.color(
                    MaterialColors.getColor(
                        this,
                        R.attr.colorSignificantPrecipitation,
                        getColor(R.color.blue_700)
                    )
                ) {
                    append(precipitationString)
                }
            } else {
                precipitationString.toSpannable()
            }
        } else {
            getString(
                R.string.placeholder_precipitation_insignificant_imperial,
                formatter.format(SIGNIFICANT_PRECIPITATION_IMPERIAL)
            ).toSpannable()
        }
    } else {
        if (precipitation >= SIGNIFICANT_PRECIPITATION_METRIC) {
            val builder = SpannableStringBuilder()
            val precipitationString = getString(
                R.string.template_precipitation_metric,
                formatter.format(precipitation)
            )
            if (significantPrecipitationColor) {
                builder.color(
                    MaterialColors.getColor(
                        this,
                        R.attr.colorSignificantPrecipitation,
                        getColor(R.color.blue_700)
                    )
                ) {
                    append(precipitationString)
                }
            } else {
                precipitationString.toSpannable()
            }
        } else {
            getString(
                R.string.placeholder_precipitation_insignificant_metric,
                formatter.format(SIGNIFICANT_PRECIPITATION_METRIC)
            ).toSpannable()
        }
    }
}

fun Context.formatWeatherIconDescription(descriptionResourceId: Int?): String {
    return if (descriptionResourceId == null) {
        getString(R.string.placeholder_description)
    } else {
        getString(descriptionResourceId)
    }
}

fun Context.formatWindSpeedValue(windSpeed: Float?, unit: MeasurementUnit): String {
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

fun Context.formatWindWithDirection(
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

fun Context.formatHumidityValue(relativeHumidity: Float?): String {
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

fun Context.formatPressureValue(pressure: Float?, unit: MeasurementUnit): String {
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

fun Context.formatRepresentativeWeatherIconDescription(
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

fun Context.formatTotalPrecipitation(
    precipitation: Float?,
    unit: MeasurementUnit,
    significantPrecipitationColor: Boolean = true
): Spannable {
    return when (precipitation) {
        null, 0f -> {
            SpannableString(getString(R.string.placeholder_precipitation_text))
        }
        else -> {
            SpannableStringBuilder()
                .append(formatPrecipitationValue(precipitation, unit, significantPrecipitationColor))
                .append(" ")
                .append(getString(R.string.precipitation_total))
                .toSpannable()
        }
    }
}

fun Context.formatEmptyMessage(reasonResourceId: Int?): String {
    return getString(reasonResourceId ?: R.string.error_generic)
}

fun Context.formatPrecipitationTwoHours(
    precipitationForecast: Float?,
    unit: MeasurementUnit,
    significantPrecipitationColor: Boolean = true
): Spannable {
    return SpannableStringBuilder()
        .append(
            when (precipitationForecast) {
                null, 0f -> getString(R.string.placeholder_precipitation_text)
                else -> formatPrecipitationValue(precipitationForecast, unit, significantPrecipitationColor)
            }
        )
        .append(" ")
        .append(getString(R.string.in_two_hours))
        .toSpannable()
}

fun Context.formatFeelsLike(feelsLike: Float?, unit: MeasurementUnit): String {
    return getString(
        R.string.template_feels_like,
        if (feelsLike == null) {
            getString(R.string.placeholder_temperature)
        } else {
            formatTemperatureValue(feelsLike, unit)
        }
    )
}