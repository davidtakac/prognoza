package hr.dtakac.prognoza.shared.entity

data class Place(
    val name: String,
    val details: String?,
    val latitude: Double,
    val longitude: Double
) {
    init {
        if (latitude !in -90.0..90.0) {
            throw IllegalStateException("Latitude value must be in [-90,90], was $latitude.")
        }
        if (longitude !in -180.0..180.0) {
            throw IllegalStateException("Longitude value must be in [-180,180], was $longitude.")
        }
    }
}