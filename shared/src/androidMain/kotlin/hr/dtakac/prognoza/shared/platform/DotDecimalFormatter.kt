package hr.dtakac.prognoza.shared.platform

import java.util.*

internal actual class DotDecimalFormatter {
  actual fun format(value: Double, decimalPlaces: Int): String = String.format(
    Locale.ROOT,
    "%.${decimalPlaces}f",
    value
  )
}