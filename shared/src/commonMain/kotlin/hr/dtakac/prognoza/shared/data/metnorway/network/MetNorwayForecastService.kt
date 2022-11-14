package hr.dtakac.prognoza.shared.data.metnorway.network

import hr.dtakac.prognoza.shared.formatToDotDecimal
import hr.dtakac.prognoza.shared.formatToRfc1123
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class MetNorwayForecastService(
    private val client: HttpClient,
    private val userAgent: String,
    private val baseUrl: String
) {
    suspend fun getForecast(
        ifModifiedSinceEpochMillis: Long?,
        latitude: Double,
        longitude: Double
    ): HttpResponse = client.get(urlString = "$baseUrl/locationforecast/2.0/compact") {
        header(HttpHeaders.UserAgent, userAgent)
        ifModifiedSinceEpochMillis?.let {
            header(HttpHeaders.IfModifiedSince, formatToRfc1123(it))
        }
        parameter("lat", formatCoordinate(latitude))
        parameter("lon", formatCoordinate(longitude))
    }

    private fun formatCoordinate(value: Double): String = formatToDotDecimal(
        value = value,
        decimalPlaces = 2
    )
}