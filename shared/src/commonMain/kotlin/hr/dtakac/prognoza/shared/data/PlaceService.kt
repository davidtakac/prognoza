package hr.dtakac.prognoza.shared.data

import hr.dtakac.prognoza.shared.entity.Coordinates
import hr.dtakac.prognoza.shared.entity.Place
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

internal class PlaceService(private val client: HttpClient, private val userAgent: String) {
  suspend fun search(query: String, acceptLanguage: String): List<Place> = client
    .get(urlString = "https://api.open-meteo.com/v1/search") {
      header(HttpHeaders.UserAgent, userAgent)
      header(HttpHeaders.AcceptLanguage, acceptLanguage)
      parameter("q", query)
      parameter("format", "jsonv2")
    }
    .body<List<PlaceResponse>>()
    .map(PlaceResponse::toPlace)
}

@Serializable
private data class PlaceResponse(
  @SerialName("place_id")
  val id: String,
  @SerialName("lat")
  val latitude: Double,
  @SerialName("lon")
  val longitude: Double,
  @SerialName("display_name")
  val displayName: String
) {
  fun toPlace() =
    Place(
      coordinates = Coordinates(latitude, longitude),
      name = displayName.split(", ").getOrNull(0) ?: displayName,
      details = displayName
    )
}