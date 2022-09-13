package hr.dtakac.prognoza.presentation.today

import androidx.annotation.DrawableRes
import hr.dtakac.prognoza.entities.forecast.units.Temperature
import hr.dtakac.prognoza.presentation.TextResource

data class TodayUiState(
    val isLoading: Boolean = false,
    val error: TextResource? = null,
    val content: TodayContent? = null
)

data class TodayContent(
    val placeName: TextResource,
    val time: TextResource,
    val temperatureValue: Temperature,
    val temperature: TextResource,
    val feelsLike: TextResource,
    val description: TextResource,
    val lowHighTemperature: TextResource,
    val wind: TextResource,
    val precipitation: TextResource,
    val hours: List<TodayHour>
)

data class TodayHour(
    val time: TextResource,
    @DrawableRes
    val temperature: TextResource,
    val precipitation: TextResource,
    val description: TextResource
)