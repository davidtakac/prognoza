package hr.dtakac.prognoza.forecast.composable.common

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.res.stringResource
import hr.dtakac.prognoza.core.R
import hr.dtakac.prognoza.core.formatting.formatPrecipitationValue
import hr.dtakac.prognoza.core.model.ui.MeasurementUnit
import hr.dtakac.prognoza.core.theme.PrognozaTheme
import hr.dtakac.prognoza.core.utils.SIGNIFICANT_PRECIPITATION_METRIC
import hr.dtakac.prognoza.core.utils.isPrecipitationSignificant
import hr.dtakac.prognoza.core.utils.precipitationExists

@Composable
fun HourSummaryPrecipitation(
    precipitationMetric: Double?,
    preferredUnit: MeasurementUnit
) {
    val isSignificant = isPrecipitationSignificant(precipitationMetric)
    CompositionLocalProvider(
        LocalContentAlpha provides if (isSignificant) ContentAlpha.high else LocalContentAlpha.current,
        LocalContentColor provides if (isSignificant) PrognozaTheme.precipitationColors.significant else LocalContentColor.current
    ) {
        Text(
            text = if (!precipitationExists(precipitationMetric)) {
                stringResource(R.string.placeholder_precipitation)
            } else if (!isPrecipitationSignificant(precipitationMetric)) {
                stringResource(
                    id = R.string.template_precipitation_insignificant,
                    formatPrecipitationValue(
                        precipitationMetric = SIGNIFICANT_PRECIPITATION_METRIC,
                        preferredUnit = preferredUnit
                    )
                )
            } else {
                formatPrecipitationValue(
                    precipitationMetric = precipitationMetric,
                    preferredUnit = preferredUnit
                )
            }
        )
    }
}

@Composable
fun DaySummaryPrecipitation(
    precipitationMetric: Double?,
    preferredUnit: MeasurementUnit
) {
    val isSignificant = isPrecipitationSignificant(precipitationMetric)
    CompositionLocalProvider(
        LocalContentAlpha provides if (isSignificant) ContentAlpha.high else LocalContentAlpha.current,
        LocalContentColor provides if (isSignificant) PrognozaTheme.precipitationColors.significant else LocalContentColor.current
    ) {
        Text(
            text = if (!precipitationExists(precipitationMetric)) {
                stringResource(R.string.placeholder_precipitation_text)
            } else if (!isPrecipitationSignificant(precipitationMetric)) {
                stringResource(
                    id = R.string.template_precipitation_insignificant,
                    formatPrecipitationValue(
                        precipitationMetric = SIGNIFICANT_PRECIPITATION_METRIC,
                        preferredUnit = preferredUnit
                    )
                )
            } else {
                formatPrecipitationValue(
                    precipitationMetric = precipitationMetric,
                    preferredUnit = preferredUnit
                )
            }
        )
    }
}

@Composable
fun TodayForecastHeaderPrecipitation(
    precipitationMetric: Double?,
    preferredUnit: MeasurementUnit
) {
    HourSummaryPrecipitation(
        precipitationMetric = precipitationMetric,
        preferredUnit = preferredUnit
    )
}