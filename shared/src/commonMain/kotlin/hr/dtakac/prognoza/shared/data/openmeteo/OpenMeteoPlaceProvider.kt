package hr.dtakac.prognoza.shared.data.openmeteo

import hr.dtakac.prognoza.shared.domain.data.PlaceProvider
import hr.dtakac.prognoza.shared.entity.Place
import io.github.aakira.napier.Napier
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.datetime.TimeZone
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

internal class OpenMeteoPlaceProvider(
    private val client: HttpClient,
    private val userAgent: String
) : PlaceProvider {
    override suspend fun get(query: String, rfc2616Language: String): List<Place>? {
        val response = try {
            client.request("https://geocoding-api.open-meteo.com/v1/search") {
                header(HttpHeaders.UserAgent, userAgent)
                parameter("name", query)
                parameter("count", 10)
                parameter("language", rfc2616Language)
                parameter("format", "json")
            }.body<OpenMeteoGeocodingResponse>().results?.map(OpenMeteoPlace::toEntity)
        } catch (e: Exception) {
            Napier.e("OpenMeteoPlaceSearcher", e)
            null
        }
        return response
    }
}

@Serializable
private data class OpenMeteoGeocodingResponse(
    @SerialName("results")
    val results: List<OpenMeteoPlace>? = listOf()
)

@Serializable
private data class OpenMeteoPlace(
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

private fun OpenMeteoPlace.toEntity(): Place {
    return Place(
        name = name,
        timeZone = TimeZone.of(timeZone),
        details = listOf(admin1, admin2, admin3, admin4)
            .filterNot { it.isNullOrBlank() }
            .joinToString(", ")
            .takeIf { it.isNotBlank() },
        latitude = latitude,
        longitude = longitude
    )
}