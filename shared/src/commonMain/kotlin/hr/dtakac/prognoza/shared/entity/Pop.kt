package hr.dtakac.prognoza.shared.entity

class Pop internal constructor(val preciseValue: Int) : Comparable<Pop> {
  val humanValue: Int =
    if (preciseValue < 10) {
      0
    } else {
      val remainder = preciseValue % 5
      preciseValue + if (remainder >= 3) 5 - remainder else -remainder
    }

  override fun compareTo(other: Pop): Int = preciseValue.compareTo(other.preciseValue)
}