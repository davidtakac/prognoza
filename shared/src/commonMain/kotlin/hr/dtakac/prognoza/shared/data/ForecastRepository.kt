package hr.dtakac.prognoza.shared.data

import hr.dtakac.prognoza.shared.entity.Forecast

internal class ForecastRepository(
    private val apiService: ForecastService,
    private val placeRepository: PlaceRepository
) {
    // todo: implement caching to local storage
    suspend fun get(placeId: String): Forecast? =
        placeRepository.get(placeId)?.let {
            apiService.getForecast(
                latitude = it.latitude,
                longitude = it.longitude,
                timeZone = it.timeZone
            )
        }

    suspend fun delete(placeId: String) {/*todo*/}
}