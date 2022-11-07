package hr.dtakac.prognoza.presentation.forecast

import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.domain.usecase.GetForecastResult
import hr.dtakac.prognoza.entities.forecast.*
import hr.dtakac.prognoza.entities.forecast.units.*
import hr.dtakac.prognoza.presentation.TextResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class ForecastUiMapper @Inject constructor(
    @Named("computation")
    private val computationDispatcher: CoroutineDispatcher
) {
    suspend fun mapToForecastUi(
        placeName: String,
        current: Current,
        today: Today?,
        coming: List<Day>?,
        temperatureUnit: TemperatureUnit,
        windUnit: SpeedUnit,
        precipitationUnit: LengthUnit
    ): ForecastUi = withContext(computationDispatcher) {
        ForecastUi(
            current = mapToCurrentUi(placeName, current, temperatureUnit, windUnit, precipitationUnit),
            today = today?.let { mapToTodayUi(it, temperatureUnit, precipitationUnit) },
            coming = coming?.let { mapToComingUi(it, temperatureUnit, precipitationUnit) }
        )
    }

    fun mapToError(
        error: GetForecastResult.Empty
    ): TextResource = TextResource.fromStringId(
        when (error) {
            GetForecastResult.Empty.NoSelectedPlace -> R.string.error_no_selected_place
            GetForecastResult.Empty.Error -> R.string.error_unknown
        }
    )

    private fun mapToCurrentUi(
        placeName: String,
        current: Current,
        temperatureUnit: TemperatureUnit,
        windUnit: SpeedUnit,
        precipitationUnit: LengthUnit
    ): CurrentUi = CurrentUi(
        place = TextResource.fromText(placeName),
        mood = current.mood,
        date = TextResource.fromDate(current.dateTime.toInstant().toEpochMilli()),
        temperature = getTemperature(current.temperature, temperatureUnit),
        description = TextResource.fromStringId(current.description.toStringId()),
        icon = current.description.toDrawableId(),
        wind = TextResource.fromStringId(
            id = R.string.template_wind,
            TextResource.fromStringId(current.wind.speed.beaufort.toStringId()),
            getWind(current.wind, windUnit)
        ),
        feelsLike = TextResource.fromStringId(
            id = R.string.template_feels_like,
            getTemperature(current.temperature, temperatureUnit)
        ),
        precipitation = current.precipitation.takeIf { it.millimeters > 0 }?.let {
            getPrecipitation(it, precipitationUnit)
        } ?: TextResource.empty()
    )

    private fun mapToTodayUi(
        today: Today,
        temperatureUnit: TemperatureUnit,
        precipitationUnit: LengthUnit
    ): TodayUi = TodayUi(
        lowHighTemperature = getLowHighTemperature(
            today.lowTemperature,
            today.highTemperature,
            temperatureUnit
        ),
        hourly = today.hourly.map { datum ->
            getDayHourUi(
                datum,
                temperatureUnit,
                precipitationUnit,
            )
        }
    )

    private fun mapToComingUi(
        days: List<Day>,
        temperatureUnit: TemperatureUnit,
        precipitationUnit: LengthUnit
    ): List<DayUi> = days.map { day ->
        DayUi(
            date = TextResource.fromShortDateAndWeekday(day.dateTime.toInstant().toEpochMilli()),
            lowHighTemperature = getLowHighTemperature(
                lowTemperature = day.lowTemperature,
                highTemperature = day.highTemperature,
                temperatureUnit = temperatureUnit
            ),
            precipitation = day.totalPrecipitation.takeIf { it.millimeters > 0.0 }?.let {
                getPrecipitation(it, precipitationUnit)
            } ?: TextResource.empty(),
            hours = day.hours.map {
                ComingHourUi(
                    time = getShortTime(it.dateTime),
                    temperature = getTemperature(
                        temperature = it.temperature,
                        unit = temperatureUnit
                    ),
                    icon = it.description.toDrawableId()
                )
            }
        )
    }

    private fun getDayHourUi(
        datum: HourlyDatum,
        temperatureUnit: TemperatureUnit,
        precipitationUnit: LengthUnit
    ): DayHourUi = DayHourUi(
        time = TextResource.fromText(listOf("1", "12", "123", "1234", "12345").random()),
        temperature = getTemperature(datum.temperature, temperatureUnit),
        precipitation = datum.precipitation.takeIf { it.millimeters > 0.0 }?.let {
            getPrecipitation(it, precipitationUnit)
        } ?: TextResource.empty(),
        description = TextResource.fromStringId(datum.description.toStringId()),
        icon = datum.description.toDrawableId()
    )
}