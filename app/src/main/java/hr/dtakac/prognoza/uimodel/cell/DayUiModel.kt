package hr.dtakac.prognoza.uimodel.cell

import hr.dtakac.prognoza.uimodel.MeasurementUnit
import hr.dtakac.prognoza.uimodel.RepresentativeWeatherDescription
import java.time.ZonedDateTime

data class DayUiModel(
    val id: String,
    val time: ZonedDateTime,
    val representativeWeatherDescription: RepresentativeWeatherDescription?,
    val lowTemperature: Float?,
    val highTemperature: Float?,
    val maxWindSpeed: Float?,
    val windFromCompassDirection: Int?,
    val totalPrecipitationAmount: Float?,
    val maxHumidity: Float?,
    val maxPressure: Float?,
    var isExpanded: Boolean = false,
    val displayDataInUnit: MeasurementUnit
)