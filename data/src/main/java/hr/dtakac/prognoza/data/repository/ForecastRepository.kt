package hr.dtakac.prognoza.data.repository

import hr.dtakac.prognoza.data.Forecast
import hr.dtakac.prognoza.data.ForecastQueries
import hr.dtakac.prognoza.domain.forecast.ForecastSaver
import hr.dtakac.prognoza.domain.forecast.SavedForecastGetter
import hr.dtakac.prognoza.entities.forecast.ForecastDatum
import hr.dtakac.prognoza.entities.forecast.Wind
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class ForecastRepository(
    private val forecastQueries: ForecastQueries,
    private val computationDispatcher: CoroutineDispatcher,
    private val ioDispatcher: CoroutineDispatcher
) : ForecastSaver, SavedForecastGetter {
    override suspend fun get(
        latitude: Double,
        longitude: Double
    ): List<ForecastDatum> {
        val data = withContext(ioDispatcher) {
            forecastQueries
                .get(latitude, longitude)
                .executeAsList()
        }
        return withContext(computationDispatcher) {
            data.map {
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
            }
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
        }
    }
}