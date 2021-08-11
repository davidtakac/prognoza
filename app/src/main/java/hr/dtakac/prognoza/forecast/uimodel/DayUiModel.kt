package hr.dtakac.prognoza.forecast.uimodel

import java.time.ZonedDateTime

data class DayUiModel(
    val time: ZonedDateTime,
    val representativeWeatherIcon: RepresentativeWeatherIcon?,
    val lowTemperature: Float?,
    val highTemperature: Float?,
    val maxWindSpeed: Float?,
    val windFromCompassDirection: Int?,
    val totalPrecipitationAmount: Float?,
    val maxHumidity: Float?,
    val maxPressure: Float?,
    var isExpanded: Boolean = false
)