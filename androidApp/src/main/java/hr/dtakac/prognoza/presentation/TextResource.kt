package hr.dtakac.prognoza.presentation

import android.content.Context
import android.icu.text.NumberFormat
import android.text.format.DateFormat
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.shared.entity.Temperature
import kotlinx.datetime.*
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.format.DateTimeFormatter

sealed interface TextResource {
    companion object {
        fun empty(): TextResource = fromString("")

        fun fromString(text: String): TextResource =
            StringTextResource(text)

        fun fromResId(@StringRes id: Int): TextResource =
            ResIdTextResource(id)

        fun fromResId(@StringRes id: Int, vararg args: Any): TextResource =
            ResIdTextResourceWithArgs(id, args.toList())

        fun fromTemperature(temperature: Temperature): TextResource =
            TemperatureTextResource(temperature)

        fun fromTime(
            unixSecond: Long,
            timeZone: TimeZone
        ): TextResource = TimeTextResource(unixSecond, timeZone)

        fun fromPercentage(percentage: Double): TextResource =
            PercentageTextResource(percentage)
    }

    fun asString(context: Context): String

    @Composable
    fun asString(): String = asString(LocalContext.current)
}

private data class StringTextResource(val text: String) : TextResource {
    override fun asString(context: Context): String =
        text
}

private data class ResIdTextResource(@StringRes val id: Int) : TextResource {
    override fun asString(context: Context): String =
        context.getString(id)
}

private data class ResIdTextResourceWithArgs(@StringRes val id: Int, val args: List<Any>) :
    TextResource {
    override fun asString(context: Context): String =
        context.getString(
            id,
            *args.map { if (it is TextResource) it.asString(context) else it }.toTypedArray()
        )
}

private data class NumberTextResource(
    val number: BigDecimal
) : TextResource {
    override fun asString(context: Context): String =
        NumberFormat
            .getInstance(context.resources.configuration.locales[0])
            .format(number)
}

private data class TemperatureTextResource(val temperature: Temperature) : TextResource {
    override fun asString(context: Context): String =
        context.getString(
            R.string.temperature_value,
            NumberTextResource(
                temperature.value
                    .toBigDecimal()
                    .setScale(0, RoundingMode.HALF_UP)
            ).asString(context)
        )
}

private data class TimeTextResource(
    val unixSecond: Long,
    val timeZone: TimeZone
) : TextResource {
    override fun asString(context: Context): String {
        val localDateTime = Instant.fromEpochSeconds(unixSecond)
            .toLocalDateTime(timeZone)
            .toJavaLocalDateTime()
        val use24Hr = DateFormat.is24HourFormat(context)
        val pattern = (if (use24Hr) "H" else "h") +
                (if (localDateTime.minute > 0) ":mm" else "") +
                (if (use24Hr) "" else " a")
        return DateTimeFormatter.ofPattern(pattern).format(localDateTime)
    }
}

private data class PercentageTextResource(val percentage: Double) : TextResource {
    override fun asString(context: Context): String =
        context.getString(
            R.string.percentage_value,
            NumberTextResource(
                percentage
                    .toBigDecimal()
                    .setScale(0, RoundingMode.HALF_UP)
            ).asString(context)
        )
}