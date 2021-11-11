package hr.dtakac.prognoza.forecast.model

import androidx.compose.runtime.Immutable
import hr.dtakac.prognoza.core.model.ui.WeatherDescription
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