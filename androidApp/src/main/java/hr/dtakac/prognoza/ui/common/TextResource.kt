package hr.dtakac.prognoza.ui.common

import android.content.Context
import android.icu.number.NumberFormatter
import android.icu.number.Precision
import android.icu.util.MeasureUnit
import android.text.format.DateFormat
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import hr.dtakac.prognoza.shared.entity.*
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
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

    fun fromNumberToInt(value: Number): TextResource =
      IntTextResource(value)

    fun fromTemperature(temperature: Temperature): TextResource =
      TemperatureTextResource(temperature)

    fun fromUnixSecondToShortTime(
      unixSecond: Long,
      timeZone: TimeZone
    ): TextResource = TimeTextResource(unixSecond, timeZone)

    fun fromUnixSecondToShortDayOfWeek(
      unixSecond: Long,
      timeZone: TimeZone
    ): TextResource = DayOfWeekTextResource(unixSecond, timeZone)

    fun fromPercentage(percentage: Int): TextResource =
      PercentageTextResource(percentage)

    fun fromLength(length: Length): TextResource =
      LengthTextResource(length)

    fun fromSpeed(speed: Speed): TextResource =
      SpeedTextResource(speed)
  }

  fun asString(context: Context): String

  @Composable
  fun asString(): String = asString(LocalContext.current)
}

private data class StringTextResource(val text: String) : TextResource {
  override fun asString(context: Context): String = text
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

private data class IntTextResource(val value: Number) : TextResource {
  override fun asString(context: Context): String =
    NumberFormatter
      .withLocale(context.supportedLocaleOrDefault)
      .precision(Precision.integer())
      .roundingMode(RoundingMode.HALF_UP)
      .format(value)
      .toString()
}

private data class TemperatureTextResource(val temperature: Temperature) : TextResource {
  override fun asString(context: Context): String =
    NumberFormatter
      .withLocale(context.supportedLocaleOrDefault)
      .unit(MeasureUnit.DEGREE)
      .unitWidth(NumberFormatter.UnitWidth.NARROW)
      .precision(Precision.integer())
      .roundingMode(RoundingMode.HALF_UP)
      .format(temperature.value)
      .toString()
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
    return DateTimeFormatter
      .ofPattern(pattern)
      .withLocale(context.supportedLocaleOrDefault)
      .format(localDateTime)
  }
}

private data class PercentageTextResource(val percentage: Int) : TextResource {
  override fun asString(context: Context): String =
    NumberFormatter
      .withLocale(context.supportedLocaleOrDefault)
      .unit(MeasureUnit.PERCENT)
      .unitWidth(NumberFormatter.UnitWidth.NARROW)
      .format(percentage)
      .toString()
}

private data class DayOfWeekTextResource(
  val unixSecond: Long,
  val timeZone: TimeZone
) : TextResource {
  override fun asString(context: Context): String {
    val localDateTime = Instant.fromEpochSeconds(unixSecond)
      .toLocalDateTime(timeZone)
      .toJavaLocalDateTime()
    return DateTimeFormatter
      .ofPattern("E")
      .withLocale(context.supportedLocaleOrDefault)
      .format(localDateTime)
  }
}

private data class LengthTextResource(val length: Length) : TextResource {
  override fun asString(context: Context): String =
    NumberFormatter
      .withLocale(context.supportedLocaleOrDefault)
      .unit(
        when (length.unit) {
          LengthUnit.Metre -> MeasureUnit.METER
          LengthUnit.Millimetre -> MeasureUnit.MILLIMETER
          LengthUnit.Centimetre -> MeasureUnit.CENTIMETER
          LengthUnit.Kilometre -> MeasureUnit.KILOMETER
          LengthUnit.Inch -> MeasureUnit.INCH
          LengthUnit.Foot -> MeasureUnit.FOOT
          LengthUnit.Mile -> MeasureUnit.MILE
        }
      )
      .unitWidth(NumberFormatter.UnitWidth.SHORT)
      .precision(
        when (length.unit) {
          LengthUnit.Metre,
          LengthUnit.Kilometre,
          LengthUnit.Foot,
          LengthUnit.Mile -> Precision.integer()
          LengthUnit.Millimetre,
          LengthUnit.Centimetre -> Precision.maxSignificantDigits(1)
          LengthUnit.Inch -> Precision.maxSignificantDigits(2)
        }
      )
      .format(length.value)
      .toString()
}

private data class SpeedTextResource(val speed: Speed) : TextResource {
  override fun asString(context: Context): String {
    return NumberFormatter
      .withLocale(context.supportedLocaleOrDefault)
      .unit(
        when (speed.unit) {
          SpeedUnit.MetrePerSecond -> MeasureUnit.METER_PER_SECOND
          SpeedUnit.KilometrePerHour -> MeasureUnit.KILOMETER_PER_HOUR
          SpeedUnit.MilePerHour -> MeasureUnit.MILE_PER_HOUR
          SpeedUnit.Knot -> MeasureUnit.KNOT
        }
      )
      .unitWidth(NumberFormatter.UnitWidth.SHORT)
      .precision(Precision.integer())
      .format(speed.value)
      .toString()
  }
}