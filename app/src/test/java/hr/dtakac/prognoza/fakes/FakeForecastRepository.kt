package hr.dtakac.prognoza.fakes

import com.google.gson.Gson
import hr.dtakac.prognoza.common.TEST_PLACE
import hr.dtakac.prognoza.core.model.api.LocationForecastResponse
import hr.dtakac.prognoza.core.model.database.ForecastTimeSpan
import hr.dtakac.prognoza.core.model.database.Place
import hr.dtakac.prognoza.core.model.repository.CachedSuccess
import hr.dtakac.prognoza.core.model.repository.Empty
import hr.dtakac.prognoza.core.model.repository.ForecastResult
import hr.dtakac.prognoza.core.model.repository.Success
import hr.dtakac.prognoza.core.repository.forecast.ForecastRepository
import okhttp3.Headers
import retrofit2.Response
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.time.ZonedDateTime

class FakeForecastRepository : ForecastRepository {
    companion object {
        val now: ZonedDateTime = ZonedDateTime.parse("2021-08-16T20:00:00Z")
    }

    var typeOfResultToReturn: Class<*> = Success::class.java

    override suspend fun deleteExpiredData() {
        // do nothing
    }

    override suspend fun updateForecastTimespans(
        start: ZonedDateTime,
        end: ZonedDateTime,
        place: Place
    ): ForecastResult {
        val response = getData()
        // filter data according to start and end times then map to ForecastTimeSpan
        val timeSteps = response.body()!!.forecast.forecastTimeSteps
            .filter {
                val time = ZonedDateTime.parse(it.time)
                time in start..end
            }
        val timeSpans: MutableList<ForecastTimeSpan> = mutableListOf()
        for (i in timeSteps.indices) {
            timeSpans.add(
                timeSteps[i].toForecastTimeSpan(
                    TEST_PLACE.id,
                    timeSteps.getOrNull(i + 1)
                )
            )
        }
        val success = Success(null, timeSpans)
        return when (typeOfResultToReturn) {
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