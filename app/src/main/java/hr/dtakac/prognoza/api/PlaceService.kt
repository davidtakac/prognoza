package hr.dtakac.prognoza.api

import hr.dtakac.prognoza.model.api.PlaceResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface PlaceService {
    @GET("search")
    suspend fun search(
        @Header("User-Agent") userAgent: String,
        @Header("Accept-Language") acceptLanguage: String,
        @Query("q") query: String,
        @Query("format") format: String
    ): List<PlaceResponse>
}