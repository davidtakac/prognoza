package hr.dtakac.prognoza.forecast.uimodel

import java.time.ZonedDateTime

sealed class TomorrowUiModel {
    data class Success(
        val dateTime: ZonedDateTime,
        val lowTemperature: Int,
        val highTemperature: Int,
        val weatherIcon: WeatherIcon?,
        val hours: List<HourUiModel>
    ): TomorrowUiModel()

    data class Error(
        val errorMessageResourceId: Int
    ): TomorrowUiModel()
}