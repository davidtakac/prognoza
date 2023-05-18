package hr.dtakac.prognoza.shared.data

import hr.dtakac.prognoza.shared.entity.Forecast

internal class ForecastRepository(
    private val apiService: ForecastService,
    private val placeRepository: PlaceRepository
) {
    // todo: implement caching to local storage
    suspend fun getForecast(placeId: String): Forecast? =
        placeRepository.getSavedPlace(placeId)?.let {
            apiService.getForecast(
                latitude = it.latitude,
                longitude = it.longitude
            )
        }

    suspend fun deleteForecast(placeId: String) {/*todo*/}
}