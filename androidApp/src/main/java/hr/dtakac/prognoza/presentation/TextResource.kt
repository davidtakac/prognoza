package hr.dtakac.prognoza.presentation

import android.content.Context
import android.icu.text.NumberFormat
import android.text.format.DateFormat
import android.text.format.DateUtils
import androidx.annotation.StringRes
import java.math.BigDecimal
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

sealed interface TextResource {
    companion object {
        fun empty(): TextResource = fromText("")

        fun fromText(text: String): TextResource =
            SimpleTextResource(text)

        fun fromStringId(@StringRes id: Int): TextResource =
            IdTextResource(id)

        fun fromStringId(@StringRes id: Int, vararg args: Any): TextResource =
            IdTextResourceWithArgs(id, args.toList())

        fun fromShortTime(epochMillis: Long): TextResource =
            ShortTimeTextResource(epochMillis)

        fun fromDate(epochMillis: Long): TextResource =
            DateUtilsTextResource(epochMillis, DateUtils.FORMAT_SHOW_DATE)

        fun fromShortDateAndWeekday(epochMillis: Long): TextResource =
            DateUtilsTextResource(
                millis = epochMillis,
                flags = DateUtils.FORMAT_SHOW_DATE or
                        DateUtils.FORMAT_SHOW_WEEKDAY or
                        DateUtils.FORMAT_ABBREV_ALL
            )

        fun fromNumber(number: BigDecimal): TextResource = NumberTextResource(number)
    }

    fun asString(context: Context): String
}

private data class SimpleTextResource(
    val text: String
) : TextResource {
    override fun asString(context: Context): String = text
}

private data class IdTextResource(
    @StringRes val id: Int
) : TextResource {
    override fun asString(context: Context): String = context.getString(id)
}

private data class ShortTimeTextResource(
    val epochMillis: Long
) : TextResource {
    override fun asString(context: Context): String {
        val zonedDateTime = Instant.ofEpochMilli(epochMillis).atZone(ZoneId.systemDefault())
        return DateTimeFormatter.ofPattern(
            if (DateFormat.is24HourFormat(context)) {
                "HH:mm"
            } else {
                if (zonedDateTime.minute > 0) "h:mm a" else "h a"
            }
        ).format(zonedDateTime)
    }
}

private data class DateUtilsTextResource(
    val millis: Long,
    val flags: Int
) : TextResource {
    override fun asString(context: Context): String =
        DateUtils.formatDateTime(context, millis, flags)
}

private data class NumberTextResource(
    val number: BigDecimal
) : TextResource {
    override fun asString(context: Context): String = NumberFormat
        .getInstance(Locale.getDefault())
        .format(number)
}

private data class IdTextResourceWithArgs(
    @StringRes val id: Int,
    val args: List<Any>
) : TextResource {
    override fun asString(context: Context): String = context.getString(
        id,
        *args.map { if (it is TextResource) it.asString(context) else it }.toTypedArray()
    )
}