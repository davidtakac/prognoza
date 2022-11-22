package hr.dtakac.prognoza.shared.data.metnorway

import hr.dtakac.prognoza.shared.data.metnorway.network.LocationForecastResponse
import hr.dtakac.prognoza.shared.data.metnorway.network.MetNorwayForecastService
import hr.dtakac.prognoza.shared.data.metnorway.network.mapAdjacentTimeStepsToEntity
import hr.dtakac.prognoza.shared.domain.data.ForecastProvider
import hr.dtakac.prognoza.shared.domain.data.ForecastProviderResult
import hr.dtakac.prognoza.shared.entity.ForecastDatum
import io.github.aakira.napier.Napier
import io.ktor.client.call.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json

private val TAG = MetNorwayForecastProvider::class.simpleName ?: ""
internal class MetNorwayForecastProvider(
    private val apiService: MetNorwayForecastService,
    private val metaQueries: MetaQueries,
    private val cachedResponseQueries: CachedResponseQueries,
    private val ioDispatcher: CoroutineDispatcher,
    private val rfc1123ToEpochMillis: (String) -> Long
) : ForecastProvider {
    override suspend fun provide(
        latitude: Double,
        longitude: Double
    ): ForecastProviderResult {
        val meta = getMeta(latitude, longitude)
        if (meta?.expiresEpochMillis?.let { Clock.System.now().toEpochMilliseconds() > it } != false) {
            try {
                updateDatabase(
                    latitude = latitude,
                    longitude = longitude,
                    lastModifiedEpochMillis = meta?.lastModifiedEpochMillis
                )
            } catch (e: Exception) {
                Napier.e(TAG, e)
            }
        }

        return try {
            val response = getCachedResponse(latitude, longitude)
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
            Napier.e(TAG, e)
            ForecastProviderResult.Error
        }
    }

    private suspend fun updateDatabase(
        latitude: Double,
        longitude: Double,
        lastModifiedEpochMillis: Long?
    ) {
        val forecastResponse = apiService.getForecast(
            ifModifiedSinceEpochMillis = lastModifiedEpochMillis,
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
                cachedResponseQueries.insert(
                    CachedResponse(
                        latitude = latitude,
                        longitude = longitude,
                        responseJson = Json.encodeToString(LocationForecastResponse.serializer(), response)
                    )
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
            metaQueries.insert(
                Meta(
                    latitude = latitude,
                    longitude = longitude,
                    expiresEpochMillis = headers[HttpHeaders.Expires]?.let { rfc1123ToEpochMillis(it) },
                    lastModifiedEpochMillis = headers[HttpHeaders.LastModified]?.let { rfc1123ToEpochMillis(it) }
                )
            )
        }
    }

    private suspend fun getMeta(
        latitude: Double,
        longitude: Double
    ): Meta? = withContext(ioDispatcher) {
        metaQueries
            .get(latitude, longitude)
            .executeAsOneOrNull()
    }

    private suspend fun getCachedResponse(
        latitude: Double,
        longitude: Double
    ): LocationForecastResponse? = withContext(ioDispatcher) {
        cachedResponseQueries
            .get(latitude, longitude)
            .executeAsOneOrNull()
            ?.responseJson
            ?.let {
                Json.decodeFromString(LocationForecastResponse.serializer(), it)
            }
    }
}