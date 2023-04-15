package hr.dtakac.prognoza.shared.data.prognoza

import hr.dtakac.prognoza.shared.domain.data.ForecastProvider
import hr.dtakac.prognoza.shared.domain.data.ForecastProviderResult
import hr.dtakac.prognoza.shared.domain.data.ForecastRepository
import hr.dtakac.prognoza.shared.domain.data.ForecastRepositoryResult
import hr.dtakac.prognoza.shared.entity.Hour
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlin.time.Duration

internal class DatabaseForecastRepository(
    private val forecastQueries: ForecastQueries,
    private val metaQueries: PrognozaMetaQueries,
    private val openMeteoProvider: ForecastProvider,
    private val metNorwayProvider: ForecastProvider,
    private val computationDispatcher: CoroutineDispatcher,
    private val ioDispatcher: CoroutineDispatcher
) : ForecastRepository {
    override suspend fun get(
        latitude: Double,
        longitude: Double,
        from: hr.dtakac.prognoza.shared.entity.ForecastProvider,
        refreshIfOlderThan: Duration
    ): ForecastRepositoryResult {
        if (shouldPullFromProvider(refreshIfOlderThan, latitude, longitude)) {
            pullFromProvider(latitude, longitude, from)
        }
        val data = withContext(ioDispatcher) {
            forecastQueries
                .get(latitude, longitude)
                .executeAsList()
        }
        val provider = withContext(ioDispatcher) {
            metaQueries
                .get(latitude, longitude)
                .executeAsOneOrNull()
                ?.provider
                ?: hr.dtakac.prognoza.shared.entity.ForecastProvider.MET_NORWAY
        }

        return if (data.isEmpty()) {
            ForecastRepositoryResult.Empty
        } else withContext(computationDispatcher) {
            ForecastRepositoryResult.Success(
                data = data.map {
                    Hour(
                        unixSecond = it.startEpochMillis,
                        endEpochMillis = it.endEpochMillis,
                        temperature = it.temperature,
                        precipitation = it.precipitation,
                        wind = Wind(speed = it.windSpeed, direction = it.windFromDirection),
                        pressureAtSeaLevel = it.airPressureAtSeaLevel,
                        description = it.description,
                        mood = it.mood,
                        relativeHumidity = it.humidity
                    )
                },
                provider = provider
            )
        }
    }

    override suspend fun remove(latitude: Double, longitude: Double) {
        forecastQueries.delete(latitude = latitude, longitude = longitude)
        metaQueries.delete(latitude = latitude, longitude = longitude)
    }

    private suspend fun shouldPullFromProvider(
        refreshIfOlderThan: Duration,
        latitude: Double,
        longitude: Double
    ): Boolean {
        return if (refreshIfOlderThan > Duration.ZERO) {
            val lastUpdated = withContext(ioDispatcher) {
                metaQueries
                    .get(latitude, longitude)
                    .executeAsOneOrNull()
                    ?.lastUpdatedEpochMillis ?: 0L
            }
            Clock.System.now().toEpochMilliseconds() - lastUpdated >= refreshIfOlderThan.inWholeMilliseconds
        } else true
    }

    private suspend fun pullFromProvider(
        latitude: Double,
        longitude: Double,
        from: hr.dtakac.prognoza.shared.entity.ForecastProvider
    ) {
        val provider = when (from) {
            hr.dtakac.prognoza.shared.entity.ForecastProvider.OPEN_METEO -> openMeteoProvider
            hr.dtakac.prognoza.shared.entity.ForecastProvider.MET_NORWAY -> metNorwayProvider
        }
        val data = (provider.provide(latitude, longitude) as? ForecastProviderResult.Success)
            ?.data
            ?.takeIf { it.isNotEmpty() }
            ?: return

        withContext(ioDispatcher) {
            remove(latitude, longitude)
            data.forEach {
                val dbModel = Forecast(
                    startEpochMillis = it.unixSecond,
                    endEpochMillis = it.endEpochMillis,
                    latitude = latitude,
                    longitude = longitude,
                    temperature = it.temperature,
                    description = it.description,
                    mood = it.mood,
                    precipitation = it.precipitation,
                    windSpeed = it.wind.speed,
                    windFromDirection = it.wind.direction,
                    humidity = it.relativeHumidity,
                    airPressureAtSeaLevel = it.pressureAtSeaLevel
                )
                forecastQueries.insert(dbModel)
            }
            metaQueries.insert(
                PrognozaMeta(
                    latitude = latitude,
                    longitude = longitude,
                    lastUpdatedEpochMillis = Clock.System.now().toEpochMilliseconds(),
                    provider = from
                )
            )
        }
    }
}