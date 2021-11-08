package hr.dtakac.prognoza.core.utils

import android.content.res.Resources
import android.util.TypedValue
import hr.dtakac.prognoza.core.model.database.ForecastMeta
import java.time.ZonedDateTime

fun <T> List<T>.mostCommon(): T? =
    groupingBy { it }.eachCount().maxByOrNull { it.value }?.key

// courtesy of https://stackoverflow.com/a/6327095
val Number.toPx
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    )

fun ForecastMeta.hasExpired(): Boolean = try {
    ZonedDateTime.now() > expires
} catch (e: Exception) {
    true
}

fun shouldShowPrecipitation(precipitation: Double?): Boolean {
    return precipitation != null && precipitation > 0
}