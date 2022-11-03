package hr.dtakac.prognoza.osmplacesearcher

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import java.util.*

class OsmPlaceService(
    private val client: HttpClient,
    private val baseUrl: String,
    private val userAgent: String
) {
    suspend fun search(query: String): List<PlaceResponse> = client
        .get(urlString = "$baseUrl/search") {
            header(HttpHeaders.UserAgent, userAgent)
            header(HttpHeaders.AcceptLanguage, Locale.getDefault().language)
            parameter("q", query)
            parameter("format", "jsonv2")
        }
        .body()
}