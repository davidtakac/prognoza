package hr.dtakac.prognoza.shared.entity

class Coordinates(
    val latitude: Double,
    val longitude: Double
) {
    init {
        if (latitude !in -90.0..90.0) throwInvalidLatitude()
        if (longitude !in -180.0..180.0) throwInvalidLongitude()
    }

    private fun throwInvalidLatitude(): Nothing =
        throw IllegalStateException("Latitude value must be in [-90,90], was $latitude.")

    private fun throwInvalidLongitude(): Nothing =
        throw IllegalStateException("Longitude value must be in [-180,180], was $longitude.")
}