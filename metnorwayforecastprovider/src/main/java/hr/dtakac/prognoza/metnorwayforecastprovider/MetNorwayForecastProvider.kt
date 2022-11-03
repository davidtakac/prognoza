package hr.dtakac.prognoza.metnorwayforecastprovider

import hr.dtakac.prognoza.MetNorwayDatabase
import hr.dtakac.prognoza.domain.forecast.ForecastProvider
import hr.dtakac.prognoza.domain.forecast.ForecastProviderResult
import hr.dtakac.prognoza.entities.forecast.ForecastDatum
import io.github.aakira.napier.Napier
import io.ktor.client.call.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class MetNorwayForecastProvider(
    private val apiService: MetNorwayForecastService,
    private val database: MetNorwayDatabase,
    private val ioDispatcher: CoroutineDispatcher
) : ForecastProvider {
    override suspend fun provide(
        latitude: Double,
        longitude: Double
    ): ForecastProviderResult {
        val meta = getMeta(latitude, longitude)
        if (meta?.expires?.let { ZonedDateTime.now() > fromRfc1123(meta.expires) } != false) {
            try {
                updateDatabase(
                    latitude = latitude,
                    longitude = longitude,
                    lastModified = meta?.lastModified?.let(::fromRfc1123)
                )
            } catch (e: Exception) {
                Napier.e(message = "MET Norway error", e)
            }
        }

        return try {
            val response = getForecastResponse(latitude, longitude)
            if (response == null) {
                ForecastProviderResult.Error
            } else {
                val timeSteps = response.forecast.forecastTimeSteps
                val data = mutableListOf<ForecastDatum>()
                for (i in timeSteps.indices) {
                    val datum = mapAdjacentTimeStepsToEntity(
                        current = timeSteps[i],
                        next = timeSteps.getOrNull(i + 1)
                    )
                    datum?.let(data::add)
                }
                ForecastProviderResult.Success(data)
            }
        } catch (e: Exception) {
            Napier.e(message = "MET Norway error", e)
            ForecastProviderResult.Error
        }
    }

    private suspend fun updateDatabase(
        latitude: Double,
        longitude: Double,
        lastModified: ZonedDateTime?
    ) {
        val forecastResponse = apiService.getForecast(
            ifModifiedSince = lastModified,
            latitude = latitude,
            longitude = longitude
        )
        updateForecast(forecastResponse.body(), latitude, longitude)
        updateMeta(forecastResponse.headers, latitude, longitude)
    }

    private suspend fun updateForecast(
        response: LocationForecastResponse?,
        latitude: Double,
        longitude: Double
    ) {
        response?.let {
            withContext(ioDispatcher) {
                database.forecastResponseDbModelQueries.insert(
                    latitude = latitude,
                    longitude = longitude,
                    json = Json.encodeToString(LocationForecastResponse.serializer(), response)
                )
            }
        }
    }

    private suspend fun updateMeta(
        headers: Headers,
        latitude: Double,
        longitude: Double
    ) {
        withContext(ioDispatcher) {
            database.forecastMetaDbModelQueries.insert(
                latitude = latitude,
                longitude = longitude,
                expires = headers[HttpHeaders.Expires],
                lastModified = headers[HttpHeaders.LastModified]
            )
        }
    }

    private suspend fun getMeta(
        latitude: Double,
        longitude: Double
    ): ForecastMetaDbModel? = withContext(ioDispatcher) {
        database.forecastMetaDbModelQueries
            .get(latitude, longitude)
            .executeAsOneOrNull()
    }

    private suspend fun getForecastResponse(
        latitude: Double,
        longitude: Double
    ): LocationForecastResponse? = withContext(ioDispatcher) {
        database.forecastResponseDbModelQueries
            .get(latitude, longitude)
            .executeAsOneOrNull()
            ?.let { Json.decodeFromString(LocationForecastResponse.serializer(), it.json) }
    }

    private fun fromRfc1123(rfc1123ZonedDateTime: String): ZonedDateTime =
        ZonedDateTime.parse(rfc1123ZonedDateTime, DateTimeFormatter.RFC_1123_DATE_TIME)
}