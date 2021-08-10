package hr.dtakac.prognoza.common.util

import android.content.res.Resources
import hr.dtakac.prognoza.R
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

fun Resources.formatFeelsLikeDescription(feelsLike: Float?): String {
    return getString(
        R.string.template_feels_like,
        if (feelsLike == null) {
            getString(R.string.placeholder_temperature)
        } else {
            formatTemperatureValue(feelsLike)
        }
    )
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

fun Resources.formatWindDescription(windSpeed: Float?, windFromCompassDirection: Int?): String {
    return getString(
        R.string.template_wind_description,
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

fun Resources.formatHumidityDescription(relativeHumidity: Float?): String {
    return getString(
        R.string.template_humidity_description,
        formatHumidityValue(relativeHumidity)
    )
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

fun Resources.formatPressureDescription(pressure: Float?): String {
    return getString(
        R.string.template_pressure_description,
        formatPressureValue(pressure)
    )
}

fun Resources.formatTemperatureHighLow(high: Float?, low: Float?): String {
    return getString(
        R.string.template_temperature_high_low,
        formatTemperatureValue(high),
        formatTemperatureValue(low)
    )
}

fun Resources.formatWeatherIconDescriptionMostly(descriptionResourceId: Int?): String {
    return getString(
        R.string.template_mostly,
        formatWeatherIconDescription(descriptionResourceId).lowercase()
    )
}

fun Resources.formatTotalPrecipitation(precipitation: Float?): String {
    return getString(R.string.template_total_precipitation, formatPrecipitationValue(precipitation))
}

fun Resources.formatEmptyMessage(reasonResourceId: Int?): String {
    return if (reasonResourceId != null) {
        getString(
            R.string.template_error_forecast_empty_reason,
            getString(reasonResourceId)
        )
    } else {
        getString(R.string.error_forecast_empty)
    }
}