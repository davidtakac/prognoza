package hr.dtakac.prognoza.fakes

import com.google.gson.Gson
import hr.dtakac.prognoza.api.*
import okhttp3.Headers
import retrofit2.Response
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.time.ZonedDateTime

class FakeForecastService : ForecastService {
    companion object {
        val startOfData = ZonedDateTime.parse("2021-08-16T20:00:00Z")
        val endOfData = ZonedDateTime.parse("2021-08-26T06:00:00Z")
    }

    override suspend fun getCompactLocationForecast(
        userAgent: String,
        ifModifiedSince: String?,
        latitude: String,
        longitude: String
    ): Response<LocationForecastResponse> {
        val json = readFileWithoutNewLineFromResources("osijek_16_08_21_response.json")
        val body = Gson().fromJson(json, LocationForecastResponse::class.java)
        val headers = Headers.headersOf(
            "Expires", "Mon, 16 Aug 2021 21:18:38 GMT",
            "Last-Modified", "Mon, 16 Aug 2021 20:47:40 GMT"
        )
        return Response.success(body, headers)
    }

    private fun readFileWithoutNewLineFromResources(fileName: String): String {
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