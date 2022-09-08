package hr.dtakac.prognoza.presentation.strings

import android.content.Context
import android.icu.text.NumberFormat
import android.text.format.DateUtils
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

sealed class TextResource {
    companion object {
        fun fromText(text: String): TextResource =
            SimpleTextResource(text)

        fun fromStringId(@StringRes id: Int): TextResource =
            IdTextResource(id)

        fun fromStringIdWithArgs(@StringRes id: Int, vararg args: Any): TextResource =
            IdTextResourceWithArgs(id, args.toList())

        fun fromEpochMillis(millis: Long, flags: Int): TextResource =
            DateTimeTextResource(millis, flags)

        fun fromNumber(number: Number, decimalPlaces: Int = 0): TextResource =
            NumberTextResource(number, decimalPlaces)
    }

    abstract fun asString(context: Context): String
}

@Composable
fun TextResource.asString(): String = asString(LocalContext.current)

private data class SimpleTextResource(
    val text: String
) : TextResource() {
    override fun asString(context: Context): String = text
}

private data class IdTextResource(
    @StringRes val id: Int
) : TextResource() {
    override fun asString(context: Context): String = context.getString(id)
}

private data class DateTimeTextResource(
    val millis: Long,
    val flags: Int
) : TextResource() {
    override fun asString(context: Context): String =
        DateUtils.formatDateTime(context, millis, flags)
}

private data class NumberTextResource(
    val number: Number,
    val decimalPlaces: Int
) : TextResource() {
    override fun asString(context: Context): String = NumberFormat.getInstance().apply {
        maximumFractionDigits = decimalPlaces
    }.format(number)
}

private data class IdTextResourceWithArgs(
    @StringRes val id: Int,
    val args: List<Any>
) : TextResource() {
    override fun asString(context: Context): String = context.getString(
        id,
        *args.map { if (it is TextResource) it.asString(context) else it }.toTypedArray()
    )
}