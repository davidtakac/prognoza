package hr.dtakac.prognoza

import android.content.res.Resources
import android.util.TypedValue
import java.time.ZoneId
import java.time.ZonedDateTime

fun ZonedDateTime.atStartOfDay(): ZonedDateTime =
    toLocalDate().atStartOfDay(ZoneId.systemDefault())

fun <T> List<T>.mostCommon(): T? =
    groupingBy { it }.eachCount().maxByOrNull { it.value }?.key

// courtesy of https://stackoverflow.com/a/6327095
val Number.toPx
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    )