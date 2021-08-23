package hr.dtakac.prognoza.extensions

import android.content.res.Resources
import android.util.TypedValue
import hr.dtakac.prognoza.dbmodel.ForecastMeta
import java.time.ZoneId
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

fun ZonedDateTime.atStartOfDay(): ZonedDateTime =
    toLocalDate().atStartOfDay(ZoneId.systemDefault())

fun ForecastMeta.hasExpired(): Boolean = try {
    ZonedDateTime.now() > expires
} catch (e: Exception) {
    true
}