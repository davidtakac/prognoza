package hr.dtakac.prognoza.shared.data.metnorway.network

import hr.dtakac.prognoza.shared.platform.DotDecimalFormatter
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

internal class MetNorwayForecastService(
    private val client: HttpClient,
    private val userAgent: String,
    private val baseUrl: String,
    private val dotDecimalFormatter: DotDecimalFormatter,
    private val epochMillisToRfc1123: (Long) -> String
) {
    suspend fun getForecast(
        ifModifiedSinceEpochMillis: Long?,
        latitude: Double,
        longitude: Double
    ): HttpResponse = client.get(urlString = "$baseUrl/locationforecast/2.0/compact") {
        header(HttpHeaders.UserAgent, userAgent)
        ifModifiedSinceEpochMillis?.let {
            header(HttpHeaders.IfModifiedSince, epochMillisToRfc1123(it))
        }
        parameter("lat", formatCoordinate(latitude))
        parameter("lon", formatCoordinate(longitude))
    }

    private fun formatCoordinate(value: Double): String = dotDecimalFormatter.format(
        value = value,
        decimalPlaces = 2
    )
}