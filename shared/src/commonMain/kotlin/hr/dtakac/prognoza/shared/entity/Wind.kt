package hr.dtakac.prognoza.shared.entity

data class Wind(
    val speed: Speed,
    val gust: Speed,
    val direction: Angle
)