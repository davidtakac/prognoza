package hr.dtakac.prognoza.model.ui.cell

import androidx.compose.runtime.Immutable
import hr.dtakac.prognoza.model.ui.MeasurementUnit
import hr.dtakac.prognoza.model.ui.WeatherDescription
import java.time.ZonedDateTime

@Immutable
data class HourUiModel(
    val id: String,
    val temperature: Double?,
    val feelsLike: Double?,
    val precipitationAmount: Double?,
    val windSpeed: Double?,
    val windFromCompassDirection: Int?,
    val weatherDescription: WeatherDescription?,
    val time: ZonedDateTime,
    val relativeHumidity: Double?,
    val airPressureAtSeaLevel: Double?
)