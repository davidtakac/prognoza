package hr.dtakac.prognoza.core.formatting

import android.icu.text.RelativeDateTimeFormatter
import android.text.format.DateUtils
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import hr.dtakac.prognoza.core.R
import hr.dtakac.prognoza.core.model.ui.MeasurementUnit
import hr.dtakac.prognoza.core.model.ui.RepresentativeWeatherDescription
import hr.dtakac.prognoza.core.utils.*
import java.lang.IllegalStateException
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.NumberFormat
import java.time.ZonedDateTime
import java.util.*
import kotlin.math.roundToInt

@Composable
fun formatCurrentHourHeaderTime(time: ZonedDateTime): String {
    return DateUtils.formatDateTime(
        LocalContext.current,
        time.toInstant().toEpochMilli(),
        DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_TIME or DateUtils.FORMAT_ABBREV_ALL
    )
}

@Composable
fun formatDaySummaryTime(time: ZonedDateTime): String {
    return DateUtils.formatDateTime(
        LocalContext.current,
        time.toInstant().toEpochMilli(),
        DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_WEEKDAY or DateUtils.FORMAT_ABBREV_ALL
    )
}

@Composable
fun formatTomorrowTime(): String {
    return RelativeDateTimeFormatter.getInstance().format(
        RelativeDateTimeFormatter.Direction.NEXT,
        RelativeDateTimeFormatter.AbsoluteUnit.DAY
    ).replaceFirstChar { it.uppercaseChar() }
}

@Composable
fun formatTemperatureValue(temperature: Double?, unit: MeasurementUnit): String {
    val formatter = DecimalFormat.getInstance(Locale.getDefault()).apply {
        maximumFractionDigits = 0
        isParseIntegerOnly = true
        roundingMode = RoundingMode.HALF_UP
    }
    return when {
        temperature == null -> {
            stringResource(R.string.placeholder_temperature)
        }
        unit == MeasurementUnit.IMPERIAL -> {
            stringResource(
                id = R.string.template_temperature_universal,
                formatter.format(temperature.degreesCelsiusToDegreesFahrenheit())
            )
        }
        else -> {
            stringResource(
                id = R.string.template_temperature_universal,
                formatter.format(temperature)
            )
        }
    }
}

@Composable
fun formatPrecipitationValue(
    precipitationMetric: Double?,
    preferredUnit: MeasurementUnit
): String {
    val formatter = getPrecipitationFormatter(preferredUnit)
    if (!precipitationExists(precipitationMetric)) {
        throw IllegalStateException("Precipitation is nonexistent.")
    }
    return when(preferredUnit) {
        MeasurementUnit.METRIC -> {
            stringResource(
                id = R.string.template_precipitation_metric,
                formatter.format(precipitationMetric)
            )
        }
        MeasurementUnit.IMPERIAL -> {
            stringResource(
                id = R.string.template_precipitation_imperial,
                formatter.format(precipitationMetric?.millimetresToInches())
            )
        }
    }
}

private fun getPrecipitationFormatter(preferredUnit: MeasurementUnit): NumberFormat {
    return DecimalFormat.getInstance(Locale.getDefault()).apply {
        maximumFractionDigits = when (preferredUnit) {
            MeasurementUnit.IMPERIAL -> {
                2
            }
            MeasurementUnit.METRIC -> {
                1
            }
        }
        roundingMode = RoundingMode.HALF_UP
    }
}

@Composable
fun formatWeatherIconDescription(id: Int?): String {
    return if (id == null) {
        stringResource(R.string.placeholder_description)
    } else {
        stringResource(id)
    }
}

@Composable
fun formatFeelsLike(feelsLike: Double?, unit: MeasurementUnit): String {
    return stringResource(
        R.string.template_feels_like,
        if (feelsLike == null) {
            stringResource(R.string.placeholder_temperature)
        } else {
            formatTemperatureValue(
                temperature = feelsLike,
                unit = unit
            )
        }
    )
}

@Composable
fun formatHourTime(time: ZonedDateTime): String {
    return DateUtils.formatDateTime(
        LocalContext.current,
        time.toInstant().toEpochMilli(),
        DateUtils.FORMAT_SHOW_TIME or DateUtils.FORMAT_ABBREV_TIME
    )
}

@Composable
fun formatWindSpeedValue(windSpeed: Double?, unit: MeasurementUnit): String {
    val formatter = DecimalFormat.getInstance(Locale.getDefault()).apply {
        maximumFractionDigits = 1
        roundingMode = RoundingMode.HALF_UP
    }
    return if (windSpeed != null) {
        if (unit == MeasurementUnit.IMPERIAL) {
            stringResource(
                id = R.string.template_wind_imperial,
                formatter.format(windSpeed.metersPerSecondToMilesPerHour())
            )
        } else {
            stringResource(
                id = R.string.template_wind_metric,
                formatter.format(windSpeed.metersPerSecondToKilometresPerHour())
            )
        }
    } else {
        stringResource(R.string.placeholder_wind_speed)
    }
}

@Composable
fun formatWindWithDirection(
    windSpeed: Double?,
    windFromCompassDirection: Int?,
    windSpeedUnit: MeasurementUnit
): String {
    return stringResource(
        id = R.string.template_wind_with_direction,
        formatWindSpeedValue(windSpeed, windSpeedUnit),
        stringResource(windFromCompassDirection ?: R.string.placeholder_wind_direction)
    )
}

@Composable
fun formatHumidityValue(relativeHumidity: Double?): String {
    val formatter = DecimalFormat.getInstance(Locale.getDefault()).apply {
        maximumFractionDigits = 0
        isParseIntegerOnly = true
        roundingMode = RoundingMode.HALF_UP
    }
    return if (relativeHumidity != null) {
        stringResource(
            id = R.string.template_humidity_universal,
            formatter.format(relativeHumidity.roundToInt())
        )
    } else {
        stringResource(R.string.placeholder_humidity)
    }
}

@Composable
fun formatPressureValue(pressure: Double?, unit: MeasurementUnit): String {
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
            stringResource(
                id = R.string.template_pressure_imperial,
                formatter.format(pressure.hectoPascalToPsi())
            )
        } else {
            stringResource(
                id = R.string.template_pressure_metric,
                formatter.format(pressure)
            )
        }
    } else {
        stringResource(R.string.placeholder_pressure)
    }
}

@Composable
fun formatRepresentativeWeatherIconDescription(
    representativeWeatherDescription: RepresentativeWeatherDescription?
): String {
    val weatherIconDescriptionString =
        formatWeatherIconDescription(representativeWeatherDescription?.weatherDescription?.descriptionResourceId)
    return if (representativeWeatherDescription?.isMostly == true) {
        stringResource(
            id = R.string.template_mostly,
            weatherIconDescriptionString.lowercase()
        )
    } else {
        weatherIconDescriptionString
    }
}