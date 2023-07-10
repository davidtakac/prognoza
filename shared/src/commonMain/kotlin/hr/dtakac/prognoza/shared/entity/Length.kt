package hr.dtakac.prognoza.shared.entity

class Length internal constructor(
  val value: Double,
  val unit: LengthUnit
) : Comparable<Length> {
  init {
    if (value < 0) throwInvalidLength()
  }

  operator fun plus(other: Length): Length = Length(
    value = value + other.valueIn(unit),
    unit = unit
  )

  override fun compareTo(other: Length): Int = value.compareTo(other.valueIn(unit))

  override fun toString(): String = "$value ${unit.suffix}"

  // From ChatGPT 3.5 for the prompt
  // "Can you map meteorological visibility in kilometres to qualitative measurements?"
  val visibility: Visibility = valueIn(LengthUnit.Kilometre).let { kilometres ->
    when {
      kilometres >= 10 -> Visibility.Excellent
      kilometres >= 5 -> Visibility.VeryGood
      kilometres >= 3 -> Visibility.Good
      kilometres >= 1 -> Visibility.Moderate
      kilometres >= 0.5 -> Visibility.Poor
      kilometres >= 0.2 -> Visibility.VeryPoor
      else -> Visibility.ExtremelyPoor
    }
  }

  internal fun convertTo(unit: LengthUnit) = Length(valueIn(unit), unit)

  private fun valueIn(unit: LengthUnit): Double =
    if (this.unit == unit) value else value * this.unit.metres / unit.metres

  private fun throwInvalidLength(): Nothing =
    throw IllegalStateException("Length must be positive, was ${toString()}.")
}

enum class LengthUnit(
  internal val metres: Double,
  internal val suffix: String
) {
  Metre(metres = 1.0, suffix = "m"),
  Millimetre(metres = 0.001, suffix = "mm"),
  Centimetre(metres = 0.01, suffix = "cm"),
  Kilometre(metres = 1000.0, suffix = "km"),
  Inch(metres = 0.0254, suffix = "in"),
  Foot(metres = 0.3048, suffix = "ft"),
  Mile(metres = 1609.34, suffix = "mi")
}

enum class Visibility {
  Excellent,
  VeryGood,
  Good,
  Moderate,
  Poor,
  VeryPoor,
  ExtremelyPoor
}