package hr.dtakac.prognoza.shared.entity

class UvIndex internal constructor(val preciseValue: Double) : Comparable<UvIndex> {
  init {
    if (preciseValue < 0) throwInvalidValue()
  }

  override fun compareTo(other: UvIndex): Int = preciseValue.compareTo(other.preciseValue)

  override fun toString(): String = preciseValue.toString()

  val isDangerous: Boolean = preciseValue >= Dangerous

  private fun throwInvalidValue(): Nothing =
    throw IllegalStateException("UV Index value must be positive, was ${toString()}")

  companion object {
    const val Dangerous = 3
  }
}