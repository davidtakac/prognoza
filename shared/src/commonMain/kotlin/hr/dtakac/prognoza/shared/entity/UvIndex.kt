package hr.dtakac.prognoza.shared.entity

class UvIndex internal constructor(val value: Int) : Comparable<UvIndex> {
  init {
    if (value < 0) throwInvalidValue()
  }

  override fun compareTo(other: UvIndex): Int = value.compareTo(other.value)

  override fun toString(): String = value.toString()

  val isDangerous: Boolean = value > 2

  private fun throwInvalidValue(): Nothing =
    throw IllegalStateException("UV Index value must be positive, was ${toString()}")
}