package hr.dtakac.prognoza.shared.data.openmeteo.geocoding

import hr.dtakac.prognoza.shared.entity.Place
import kotlinx.datetime.TimeZone
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class OpenMeteoGeocodingResponse(
    @SerialName("results")
    val results: List<OpenMeteoPlace>? = listOf()
)

@Serializable
internal data class OpenMeteoPlace(
    @SerialName("latitude")
    val latitude: Double,
    @SerialName("longitude")
    val longitude: Double,
    @SerialName("timezone")
    val timeZone: String,
    @SerialName("name")
    val name: String,
    @SerialName("admin1")
    val admin1: String? = "",
    @SerialName("admin2")
    val admin2: String? = "",
    @SerialName("admin3")
    val admin3: String? = "",
    @SerialName("admin4")
    val admin4: String? = ""
)

internal fun OpenMeteoPlace.toEntity(): Place {
    return Place(
        name = name,
        timeZone = TimeZone.of(timeZone),
        details = listOf(admin1, admin2, admin3, admin4).filterNot { it.isNullOrBlank() }.joinToString(", "),
        latitude = latitude,
        longitude = longitude
    )
}