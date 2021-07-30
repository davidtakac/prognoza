package hr.dtakac.prognoza.api

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface PlaceService {
    @GET("search")
    suspend fun search(
        @Header("User-Agent") userAgent: String,
        @Query("q") query: String
    ): List<PlaceResponse>
}