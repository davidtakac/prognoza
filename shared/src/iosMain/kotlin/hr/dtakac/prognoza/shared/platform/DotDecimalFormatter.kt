package hr.dtakac.prognoza.shared.platform

import platform.Foundation.*

internal actual class DotDecimalFormatter {
    actual fun format(value: Double, decimalPlaces: Int): String {
        return NSString.stringWithFormat("%.${decimalPlaces}f", value)
    }
}