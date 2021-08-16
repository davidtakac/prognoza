package hr.dtakac.prognoza.common.util

import android.content.res.Resources
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import androidx.core.text.color
import androidx.core.text.toSpannable
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.forecast.uimodel.RepresentativeWeatherIcon
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.roundToInt

fun Resources.formatTemperatureValue(temperature: Float?): String {
    return if (temperature == null) {
        getString(R.string.placeholder_temperature)
    } else {
        getString(
            R.string.template_temperature_universal,
            temperature.roundToInt()
        )
    }
}

fun Resources.formatPrecipitationValue(precipitation: Float?): String {
    return if (precipitation.isPrecipitationAmountSignificant()) {
        getString(
            R.string.template_precipitation_metric,
            precipitation
        )
    } else {
        getString(R.string.placeholder_precipitation)
    }
}

fun Resources.formatWeatherIconDescription(descriptionResourceId: Int?): String {
    return if (descriptionResourceId == null) {
        getString(R.string.placeholder_description)
    } else {
        getString(descriptionResourceId)
    }
}

fun Resources.formatWindSpeedValue(windSpeed: Float?): String {
    return if (windSpeed != null) {
        getString(R.string.template_wind_metric, windSpeed.toKilometresPerHour())
    } else {
        getString(R.string.placeholder_wind_speed)
    }
}

fun Resources.formatWindWithDirection(windSpeed: Float?, windFromCompassDirection: Int?): String {
    return getString(
        R.string.template_wind_with_direction,
        formatWindSpeedValue(windSpeed),
        getString(windFromCompassDirection ?: R.string.placeholder_wind_direction)
    )
}

fun Resources.formatHumidityValue(relativeHumidity: Float?): String {
    return if (relativeHumidity == null) {
        getString(R.string.placeholder_humidity)
    } else {
        getString(
            R.string.template_humidity_universal,
            relativeHumidity.roundToInt()
        )
    }
}

fun Resources.formatPressureValue(pressure: Float?): String {
    return if (pressure == null) {
        getString(R.string.placeholder_pressure)
    } else {
        getString(
            R.string.template_pressure_metric,
            pressure.roundToInt()
        )
    }
}

fun Resources.formatRepresentativeWeatherIconDescription(representativeWeatherIcon: RepresentativeWeatherIcon?): String {
    val weatherIconDescription =
        formatWeatherIconDescription(representativeWeatherIcon?.weatherIcon?.descriptionResourceId)
    return if (representativeWeatherIcon?.isMostly == true) {
        getString(
            R.string.template_mostly,
            weatherIconDescription.lowercase()
        )
    } else {
        weatherIconDescription
    }
}

fun Resources.formatTotalPrecipitation(precipitation: Float?): Spannable {
    return if (!precipitation.isPrecipitationAmountSignificant()) {
        SpannableString(getString(R.string.total_precipitation_none))
    } else {
        SpannableStringBuilder()
            .color(getColor(R.color.precipitation, null)) {
                append(
                    formatPrecipitationValue(
                        precipitation
                    )
                )
            }
            .append(getString(R.string.amount_of_precipitation))
            .toSpannable()
    }
}

fun Resources.formatEmptyMessage(reasonResourceId: Int?): String {
    return getString(reasonResourceId ?: R.string.error_generic)
}

fun Resources.formatPrecipitationTwoHours(precipitationForecast: Float?): Spannable {
    return if (precipitationForecast == null) {
        SpannableString(getString(R.string.precipitation_forecast_none))
    } else {
        SpannableStringBuilder()
            .color(getColor(R.color.precipitation, null)) {
                append(
                    formatPrecipitationValue(
                        precipitationForecast
                    )
                )
            }
            .append(getString(R.string.amount_of_precipitation_forecast))
    }
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