package hr.dtakac.prognoza.repository.forecast

import android.database.sqlite.SQLiteException
import hr.dtakac.prognoza.USER_AGENT
import hr.dtakac.prognoza.api.ForecastService
import hr.dtakac.prognoza.apimodel.LocationForecastResponse
import hr.dtakac.prognoza.coroutines.DispatcherProvider
import hr.dtakac.prognoza.database.converter.ForecastMetaDateTimeConverter
import hr.dtakac.prognoza.database.dao.ForecastTimeSpanDao
import hr.dtakac.prognoza.dbmodel.ForecastTimeSpan
import hr.dtakac.prognoza.extensions.atStartOfDay
import hr.dtakac.prognoza.extensions.hasExpired
import hr.dtakac.prognoza.extensions.toForecastResult
import hr.dtakac.prognoza.extensions.toForecastTimeSpan
import hr.dtakac.prognoza.repomodel.*
import hr.dtakac.prognoza.repository.meta.MetaRepository
import hr.dtakac.prognoza.repository.place.PlaceRepository
import kotlinx.coroutines.withContext
import okhttp3.Headers
import okhttp3.internal.format
import retrofit2.HttpException
import java.io.IOException
import java.time.ZonedDateTime

class DefaultForecastRepository(
    private val forecastService: ForecastService,
    private val forecastDao: ForecastTimeSpanDao,
    private val placeRepository: PlaceRepository,
    private val metaRepository: MetaRepository,
    private val dispatcherProvider: DispatcherProvider
) : ForecastRepository {
    private val hoursAfterMidnightToShow = 6L

    override suspend fun getTodayForecastTimeSpans(placeId: String): ForecastResult {
        val anHourAgo = ZonedDateTime
            .now()
            .minusHours(1) // to get the current hour as well
        val hoursLeftInTheDay = 24 - anHourAgo.hour
        val hoursToShow = hoursLeftInTheDay + hoursAfterMidnightToShow
        return getForecastTimeSpans(
            start = anHourAgo,
            end = anHourAgo.plusHours(hoursToShow),
            placeId
        )
    }

    override suspend fun getTomorrowForecastTimeSpans(placeId: String): ForecastResult {
        val tomorrow = ZonedDateTime
            .now()
            .atStartOfDay()
            .plusDays(1)
        return getForecastTimeSpans(
            start = tomorrow.plusHours(hoursAfterMidnightToShow + 1L /* start where today left off */),
            end = tomorrow.plusDays(1).plusHours(hoursAfterMidnightToShow),
            placeId
        )
    }

    override suspend fun getOtherDaysForecastTimeSpans(placeId: String): ForecastResult {
        val now = ZonedDateTime.now().atStartOfDay()
        return getForecastTimeSpans(
            start = now,
            end = now.plusDays(7L),
            placeId
        )
    }

    override suspend fun deleteExpiredData() {
        try {
            forecastDao.deleteExpiredForecastTimeSpans()
        } catch (e: Exception) {
            // intentionally ignored
            e.printStackTrace()
        }
    }

    private suspend fun getForecastTimeSpans(
        start: ZonedDateTime,
        end: ZonedDateTime,
        placeId: String
    ): ForecastResult {
        var meta = try {
            metaRepository.get(placeId)
        } catch (e: Exception) {
            null
        }
        var error: ForecastError? = null
        if (meta?.hasExpired() != false) {
            try {
                updateForecastDatabase(placeId, meta?.lastModified)
                meta = metaRepository.get(placeId)
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
            val hours = forecastDao.getForecastTimeSpans(start, end, placeId)
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

    private suspend fun updateForecastDatabase(placeId: String, lastModified: ZonedDateTime?) {
        val forecastPlace = placeRepository.get(placeId) ?: placeRepository.getDefaultPlace()
        val lastModifiedTimestamp = ForecastMetaDateTimeConverter.toTimestamp(lastModified)
        val forecastResponse = forecastService.getCompleteLocationForecast(
            userAgent = USER_AGENT,
            ifModifiedSince = lastModifiedTimestamp,
            latitude = format("%.2f", forecastPlace.latitude),
            longitude = format("%.2f", forecastPlace.longitude)
        )
        updateForecastTimeSpans(forecastResponse.body(), forecastPlace.id)
        updateForecastMeta(forecastResponse.headers(), forecastPlace.id)
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