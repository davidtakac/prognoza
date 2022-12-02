package hr.dtakac.prognoza.shared.data.prognoza

import hr.dtakac.prognoza.shared.domain.data.SavedForecastGetterResult
import hr.dtakac.prognoza.shared.domain.data.ForecastSaver
import hr.dtakac.prognoza.shared.domain.data.SavedForecastGetter
import hr.dtakac.prognoza.shared.entity.ForecastDatum
import hr.dtakac.prognoza.shared.entity.Wind
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock

internal class DatabaseForecastRepository(
    private val forecastQueries: ForecastQueries,
    private val metaQueries: PrognozaMetaQueries,
    private val computationDispatcher: CoroutineDispatcher,
    private val ioDispatcher: CoroutineDispatcher
) : ForecastSaver, SavedForecastGetter {
    override suspend fun get(
        latitude: Double,
        longitude: Double
    ): SavedForecastGetterResult {
        val data = withContext(ioDispatcher) {
            forecastQueries
                .get(latitude, longitude)
                .executeAsList()
        }
        return if (data.isEmpty()) {
            SavedForecastGetterResult.Empty
        } else withContext(computationDispatcher) {
            SavedForecastGetterResult.Success(
                data = data.map {
                    ForecastDatum(
                        startEpochMillis = it.startEpochMillis,
                        endEpochMillis = it.endEpochMillis,
                        temperature = it.temperature,
                        precipitation = it.precipitation,
                        wind = Wind(speed = it.windSpeed, fromDirection = it.windFromDirection),
                        airPressure = it.airPressureAtSeaLevel,
                        description = it.description,
                        mood = it.mood,
                        humidity = it.humidity
                    )
                },
                // Null is possible when migrating from 3.0.0
                lastUpdatedEpochMillis = metaQueries.get(
                    latitude = latitude,
                    longitude = longitude
                ).executeAsOneOrNull()?.lastUpdatedEpochMillis ?: 0L
            )
        }
    }

    override suspend fun save(
        latitude: Double,
        longitude: Double,
        data: List<ForecastDatum>
    ) {
        withContext(ioDispatcher) {
            forecastQueries.delete(latitude, longitude)
            data.forEach {
                val dbModel = Forecast(
                    startEpochMillis = it.startEpochMillis,
                    endEpochMillis = it.endEpochMillis,
                    latitude = latitude,
                    longitude = longitude,
                    temperature = it.temperature,
                    description = it.description,
                    mood = it.mood,
                    precipitation = it.precipitation,
                    windSpeed = it.wind.speed,
                    windFromDirection = it.wind.fromDirection,
                    humidity = it.humidity,
                    airPressureAtSeaLevel = it.airPressure
                )
                forecastQueries.insert(dbModel)
            }
            metaQueries.insert(
                PrognozaMeta(
                    latitude = latitude,
                    longitude = longitude,
                    lastUpdatedEpochMillis = Clock.System.now().toEpochMilliseconds()
                )
            )
        }
    }
}