package hr.dtakac.prognoza.forecast.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import hr.dtakac.prognoza.core.model.ui.WeatherDescription
import java.time.ZonedDateTime

@Immutable
data class InstantUiModel(
    val time: ZonedDateTime,
    val nextInstantTime: ZonedDateTime?,
    val showNextInstantTime: Boolean,

    val temperature: Double?,
    val feelsLike: Double?,
    val precipitationAmount: Double?,
    val windSpeed: Double?,
    @StringRes val windCompassDirectionId: Int?,
    val relativeHumidity: Double?,
    val airPressure: Double?,
    val weatherDescription: WeatherDescription?,
)