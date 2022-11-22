package hr.dtakac.prognoza.shared.data.openstreetmap

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

internal class OsmPlaceService(
    private val client: HttpClient,
    private val baseUrl: String,
    private val userAgent: String
) {
    suspend fun search(query: String, acceptLanguage: String): List<PlaceResponse> = client
        .get(urlString = "$baseUrl/search") {
            header(HttpHeaders.UserAgent, userAgent)
            header(HttpHeaders.AcceptLanguage, acceptLanguage)
            parameter("q", query)
            parameter("format", "jsonv2")
        }
        .body()
}