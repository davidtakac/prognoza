package hr.dtakac.prognoza.core.utils

import java.lang.IllegalStateException

fun precipitationExists(precipitation: Double?): Boolean {
    return precipitation != null && precipitation > 0
}

fun isPrecipitationSignificant(precipitationMetric: Double?): Boolean {
    return precipitationExists(precipitationMetric) &&
            (precipitationMetric ?: throw IllegalStateException("Precipitation exists but is null.")) >= SIGNIFICANT_PRECIPITATION_METRIC
}