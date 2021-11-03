package hr.dtakac.prognoza.model.ui.cell

import hr.dtakac.prognoza.model.ui.MeasurementUnit
import hr.dtakac.prognoza.model.ui.RepresentativeWeatherDescription
import java.time.ZonedDateTime

data class DayUiModel(
    val id: String,
    val time: ZonedDateTime,
    val representativeWeatherDescription: RepresentativeWeatherDescription?,
    val lowTemperature: Double?,
    val highTemperature: Double?,
    val maxWindSpeed: Double?,
    val windFromCompassDirection: Int?,
    val totalPrecipitationAmount: Double?,
    val maxHumidity: Double?,
    val maxPressure: Double?,
    var isExpanded: Boolean = false,
    val displayDataInUnit: MeasurementUnit
)