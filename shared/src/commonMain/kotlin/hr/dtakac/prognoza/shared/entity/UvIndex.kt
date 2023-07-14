package hr.dtakac.prognoza.shared.entity

class UvIndex internal constructor(val value: Int) : Comparable<UvIndex> {
  init {
    if (value < 0) throwInvalidValue()
  }

  override fun compareTo(other: UvIndex): Int = value.compareTo(other.value)

  override fun toString(): String = value.toString()

  val isDangerous: Boolean = value > 2

  val description: Description =
    when {
      value > 10 -> Description.Extreme
      value > 7 -> Description.VeryHigh
      value > 5 -> Description.High
      value > 2 -> Description.Moderate
      else -> Description.Low
    }

  enum class Description {
    Low,
    Moderate,
    High,
    VeryHigh,
    Extreme
  }

  private fun throwInvalidValue(): Nothing =
    throw IllegalStateException("UV Index value must be positive, was ${toString()}")
}