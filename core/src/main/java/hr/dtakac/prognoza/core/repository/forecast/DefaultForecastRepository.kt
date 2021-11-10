package hr.dtakac.prognoza.core.repository.forecast

import android.database.sqlite.SQLiteException
import hr.dtakac.prognoza.core.api.ForecastService
import hr.dtakac.prognoza.core.coroutines.DispatcherProvider
import hr.dtakac.prognoza.core.database.converter.ForecastMetaDateTimeConverter
import hr.dtakac.prognoza.core.database.dao.ForecastTimeSpanDao
import hr.dtakac.prognoza.core.model.api.LocationForecastResponse
import hr.dtakac.prognoza.core.model.database.ForecastMeta
import hr.dtakac.prognoza.core.model.database.ForecastTimeSpan
import hr.dtakac.prognoza.core.model.database.Place
import hr.dtakac.prognoza.core.model.repository.*
import hr.dtakac.prognoza.core.repository.meta.MetaRepository
import hr.dtakac.prognoza.core.utils.USER_AGENT
import hr.dtakac.prognoza.core.utils.hasExpired
import hr.dtakac.prognoza.core.utils.toForecastResult
import hr.dtakac.prognoza.core.utils.toForecastTimeSpan
import kotlinx.coroutines.withContext
import okhttp3.Headers
import okhttp3.internal.format
import retrofit2.HttpException
import java.io.IOException
import java.lang.IllegalStateException
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
        place: Place,
        oldMeta: ForecastMeta?
    ): ForecastResult {
        var error: ForecastError? = null
        if (oldMeta.hasExpired()) {
            if (oldMeta != null && oldMeta.placeId != place.id) {
                // TODO: remove possibility of this happening by removing place and instead fetching the place here.
                throw IllegalStateException("Meta place ID doesn't match place ID.")
            }
            try {
                updateForecastDatabase(place, oldMeta?.lastModified)
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
            hours.toForecastResult(error)
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