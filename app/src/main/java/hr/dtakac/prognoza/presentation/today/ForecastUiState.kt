package hr.dtakac.prognoza.presentation.today

import androidx.annotation.DrawableRes
import hr.dtakac.prognoza.entities.forecast.ForecastDescription
import hr.dtakac.prognoza.presentation.TextResource

data class ForecastUiState(
    val isLoading: Boolean = false,
    val error: TextResource? = null,
    val today: TodayContent? = null
)

data class TodayContent(
    val place: TextResource,
    val time: TextResource,
    val temperature: TextResource,
    val feelsLike: TextResource,
    val description: TextResource,
    val lowHighTemperature: TextResource,
    val wind: TextResource,
    val precipitation: TextResource,
    val shortDescription: ForecastDescription.Short,
    val hourly: List<TodayHour>
)

data class TodayHour(
    val time: TextResource,
    @DrawableRes
    val temperature: TextResource,
    val precipitation: TextResource,
    val description: TextResource
)