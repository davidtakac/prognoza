package hr.dtakac.prognoza.shared.entity

data class Precipitation(
    val rain: Length,
    val showers: Length,
    val snow: Length,
    val combined: Length
)