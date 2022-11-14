package hr.dtakac.prognoza.platform

import hr.dtakac.prognoza.shared.platform.DotDecimalFormatter
import java.util.*

class AndroidDotDecimalFormatter : DotDecimalFormatter {
    override fun format(value: Double, decimalPlaces: Int): String = String.format(
        Locale.ROOT,
        "%.${decimalPlaces}f",
        value
    )
}