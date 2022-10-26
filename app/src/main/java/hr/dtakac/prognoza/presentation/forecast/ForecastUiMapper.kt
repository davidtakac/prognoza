package hr.dtakac.prognoza.presentation.forecast

import android.text.format.DateUtils
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
        today: Today,
        coming: List<Day>,
        temperatureUnit: TemperatureUnit,
        windUnit: SpeedUnit,
        precipitationUnit: LengthUnit
    ): ForecastUi = withContext(computationDispatcher) {
        ForecastUi(
            place = TextResource.fromText(placeName),
            today = mapToTodayUi(current, today, temperatureUnit, windUnit, precipitationUnit),
            coming = mapToComingUi(coming, temperatureUnit, precipitationUnit)
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

    private fun mapToTodayUi(
        current: Current,
        today: Today,
        temperatureUnit: TemperatureUnit,
        windUnit: SpeedUnit,
        precipitationUnit: LengthUnit
    ): TodayUi = TodayUi(
        date = TextResource.fromEpochMillis(
            millis = current.dateTime.toInstant().toEpochMilli(),
            flags = DateUtils.FORMAT_SHOW_DATE
        ),
        temperature = getTemperature(current.temperature, temperatureUnit),
        feelsLike = TextResource.fromStringId(
            R.string.template_feels_like,
            getTemperature(current.feelsLike, temperatureUnit)
        ),
        description = TextResource.fromStringId(current.description.toStringId()),
        iconResId = current.description.toDrawableId(),
        lowHighTemperature = getLowHighTemperature(
            today.lowTemperature,
            today.highTemperature,
            temperatureUnit
        ),
        wind = TextResource.fromStringId(
            id = R.string.template_wind,
            getWind(current.wind, windUnit)
        ),
        precipitation = TextResource.fromStringId(
            id = R.string.template_precipitation,
            getPrecipitation(current.precipitation, precipitationUnit)
        ),
        shortDescription = current.description.short,
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
            date = TextResource.fromEpochMillis(
                millis = day.dateTime.toInstant().toEpochMilli(),
                flags = DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_ABBREV_MONTH or DateUtils.FORMAT_SHOW_WEEKDAY
            ),
            lowHighTemperature = getLowHighTemperature(
                lowTemperature = day.lowTemperature,
                highTemperature = day.highTemperature,
                temperatureUnit = temperatureUnit
            ),
            precipitation = day.totalPrecipitation.takeIf { it.millimeters > 0.0 }?.let {
                getPrecipitation(it, precipitationUnit)
            } ?: TextResource.fromText(""),
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
        time = getShortTime(datum.dateTime),
        temperature = getTemperature(datum.temperature, temperatureUnit),
        precipitation = datum.precipitation.takeIf { it.millimeters > 0.0 }?.let {
            getPrecipitation(it, precipitationUnit)
        } ?: TextResource.fromText(""),
        description = TextResource.fromStringId(datum.description.toStringId()),
        icon = datum.description.toDrawableId()
    )
}