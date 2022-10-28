package hr.dtakac.prognoza.metnorwayforecastprovider

import hr.dtakac.prognoza.domain.forecast.ForecastProvider
import hr.dtakac.prognoza.domain.forecast.ForecastProviderResult
import hr.dtakac.prognoza.entities.forecast.ForecastDatum
import hr.dtakac.prognoza.metnorwayforecastprovider.database.*
import hr.dtakac.prognoza.metnorwayforecastprovider.database.converter.Rfc1123DateTimeConverter
import okhttp3.Headers
import java.time.ZonedDateTime
import java.util.*

class MetNorwayForecastProvider(
    private val userAgent: String,
    private val forecastService: ForecastService,
    private val metaDao: ForecastMetaDao,
    private val forecastResponseDao: ForecastResponseDao
) : ForecastProvider {
    override suspend fun provide(
        latitude: Double,
        longitude: Double
    ): ForecastProviderResult {
        val meta = metaDao.get(latitude, longitude)
        if (meta?.expires?.let { ZonedDateTime.now() > it } != false) {
            try {
                updateDatabase(
                    latitude = latitude,
                    longitude = longitude,
                    lastModified = meta?.lastModified
                )
            } catch (e: Exception) {
                // todo: log
            }
        }

        return try {
            val dbModel = forecastResponseDao.get(
                latitude = latitude,
                longitude = longitude
            )

            if (dbModel == null) {
                ForecastProviderResult.Error
            } else {
                val timeSteps = dbModel.response.forecast.forecastTimeSteps
                val data = mutableListOf<ForecastDatum>()
                for (i in timeSteps.indices) {
                    val datum = mapAdjacentTimeStepsToEntity(
                        current = timeSteps[i],
                        next = timeSteps.getOrNull(i + 1)
                    )
                    datum?.let(data::add)
                }
                ForecastProviderResult.Success(data)
            }
        } catch (e: Exception) {
            // todo: log
            ForecastProviderResult.Error
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
            latitude = String.format(Locale.ROOT,"%.2f", latitude),
            longitude = String.format(Locale.ROOT,"%.2f", longitude)
        )
        updateForecast(forecastResponse.body(), latitude, longitude)
        updateMeta(forecastResponse.headers(), latitude, longitude)
    }

    private suspend fun updateForecast(
        response: LocationForecastResponse?,
        latitude: Double,
        longitude: Double
    ) {
        response?.let {
            forecastResponseDao.insert(
                ForecastResponseDbModel(
                    latitude = latitude,
                    longitude = longitude,
                    response = it
                )
            )
        }
    }

    private suspend fun updateMeta(
        headers: Headers,
        latitude: Double,
        longitude: Double
    ) {
        metaDao.insert(
            ForecastMetaDbModel(
                latitude = latitude,
                longitude = longitude,
                expires = Rfc1123DateTimeConverter.fromTimestamp(headers["Expires"]),
                lastModified = Rfc1123DateTimeConverter.fromTimestamp(headers["Last-Modified"])
            )
        )
    }
}