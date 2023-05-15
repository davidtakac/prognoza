package hr.dtakac.prognoza.shared.entity

import kotlinx.datetime.TimeZone

class Place internal constructor(
    val id: String,
    val name: String,
    val timeZone: TimeZone,
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