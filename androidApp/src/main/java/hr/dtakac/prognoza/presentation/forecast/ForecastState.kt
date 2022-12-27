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
    val coming: List<ComingDayUi>?,
    val provider: TextResource
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
    val precipitation: TextResource,
    val hourly: List<DayHourUi>
)

data class DayHourUi(
    val time: TextResource,
    val temperature: TextResource,
    val precipitation: TextResource,
    val description: TextResource,
    val weatherIconDescription: Description
)

data class ComingDayUi(
    val date: TextResource,
    val lowHighTemperature: TextResource,
    val precipitation: TextResource,
    val weatherIconDescriptions: List<Description?>,
    val hours: List<ComingDayHourUi>
)

data class ComingDayHourUi(
    val time: TextResource,
    val temperature: TextResource,
    val weatherIconDescription: Description
)