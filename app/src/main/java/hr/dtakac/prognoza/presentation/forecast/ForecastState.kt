package hr.dtakac.prognoza.presentation.forecast

import androidx.annotation.DrawableRes
import hr.dtakac.prognoza.entities.forecast.Mood
import hr.dtakac.prognoza.presentation.TextResource

data class ForecastState(
    val isLoading: Boolean = false,
    val error: TextResource? = null,
    val forecast: ForecastUi? = null
)

data class ForecastUi(
    val current: CurrentUi,
    val today: TodayUi?,
    val coming: List<DayUi>?
)

data class CurrentUi(
    val place: TextResource,
    val mood: Mood,
    val date: TextResource,
    val temperature: TextResource,
    val description: TextResource,
    @DrawableRes
    val icon: Int,
    val wind: TextResource,
    val feelsLike: TextResource,
    val precipitation: TextResource
)

data class TodayUi(
    val lowHighTemperature: TextResource,
    val hourly: List<DayHourUi>
)

data class DayHourUi(
    val time: TextResource,
    val temperature: TextResource,
    val precipitation: TextResource,
    val description: TextResource,
    @DrawableRes
    val icon: Int
)

data class DayUi(
    val date: TextResource,
    val lowHighTemperature: TextResource,
    val precipitation: TextResource,
    val hours: List<ComingHourUi>
)

data class ComingHourUi(
    val time: TextResource,
    val temperature: TextResource,
    @DrawableRes
    val icon: Int
)