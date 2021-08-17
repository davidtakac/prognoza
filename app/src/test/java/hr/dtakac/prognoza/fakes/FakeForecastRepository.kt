package hr.dtakac.prognoza.fakes

import hr.dtakac.prognoza.common.TEST_PLACE_ID
import hr.dtakac.prognoza.common.util.atStartOfDay
import hr.dtakac.prognoza.common.util.toForecastHour
import hr.dtakac.prognoza.repository.forecast.*
import java.lang.IllegalStateException
import java.time.ZonedDateTime

class FakeForecastRepository : ForecastRepository {
    private val fakeForecastService = FakeForecastService()
    private val hoursAfterMidnightToShow = 6L

    var typeOfResultToReturn: Class<*> = Success::class.java

    override suspend fun deleteExpiredData() {
        // do nothing
    }

    override suspend fun getOtherDaysForecastHours(placeId: String): ForecastResult {
        val start = FakeForecastService.startOfData.atStartOfDay()
        return getForecastHours(
            start,
            start.plusDays(7L),
            TEST_PLACE_ID
        )
    }

    override suspend fun getTodayForecastHours(placeId: String): ForecastResult {
        val anHourAgo = FakeForecastService
            .startOfData
            .minusHours(1)
        val hoursLeftInTheDay = 24 - anHourAgo.hour
        val hoursToShow = hoursLeftInTheDay + hoursAfterMidnightToShow
        return getForecastHours(anHourAgo, anHourAgo.plusHours(hoursToShow), TEST_PLACE_ID)
    }

    override suspend fun getTomorrowForecastHours(placeId: String): ForecastResult {
        val tomorrow = FakeForecastService
            .startOfData
            .atStartOfDay()
            .plusDays(1)
        return getForecastHours(
            tomorrow.plusHours(hoursAfterMidnightToShow + 1L),
            tomorrow.plusDays(1L).plusHours(hoursAfterMidnightToShow),
            TEST_PLACE_ID
        )
    }

    private suspend fun getForecastHours(
        start: ZonedDateTime,
        end: ZonedDateTime,
        placeId: String
    ): ForecastResult {
        // we're using a fake forecast service so the parameters don't matter
        val response = fakeForecastService.getCompactLocationForecast("", "", "", "")
        // filter data according to start and end times then map to ForecastHour
        val hours = response.body()!!.forecast.forecastTimeSteps
            .filter {
                val time = ZonedDateTime.parse(it.time)
                time in start..end
            }
            .map {
                it.toForecastHour(TEST_PLACE_ID)
            }
        val success = Success(null, hours)
        return when(typeOfResultToReturn) {
            CachedSuccess::class.java -> CachedSuccess(success, null)
            Empty::class.java -> Empty(null)
            Success::class.java -> success
            else -> throw IllegalStateException("Result type $typeOfResultToReturn not recognized.")
        }
    }
}