package hr.dtakac.prognoza.data.repository

import hr.dtakac.prognoza.data.database.forecast.ForecastDao
import hr.dtakac.prognoza.data.database.forecast.ForecastDbModel
import hr.dtakac.prognoza.data.database.forecast.toDbModel
import hr.dtakac.prognoza.data.database.forecast.toEntity
import hr.dtakac.prognoza.domain.forecast.ForecastSaver
import hr.dtakac.prognoza.domain.forecast.SavedForecastGetter
import hr.dtakac.prognoza.entities.forecast.ForecastDatum
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class ForecastRepository(
    private val forecastDao: ForecastDao,
    private val computationDispatcher: CoroutineDispatcher
) : ForecastSaver, SavedForecastGetter {
    override suspend fun get(
        latitude: Double,
        longitude: Double
    ): List<ForecastDatum> {
        val data = forecastDao.getAll(latitude, longitude)
        return withContext(computationDispatcher) {
            data.map(ForecastDbModel::toEntity)
        }
    }

    override suspend fun save(
        latitude: Double,
        longitude: Double,
        data: List<ForecastDatum>
    ) {
        forecastDao.delete(latitude, longitude)
        forecastDao.insert(data.map { it.toDbModel(latitude, longitude) })
    }
}