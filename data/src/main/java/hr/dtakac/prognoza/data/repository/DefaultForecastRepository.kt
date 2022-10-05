package hr.dtakac.prognoza.data.repository

import android.database.sqlite.SQLiteException
import hr.dtakac.prognoza.data.database.converter.Rfc1123DateTimeConverter
import hr.dtakac.prognoza.data.database.forecast.dao.ForecastDao
import hr.dtakac.prognoza.data.database.forecast.dao.MetaDao
import hr.dtakac.prognoza.data.database.forecast.model.ForecastDbModel
import hr.dtakac.prognoza.data.database.forecast.model.MetaDbModel
import hr.dtakac.prognoza.data.mapping.mapDbModelToEntity
import hr.dtakac.prognoza.data.mapping.mapResponseToDbModel
import hr.dtakac.prognoza.data.network.forecast.ForecastService
import hr.dtakac.prognoza.data.network.forecast.LocationForecastResponse
import hr.dtakac.prognoza.domain.repository.ForecastRepository
import hr.dtakac.prognoza.domain.repository.ForecastRepositoryResult
import hr.dtakac.prognoza.entities.forecast.Forecast
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okhttp3.Headers
import okhttp3.internal.format
import retrofit2.HttpException
import java.time.ZonedDateTime

class DefaultForecastRepository(
    private val forecastService: ForecastService,
    private val forecastDao: ForecastDao,
    private val metaDao: MetaDao,
    private val userAgent: String,
    private val computationDispatcher: CoroutineDispatcher
) : ForecastRepository {
    override suspend fun getForecast(
        latitude: Double,
        longitude: Double,
        from: ZonedDateTime,
        to: ZonedDateTime
    ): ForecastRepositoryResult {
        val meta = try {
            metaDao.get(latitude, longitude)
        } catch (e: Exception) {
            null
        }

        if (meta?.expires?.let { ZonedDateTime.now() > it } != false) {
            try {
                updateDatabase(
                    latitude = latitude,
                    longitude = longitude,
                    lastModified = meta?.lastModified
                )
            } catch (e: HttpException) {
                return getResultFromHttpException(e)
            } catch (e: SQLiteException) {
                return ForecastRepositoryResult.DatabaseError
            } catch (e: Exception) {
                return ForecastRepositoryResult.UnknownError
            }
        }
        return try {
            forecastDao.getForecasts(
                start = from,
                end = to,
                latitude = latitude,
                longitude = longitude
            ).map(::mapDbModelToEntity).let { ForecastRepositoryResult.Success(Forecast(it)) }
        } catch (e: Exception) {
            ForecastRepositoryResult.UnknownError
        }
    }

    private suspend fun updateDatabase(
        latitude: Double,
        longitude: Double,
        lastModified: ZonedDateTime?
    ) {
        val lastModifiedTimestamp = Rfc1123DateTimeConverter.toTimestamp(lastModified)
        val forecastResponse = forecastService.getCompactLocationForecast(
            userAgent = userAgent,
            ifModifiedSince = lastModifiedTimestamp,
            latitude = format("%.2f", latitude),
            longitude = format("%.2f", longitude)
        )
        forecastDao.deleteExpired()
        updateForecast(forecastResponse.body(), latitude, longitude)
        updateMeta(forecastResponse.headers(), latitude, longitude)
    }

    private suspend fun updateMeta(
        headers: Headers,
        latitude: Double,
        longitude: Double
    ) {
        val dbModel = MetaDbModel(
            latitude = latitude,
            longitude = longitude,
            expires = Rfc1123DateTimeConverter.fromTimestamp(headers["Expires"]),
            lastModified = Rfc1123DateTimeConverter.fromTimestamp(headers["Last-Modified"])
        )
        metaDao.update(dbModel)
    }

    private suspend fun updateForecast(
        locationForecastResponse: LocationForecastResponse?,
        latitude: Double,
        longitude: Double
    ) {
        val result = mutableListOf<ForecastDbModel>()
        val dbModels = withContext(computationDispatcher) {
            locationForecastResponse?.forecast?.forecastTimeSteps?.let { timeSteps ->
                for (i in timeSteps.indices) {
                    mapResponseToDbModel(
                        current = timeSteps[i],
                        next = timeSteps.getOrNull(i + 1),
                        latitude = latitude,
                        longitude = longitude
                    )?.let { result.add(it) }
                }
                result
            }
        } ?: return
        forecastDao.delete(latitude, longitude)
        forecastDao.insert(dbModels)
    }

    private fun getResultFromHttpException(httpException: HttpException): ForecastRepositoryResult {
        return when (httpException.code()) {
            429 -> ForecastRepositoryResult.ThrottleError
            in 400..499 -> ForecastRepositoryResult.ClientError
            in 500..504 -> ForecastRepositoryResult.ServerError
            else -> ForecastRepositoryResult.UnknownError
        }
    }
}