package hr.dtakac.prognoza.metnorwayforecastprovider

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class MetNorwayForecastService(
    private val client: HttpClient,
    private val userAgent: String,
    private val baseUrl: String,
    private val rfc1123Converter: Rfc1123Converter,
    private val dotDecimalFormatter: DotDecimalFormatter
) {
    suspend fun getForecast(
        ifModifiedSinceEpochMillis: Long?,
        latitude: Double,
        longitude: Double
    ): HttpResponse = client.get(urlString = "$baseUrl/locationforecast/2.0/compact") {
        header(HttpHeaders.UserAgent, userAgent)
        ifModifiedSinceEpochMillis?.let {
            header(HttpHeaders.IfModifiedSince, rfc1123Converter.format(ifModifiedSinceEpochMillis))
        }
        parameter("lat", formatCoordinate(latitude))
        parameter("lon", formatCoordinate(longitude))
    }

    private fun formatCoordinate(value: Double): String =
        dotDecimalFormatter.format(value)
}