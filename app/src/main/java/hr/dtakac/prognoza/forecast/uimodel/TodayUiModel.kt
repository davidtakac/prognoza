package hr.dtakac.prognoza.forecast.uimodel

import java.time.ZonedDateTime

sealed class TodayUiModel {
    data class Success(
        val dateTime: ZonedDateTime,
        val currentTemperature: Int?,
        val weatherIcon: WeatherIcon?,
        val nextHours: List<HourUiModel>
    ): TodayUiModel()

    data class Error(
        val errorMessageResourceId: Int
    ): TodayUiModel()
}