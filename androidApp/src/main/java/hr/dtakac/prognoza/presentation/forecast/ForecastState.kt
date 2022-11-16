package hr.dtakac.prognoza.presentation.forecast

import hr.dtakac.prognoza.presentation.TextResource
import hr.dtakac.prognoza.shared.entity.Description
import hr.dtakac.prognoza.shared.entity.Mood

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
    val weatherIconDescription: Description,
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
    val weatherIconDescription: Description
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
    val weatherIconDescription: Description
)