package hr.dtakac.prognoza.utils

import android.text.format.DateUtils
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.SIGNIFICANT_PRECIPITATION_IMPERIAL
import hr.dtakac.prognoza.SIGNIFICANT_PRECIPITATION_METRIC
import hr.dtakac.prognoza.uimodel.MeasurementUnit
import java.math.RoundingMode
import java.text.DecimalFormat
import java.time.ZonedDateTime
import java.util.*

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
    ): String {
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
            stringResource(id = R.string.placeholder_precipitation)
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
            if (convertedPrecipitation >= significantPrecipitation) {
                stringResource(
                    id = template,
                    formatter.format(convertedPrecipitation)
                )
            } else {
                stringResource(
                    id = insignificantTemplate,
                    formatter.format(significantPrecipitation)
                )
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
}
