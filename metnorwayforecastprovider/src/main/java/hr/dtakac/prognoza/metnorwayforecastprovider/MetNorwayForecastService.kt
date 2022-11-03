package hr.dtakac.prognoza.metnorwayforecastprovider

import hr.dtakac.prognoza.metnorwayforecastprovider.database.converter.Rfc1123DateTimeConverter
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import java.time.ZonedDateTime
import java.util.*

class MetNorwayForecastService(
    private val client: HttpClient,
    private val userAgent: String,
    private val baseUrl: String
) {
    suspend fun getForecast(
        ifModifiedSince: ZonedDateTime?,
        latitude: Double,
        longitude: Double
    ): HttpResponse = client.get(urlString = "$baseUrl/locationforecast/2.0/compact") {
        header(HttpHeaders.UserAgent, userAgent)
        header(HttpHeaders.IfModifiedSince, Rfc1123DateTimeConverter.toTimestamp(ifModifiedSince))
        parameter("lat", formatCoordinate(latitude))
        parameter("lon", formatCoordinate(longitude))
    }

    private fun formatCoordinate(value: Double): String = String.format(Locale.ROOT, "%.2f", value)
}