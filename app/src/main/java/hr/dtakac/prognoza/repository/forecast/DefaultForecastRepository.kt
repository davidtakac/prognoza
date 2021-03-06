package hr.dtakac.prognoza.repository.forecast

import android.database.sqlite.SQLiteException
import hr.dtakac.prognoza.USER_AGENT
import hr.dtakac.prognoza.api.ForecastService
import hr.dtakac.prognoza.apimodel.LocationForecastResponse
import hr.dtakac.prognoza.coroutines.DispatcherProvider
import hr.dtakac.prognoza.database.converter.ForecastMetaDateTimeConverter
import hr.dtakac.prognoza.database.dao.ForecastTimeSpanDao
import hr.dtakac.prognoza.entity.ForecastTimeSpan
import hr.dtakac.prognoza.entity.Place
import hr.dtakac.prognoza.utils.hasExpired
import hr.dtakac.prognoza.utils.toForecastResult
import hr.dtakac.prognoza.utils.toForecastTimeSpan
import hr.dtakac.prognoza.repomodel.*
import hr.dtakac.prognoza.repository.meta.MetaRepository
import kotlinx.coroutines.withContext
import okhttp3.Headers
import okhttp3.internal.format
import retrofit2.HttpException
import java.io.IOException
import java.time.ZonedDateTime

class DefaultForecastRepository(
    private val forecastService: ForecastService,
    private val forecastDao: ForecastTimeSpanDao,
    private val metaRepository: MetaRepository,
    private val dispatcherProvider: DispatcherProvider
) : ForecastRepository {

    override suspend fun deleteExpiredData() {
        try {
            forecastDao.deleteExpiredForecastTimeSpans()
        } catch (e: Exception) {
            // intentionally ignored
            e.printStackTrace()
        }
    }

    override suspend fun getForecastTimeSpans(
        start: ZonedDateTime,
        end: ZonedDateTime,
        place: Place
    ): ForecastResult {
        var meta = try {
            metaRepository.get(place.id)
        } catch (e: Exception) {
            null
        }
        var error: ForecastError? = null
        if (meta?.hasExpired() != false) {
            try {
                updateForecastDatabase(place, meta?.lastModified)
                meta = metaRepository.get(place.id)
            } catch (e: HttpException) {
                error = handleHttpException(e)
            } catch (e: SQLiteException) {
                error = DatabaseError(e)
            } catch (e: IOException) {
                error = IOError(e)
            } catch (e: Exception) {
                error = UnknownError(e)
            }
        }
        return try {
            val hours = forecastDao.getForecastTimeSpans(start, end, place.id)
            hours.toForecastResult(meta, error)
        } catch (e: SQLiteException) {
            Empty(DatabaseError(e))
        } catch (e: Exception) {
            Empty(UnknownError(e))
        }
    }

    private fun handleHttpException(httpException: HttpException): ForecastError {
        return when (httpException.code()) {
            429 -> ThrottlingError(httpException)
            in 400..499 -> ClientError(httpException)
            in 500..504 -> ServerError(httpException)
            else -> UnknownError(httpException)
        }
    }

    private suspend fun updateForecastDatabase(place: Place, lastModified: ZonedDateTime?) {
        val lastModifiedTimestamp = ForecastMetaDateTimeConverter.toTimestamp(lastModified)
        val forecastResponse = forecastService.getCompleteLocationForecast(
            userAgent = USER_AGENT,
            ifModifiedSince = lastModifiedTimestamp,
            latitude = format("%.2f", place.latitude),
            longitude = format("%.2f", place.longitude)
        )
        updateForecastTimeSpans(forecastResponse.body(), place.id)
        updateForecastMeta(forecastResponse.headers(), place.id)
    }

    private suspend fun updateForecastMeta(forecastResponseHeaders: Headers, placeId: String) {
        metaRepository.update(
            placeId,
            expiresTime = forecastResponseHeaders["Expires"],
            lastModifiedTime = forecastResponseHeaders["Last-Modified"],
        )
    }

    private suspend fun updateForecastTimeSpans(
        locationForecastResponse: LocationForecastResponse?,
        placeId: String
    ) {
        val forecastTimeSpans = locationForecastResponse?.forecast?.forecastTimeSteps?.let {
            withContext(dispatcherProvider.default) {
                val result = mutableListOf<ForecastTimeSpan>()
                for (i in it.indices) {
                    val current = it[i]
                    val next = it.getOrNull(i + 1)
                    result.add(current.toForecastTimeSpan(placeId, next))
                }
                result
            }
        } ?: return
        forecastDao.insertOrUpdateAll(forecastTimeSpans)
    }
}