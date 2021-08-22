package hr.dtakac.prognoza.uimodel.cell

import hr.dtakac.prognoza.uimodel.WeatherDescription
import java.time.ZonedDateTime

data class HourCellModel(
    val id: String,
    val temperature: Float?,
    val feelsLike: Float?,
    val precipitation: Float?,
    val windSpeed: Float?,
    val windIconRotation: Float?,
    val windFromCompassDirection: Int?,
    val weatherDescription: WeatherDescription?,
    val time: ZonedDateTime,
    val relativeHumidity: Float?,
    val pressure: Float?,
    var isExpanded: Boolean = false
)