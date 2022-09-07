package hr.dtakac.prognoza.data.repository

import android.util.Log
import hr.dtakac.prognoza.data.database.converter.Rfc1123DateTimeConverter
import hr.dtakac.prognoza.data.database.forecast.dao.ForecastDao
import hr.dtakac.prognoza.data.database.forecast.dao.MetaDao
import hr.dtakac.prognoza.data.database.forecast.model.ForecastDbModel
import hr.dtakac.prognoza.data.database.forecast.model.MetaDbModel
import hr.dtakac.prognoza.data.mapping.mapDbModelToEntity
import hr.dtakac.prognoza.data.mapping.mapResponseToDbModel
import hr.dtakac.prognoza.data.network.forecast.ForecastService
import hr.dtakac.prognoza.data.network.forecast.LocationForecastResponse
import hr.dtakac.prognoza.domain.coroutines.DispatcherProvider
import hr.dtakac.prognoza.domain.repository.ForecastRepository
import hr.dtakac.prognoza.entities.forecast.Forecast
import kotlinx.coroutines.withContext
import okhttp3.Headers
import okhttp3.internal.format
import java.time.ZonedDateTime

class DefaultForecastRepository(
    private val forecastService: ForecastService,
    private val forecastDao: ForecastDao,
    private val metaDao: MetaDao,
    private val userAgent: String,
    private val dispatchers: DispatcherProvider
) : ForecastRepository {
    override suspend fun getForecast(
        latitude: Double,
        longitude: Double,
        from: ZonedDateTime,
        to: ZonedDateTime
    ): List<Forecast> {
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
            } catch (e: Exception) {
                Log.e(this::class.qualifiedName, e.stackTraceToString())
            }
        }
        return try {
            forecastDao.getForecasts(
                start = from,
                end = to,
                latitude = latitude,
                longitude = longitude
            ).map(::mapDbModelToEntity)
        } catch (e: Exception) {
            Log.e(this::class.qualifiedName, e.stackTraceToString())
            listOf()
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
        forecastDao.deleteExpiredForecastTimeSpans()
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
        metaDao.updateForecastMeta(dbModel)
    }

    private suspend fun updateForecast(
        locationForecastResponse: LocationForecastResponse?,
        latitude: Double,
        longitude: Double
    ) {
        val result = mutableListOf<ForecastDbModel>()
        val dbModels = withContext(dispatchers.compute) {
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
        forecastDao.insertOrUpdateAll(dbModels)
    }
}