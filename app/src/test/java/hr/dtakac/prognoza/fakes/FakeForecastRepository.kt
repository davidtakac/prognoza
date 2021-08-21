package hr.dtakac.prognoza.fakes

import com.google.gson.Gson
import hr.dtakac.prognoza.apimodel.LocationForecastResponse
import hr.dtakac.prognoza.common.TEST_PLACE_ID
import hr.dtakac.prognoza.extensions.atStartOfDay
import hr.dtakac.prognoza.extensions.toForecastHour
import hr.dtakac.prognoza.repomodel.CachedSuccess
import hr.dtakac.prognoza.repomodel.Empty
import hr.dtakac.prognoza.repomodel.ForecastResult
import hr.dtakac.prognoza.repomodel.Success
import hr.dtakac.prognoza.repository.forecast.*
import okhttp3.Headers
import retrofit2.Response
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.IllegalStateException
import java.time.ZonedDateTime

class FakeForecastRepository : ForecastRepository {
    companion object {
        val now: ZonedDateTime = ZonedDateTime.parse("2021-08-16T20:00:00Z")
    }

    private val hoursAfterMidnightToShow = 6L
    var typeOfResultToReturn: Class<*> = Success::class.java

    override suspend fun deleteExpiredData() {
        // do nothing
    }

    override suspend fun getOtherDaysForecastHours(placeId: String): ForecastResult {
        val start = now.atStartOfDay()
        return getForecastHours(
            start,
            start.plusDays(7L),
            TEST_PLACE_ID
        )
    }

    override suspend fun getTodayForecastHours(placeId: String): ForecastResult {
        val anHourAgo = now
            .minusHours(1)
        val hoursLeftInTheDay = 24 - anHourAgo.hour
        val hoursToShow = hoursLeftInTheDay + hoursAfterMidnightToShow
        return getForecastHours(anHourAgo, anHourAgo.plusHours(hoursToShow), TEST_PLACE_ID)
    }

    override suspend fun getTomorrowForecastHours(placeId: String): ForecastResult {
        val tomorrow = now
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
        val response = getData()
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

    private fun getData(): Response<LocationForecastResponse> {
        val json = readFileFromResources("osijek_16_08_21_response.json")
        val body = Gson().fromJson(json, LocationForecastResponse::class.java)
        val headers = Headers.headersOf(
            "Expires", "Mon, 16 Aug 2021 21:18:38 GMT",
            "Last-Modified", "Mon, 16 Aug 2021 20:47:40 GMT"
        )
        return Response.success(body, headers)
    }

    private fun readFileFromResources(fileName: String): String {
        var inputStream: InputStream? = null
        try {
            inputStream =
                javaClass.classLoader?.getResourceAsStream(fileName)
            val builder = StringBuilder()
            val reader = BufferedReader(InputStreamReader(inputStream))

            var str: String? = reader.readLine()
            while (str != null) {
                builder.append(str)
                str = reader.readLine()
            }
            return builder.toString()
        } finally {
            inputStream?.close()
        }
    }
}