package hr.dtakac.prognoza.shared.data

import hr.dtakac.prognoza.shared.entity.Coordinates
import hr.dtakac.prognoza.shared.entity.Forecast
import hr.dtakac.prognoza.shared.entity.Place

internal class ForecastRepository(private val apiService: ForecastService) {
    // todo: implement caching to local storage
    suspend fun getForecast(coordinates: Coordinates): Forecast? = apiService.getForecast(coordinates)
    suspend fun deleteForecast(place: Place) {/*todo*/}
}