package hr.dtakac.prognoza.shared.data.openmeteo

import hr.dtakac.prognoza.shared.data.openmeteo.network.OpenMeteoForecastService
import hr.dtakac.prognoza.shared.data.openmeteo.network.OpenMeteoResponse
import hr.dtakac.prognoza.shared.data.openmeteo.network.mapToEntities
import hr.dtakac.prognoza.shared.domain.data.ForecastProvider
import hr.dtakac.prognoza.shared.domain.data.ForecastProviderResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json
import kotlin.time.Duration

internal class OpenMeteoForecastProvider(
    private val apiService: OpenMeteoForecastService,
    private val cachedResponseQueries: CachedOpenMeteoResponseQueries,
    private val metaQueries: OpenMeteoMetaQueries,
    private val computationDispatcher: CoroutineDispatcher,
    private val ioDispatcher: CoroutineDispatcher
) : ForecastProvider {
    override suspend fun provide(
        latitude: Double,
        longitude: Double
    ): ForecastProviderResult {
        val lastUpdated = getLastUpdatedEpochMillis(latitude, longitude)
        if (Clock.System.now().toEpochMilliseconds() - lastUpdated >= updateThresh) {
            updateDatabase(latitude, longitude)
        }

        val cachedResponse = getCachedResponse(latitude, longitude)
        return if (cachedResponse == null) {
            ForecastProviderResult.Error
        } else {
            try {
                ForecastProviderResult.Success(
                    data = withContext(computationDispatcher) {
                        cachedResponse.mapToEntities()
                    }
                )
            } catch (_: Exception) {
                ForecastProviderResult.Error
            }
        }
    }

    private fun getLastUpdatedEpochMillis(
        latitude: Double,
        longitude: Double
    ): Long = metaQueries
        .get(latitude, longitude)
        .executeAsOneOrNull()
        ?.lastUpdatedEpochMillis ?: 0L

    private suspend fun updateDatabase(
        latitude: Double,
        longitude: Double
    ) {
        try {
            val freshResponse = apiService.getForecast(latitude, longitude)
            withContext(ioDispatcher) {
                cachedResponseQueries.insert(
                    CachedOpenMeteoResponse(
                        latitude = latitude,
                        longitude = longitude,
                        responseJson = Json.encodeToString(OpenMeteoResponse.serializer(), freshResponse)
                    )
                )
                metaQueries.insert(
                    OpenMeteoMeta(
                        latitude = latitude,
                        longitude = longitude,
                        lastUpdatedEpochMillis = Clock.System.now().toEpochMilliseconds()
                    )
                )
            }
        } catch (_: Exception) {}
    }

    private suspend fun getCachedResponse(
        latitude: Double,
        longitude: Double
    ): OpenMeteoResponse? = withContext(ioDispatcher) {
        cachedResponseQueries
            .get(latitude = latitude, longitude = longitude)
            .executeAsOneOrNull()
            ?.let {
                Json.decodeFromString(
                    deserializer = OpenMeteoResponse.serializer(),
                    it.responseJson
                )
            }
    }

    companion object {
        private val updateThresh = Duration.parse("PT30M").inWholeMilliseconds
    }
}