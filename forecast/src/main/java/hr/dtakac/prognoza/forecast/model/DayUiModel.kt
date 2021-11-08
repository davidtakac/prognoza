package hr.dtakac.prognoza.forecast.model

import androidx.compose.runtime.Immutable
import hr.dtakac.prognoza.core.model.ui.RepresentativeWeatherDescription
import java.time.ZonedDateTime

@Immutable
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
    val maxPressure: Double?
)