package hr.dtakac.prognoza.shared.data.openmeteo.geocoding

import hr.dtakac.prognoza.shared.domain.data.PlaceSearcher
import hr.dtakac.prognoza.shared.entity.Place
import io.ktor.client.*
import io.ktor.client.request.*

internal class OpenMeteoPlaceSearcher(
    private val client: HttpClient,
    private val userAgent: String
) : PlaceSearcher {
    override suspend fun search(query: String, rfc2616Language: String): List<Place>? {
        val response = try {
            client.request("https://geocoding-api.open-meteo.com/v1/search") {
                header(HttpHeaders.UserAgent, userAgent)
                header(HttpHeaders.AcceptLanguage, language)
                parameter("name", query)
                parameter("count", 10)
                parameter("language", language)
                parameter("format", "json")
            }.body<OpenMeteoGeocodingResponse>()
        } catch (e: Exception) {
            Napier.e(e)
            null
        }
        return response
    }
}