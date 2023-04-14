package hr.dtakac.prognoza.shared.data.openmeteo.geocoding

import hr.dtakac.prognoza.shared.domain.data.PlaceProvider
import hr.dtakac.prognoza.shared.entity.Place
import io.github.aakira.napier.Napier
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

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