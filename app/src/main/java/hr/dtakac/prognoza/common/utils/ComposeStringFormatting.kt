package hr.dtakac.prognoza.common.utils

import android.text.format.DateUtils
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.SIGNIFICANT_PRECIPITATION_IMPERIAL
import hr.dtakac.prognoza.SIGNIFICANT_PRECIPITATION_METRIC
import hr.dtakac.prognoza.theme.AppTheme
import hr.dtakac.prognoza.model.ui.MeasurementUnit
import hr.dtakac.prognoza.model.ui.RepresentativeWeatherDescription
import java.math.RoundingMode
import java.text.DecimalFormat
import java.time.ZonedDateTime
import java.util.*
import kotlin.math.roundToInt

object ComposeStringFormatting {
    @Composable
    fun formatCurrentHourHeaderTime(time: ZonedDateTime): String {
        return DateUtils.formatDateTime(
            LocalContext.current,
            time.toInstant().toEpochMilli(),
            DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_TIME or DateUtils.FORMAT_ABBREV_TIME
        )
    }

    @Composable
    fun formatDaySummaryTime(time: ZonedDateTime): String {
        return DateUtils.formatDateTime(
            LocalContext.current,
            time.toInstant().toEpochMilli(),
            DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_WEEKDAY
        )
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
                stringResource(id = R.string.placeholder_temperature)
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
        precipitation: Double?,
        unit: MeasurementUnit
    ): AnnotatedString {
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
        return if (precipitation == null || precipitation == 0.0) {
            AnnotatedString(text = stringResource(id = R.string.placeholder_temperature))
        } else {
            val significantPrecipitation = if (unit == MeasurementUnit.IMPERIAL) {
                SIGNIFICANT_PRECIPITATION_IMPERIAL
            } else {
                SIGNIFICANT_PRECIPITATION_METRIC
            }
            val convertedPrecipitation = if (unit == MeasurementUnit.IMPERIAL) {
                precipitation.millimetresToInches()
            } else {
                precipitation
            }
            val template = if (unit == MeasurementUnit.IMPERIAL) {
                R.string.template_precipitation_imperial
            } else {
                R.string.template_precipitation_metric
            }
            val insignificantTemplate = if (unit == MeasurementUnit.IMPERIAL) {
                R.string.placeholder_precipitation_insignificant_imperial
            } else {
                R.string.placeholder_precipitation_insignificant_metric
            }
            val precipitationColor = if (convertedPrecipitation >= significantPrecipitation) {
                AppTheme.precipitationColors.significant
            } else {
                AppTheme.precipitationColors.insignificant
            }
            if (convertedPrecipitation >= significantPrecipitation) {
                buildAnnotatedString {
                    withStyle(style = SpanStyle(color = precipitationColor)) {
                        append(
                            text = stringResource(
                                id = template,
                                formatter.format(convertedPrecipitation)
                            )
                        )
                    }
                }
            } else {
                buildAnnotatedString {
                    withStyle(style = SpanStyle(color = precipitationColor)) {
                        append(
                            text = stringResource(
                                id = insignificantTemplate,
                                formatter.format(significantPrecipitation)
                            )
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun formatWeatherIconDescription(id: Int?): String {
        return if (id == null) {
            stringResource(id = R.string.placeholder_description)
        } else {
            stringResource(id = id)
        }
    }

    @Composable
    fun formatFeelsLike(feelsLike: Double?, unit: MeasurementUnit): String {
        return stringResource(
            R.string.template_feels_like,
            if (feelsLike == null) {
                stringResource(id = R.string.placeholder_temperature)
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
            stringResource(id = R.string.placeholder_wind_speed)
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
            stringResource(id = R.string.placeholder_humidity)
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
            stringResource(id = R.string.placeholder_pressure)
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
}
