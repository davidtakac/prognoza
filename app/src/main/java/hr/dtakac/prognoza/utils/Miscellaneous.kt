package hr.dtakac.prognoza.utils

import android.content.res.Resources
import android.util.TypedValue
import hr.dtakac.prognoza.data.database.forecast.ForecastMeta

fun <T> List<T>.mostCommon(): T? =
    groupingBy { it }.eachCount().maxByOrNull { it.value }?.key

// courtesy of https://stackoverflow.com/a/6327095
val Number.toPx
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    )

fun hr.dtakac.prognoza.data.database.forecast.ForecastMeta.hasExpired(): Boolean = try {
    ZonedDateTime.now() > expires
} catch (e: Exception) {
    true
}