package hr.dtakac.prognoza

import hr.dtakac.prognoza.metnorwayforecastprovider.DotDecimalFormatter
import java.util.*

class AndroidDotDecimalFormatter : DotDecimalFormatter {
    override fun format(value: Double): String {
        return String.format(Locale.ROOT, "%.2f", value)
    }
}