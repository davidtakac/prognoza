package hr.dtakac.prognoza.shared.entity

class UvIndex internal constructor(val value: Double) : Comparable<UvIndex> {
  init {
    if (value < 0.0) throwInvalidValue()
  }

  override fun compareTo(other: UvIndex): Int = value.compareTo(other.value)

  val description: UvIndexDescription = when {
    value > 10 -> UvIndexDescription.Extreme
    value > 7 -> UvIndexDescription.VeryHigh
    value > 5 -> UvIndexDescription.High
    value > 2 -> UvIndexDescription.Moderate
    else -> UvIndexDescription.Low
  }

  val useProtection: Boolean = description > UvIndexDescription.Low

  private fun throwInvalidValue(): Nothing =
    throw IllegalStateException("UV Index value must be positive, was ${toString()}")
}

enum class UvIndexDescription {
  Low,
  Moderate,
  High,
  VeryHigh,
  Extreme
}