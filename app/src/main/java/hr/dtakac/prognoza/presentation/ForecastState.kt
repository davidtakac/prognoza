package hr.dtakac.prognoza.presentation

import androidx.annotation.DrawableRes
import hr.dtakac.prognoza.entities.forecast.ForecastDescription

data class ForecastState(
    val isLoading: Boolean = false,
    val error: TextResource? = null,
    val forecast: ForecastUi? = null
)

data class ForecastUi(
    val place: TextResource,
    val today: TodayUi,
    val coming: List<DayUi>
)

data class TodayUi(
    val time: TextResource,
    val temperature: TextResource,
    val feelsLike: TextResource,
    val description: TextResource,
    val lowHighTemperature: TextResource,
    val wind: TextResource,
    val precipitation: TextResource,
    val shortDescription: ForecastDescription.Short,
    val hourly: List<DayHourUi>
)

data class DayHourUi(
    val time: TextResource,
    @DrawableRes
    val temperature: TextResource,
    val precipitation: TextResource,
    val description: TextResource
)

data class DayUi(
    val date: TextResource,
    val lowHighTemperature: TextResource,
    val hourly: List<DayHourUi>
)

