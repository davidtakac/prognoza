package hr.dtakac.prognoza.fake

import com.google.gson.Gson
import hr.dtakac.prognoza.api.*
import okhttp3.Headers
import retrofit2.Response
import java.io.File
import java.lang.RuntimeException

class FakeForecastService : ForecastService {
    var throwError: Boolean = false

    override suspend fun getCompactLocationForecast(
        userAgent: String,
        ifModifiedSince: String?,
        latitude: String,
        longitude: String
    ): Response<LocationForecastResponse> {
        if (throwError) {
            throw RuntimeException("An error occurred!")
        } else {
            val json =
                File("../assets/osijek_16_08_21_response.json").readLines().joinToString("\n")
            val body = Gson().fromJson(json, LocationForecastResponse::class.java)
            val headers = Headers.headersOf(
                "Expires", "Mon, 16 Aug 2021 21:18:38 GMT",
                "Last-Modified", "Mon, 16 Aug 2021 20:47:40 GMT"
            )
            return Response.success(body, headers)
        }
    }
}