package hr.dtakac.prognoza.shared.data.openmeteo.network

import hr.dtakac.prognoza.shared.platform.DotDecimalFormatter
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

internal class OpenMeteoForecastService(
    private val client: HttpClient,
    private val userAgent: String,
    private val baseUrl: String,
    private val dotDecimalFormatter: DotDecimalFormatter
) {
    suspend fun getForecast(
        latitude: Double,
        longitude: Double
    ): OpenMeteoResponse = client.get(urlString = "$baseUrl/forecast") {
        header(HttpHeaders.UserAgent, userAgent)
        parameter("latitude", dotDecimalFormatter.format(latitude, decimalPlaces = 2))
        parameter("longitude", dotDecimalFormatter.format(longitude, decimalPlaces = 2))
        parameter("timezone", "UTC")
        parameter("timeformat", "unixtime")

        listOf(
            "temperature_2m",
            "relativehumidity_2m",
            "apparent_temperature",
            "precipitation",
            "weathercode",
            "surface_pressure",
            "windspeed_10m",
            "winddirection_10m"
        ).forEach { parameter("hourly", it) }

        listOf(
            "sunrise",
            "sunset"
        ).forEach { parameter("daily", it) }
    }.body()
}