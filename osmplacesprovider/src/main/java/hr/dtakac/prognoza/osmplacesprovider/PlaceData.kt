package hr.dtakac.prognoza.osmplacesprovider

import hr.dtakac.prognoza.entities.Place
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlaceResponse(
    @SerialName("place_id")
    val id: String,
    @SerialName("lat")
    val latitude: Double,
    @SerialName("lon")
    val longitude: Double,
    @SerialName("display_name")
    val displayName: String
)

fun PlaceResponse.toEntity(): Place {
    return Place(
        name = displayName.split(", ").getOrNull(0) ?: displayName,
        details = displayName,
        latitude = latitude,
        longitude = longitude
    )
}