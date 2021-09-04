package hr.dtakac.prognoza.uimodel.cell

import hr.dtakac.prognoza.uimodel.MeasurementUnit
import hr.dtakac.prognoza.uimodel.WeatherDescription
import java.time.ZonedDateTime

data class HourUiModel(
    val id: String,
    val temperature: Float?,
    val feelsLike: Float?,
    val precipitationAmount: Float?,
    val windSpeed: Float?,
    val windFromCompassDirection: Int?,
    val weatherDescription: WeatherDescription?,
    val time: ZonedDateTime,
    val relativeHumidity: Float?,
    val airPressureAtSeaLevel: Float?,
    val displayDataInUnit: MeasurementUnit,
    var isExpanded: Boolean = false
)