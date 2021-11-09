package hr.dtakac.prognoza.core.utils

import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.runtime.Composable
import hr.dtakac.prognoza.core.model.ui.MeasurementUnit

fun shouldShowPrecipitation(precipitation: Double?): Boolean {
    return precipitation != null && precipitation > 0
}

fun isPrecipitationSignificant(
    precipitation: Double?,
    unit: MeasurementUnit
): Boolean {
    return precipitation != null && precipitation > 0 && precipitation >= when (unit) {
        MeasurementUnit.METRIC -> SIGNIFICANT_PRECIPITATION_METRIC
        else -> SIGNIFICANT_PRECIPITATION_IMPERIAL
    }
}

@Composable
fun contentAlphaForPrecipitation(precipitation: Double?, unit: MeasurementUnit): Float {
    return if (isPrecipitationSignificant(precipitation, unit)) {
        LocalContentAlpha.current
    } else {
        ContentAlpha.medium
    }
}