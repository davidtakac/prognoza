package hr.dtakac.prognoza.shared.platform

interface DotDecimalFormatter {
    fun format(value: Double, decimalPlaces: Int): String
}