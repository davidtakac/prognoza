package hr.dtakac.prognoza.shared.platform

internal expect class DotDecimalFormatter {
  fun format(value: Double, decimalPlaces: Int): String
}