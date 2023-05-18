package hr.dtakac.prognoza.shared.entity

data class Place internal constructor(
    val coordinates: Coordinates,
    val name: String,
    val details: String?
)