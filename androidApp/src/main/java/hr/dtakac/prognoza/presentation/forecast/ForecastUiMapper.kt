package hr.dtakac.prognoza.presentation.forecast

import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.presentation.TextResource
import hr.dtakac.prognoza.presentation.settingsscreen.toSettingsLabel
import hr.dtakac.prognoza.shared.domain.GetForecastResult
import hr.dtakac.prognoza.shared.entity.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject
import javax.inject.Named

class ForecastUiMapper @Inject constructor(
    @Named("computation")
    private val computationDispatcher: CoroutineDispatcher
) {
    suspend fun mapToForecastUi(
        data: GetForecastResult.Success
    ): ForecastUi = withContext(computationDispatcher) {
        val tempUnit = data.temperatureUnit
        val windUnit = data.windUnit
        val precipUnit = data.precipitationUnit
        ForecastUi(
            current = mapToCurrentUi(data.placeName, data.forecast.current, tempUnit, windUnit, precipUnit),
            today = data.forecast.today?.let { mapToTodayUi(it, tempUnit, precipUnit) },
            coming = data.forecast.coming?.let { mapToComingUi(it, tempUnit, precipUnit) },
            provider = TextResource.fromStringId(
                R.string.template_data_from,
                TextResource.fromStringId(data.provider.toSettingsLabel())
            )
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
        place = TextResource.fromString(placeName),
        mood = current.mood,
        date = TextResource.fromDate(current.epochMillis),
        temperature = getTemperature(current.temperature, temperatureUnit),
        description = TextResource.fromStringId(current.description.toStringId()),
        weatherIconDescription = current.description,
        wind = getWind(current.wind, windUnit),
        feelsLike = TextResource.fromStringId(
            id = R.string.template_feels_like,
            getTemperature(current.feelsLike, temperatureUnit)
        ),
        precipitation = getPrecipitation(current.precipitation, precipitationUnit)
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
        precipitation = getPrecipitation(
            precipitation = today.precipitation,
            unit = precipitationUnit
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
    ): List<ComingDayUi> = days.map { day ->
        ComingDayUi(
            date = TextResource.fromShortDateAndWeekday(day.epochMillis),
            lowHighTemperature = getLowHighTemperature(
                lowTemperature = day.lowTemperature,
                highTemperature = day.highTemperature,
                temperatureUnit = temperatureUnit
            ),
            precipitation = getPrecipitation(day.totalPrecipitation, precipitationUnit),
            hours = day.hours.map {
                ComingDayHourUi(
                    time = TextResource.fromShortTime(it.epochMillis),
                    temperature = getTemperature(
                        temperature = it.temperature,
                        unit = temperatureUnit
                    ),
                    weatherIconDescription = it.description
                )
            },
            weatherIconDescriptions = mutableListOf<Description?>().apply {
                add(day.morning)
                add(day.afternoon)
                add(day.evening)
                add(day.night)
            }
        )
    }

    private fun getDayHourUi(
        datum: HourlyDatum,
        temperatureUnit: TemperatureUnit,
        precipitationUnit: LengthUnit
    ): DayHourUi = DayHourUi(
        time = TextResource.fromShortTime(datum.epochMillis),
        temperature = getTemperature(datum.temperature, temperatureUnit),
        precipitation = datum.precipitation.takeIf { it.millimetre > 0.0 }?.let {
            getPrecipitation(it, precipitationUnit)
        } ?: TextResource.empty(),
        description = TextResource.fromStringId(datum.description.toStringId()),
        weatherIconDescription = datum.description
    )

    private fun getPrecipitation(
        precipitation: Length,
        unit: LengthUnit
    ): TextResource {
        val precipitationValue = BigDecimal(
            when (unit) {
                LengthUnit.MILLIMETRE -> precipitation.millimetre
                LengthUnit.INCH -> precipitation.inch
                LengthUnit.CENTIMETRE -> precipitation.centimetre
            }
        ).setScale(1, RoundingMode.HALF_EVEN)

        return if (precipitationValue.compareTo(BigDecimal.ZERO) == 0) TextResource.empty() else {
            val precipitationTemplateId = when (unit) {
                LengthUnit.MILLIMETRE -> R.string.template_precipitation_mm
                LengthUnit.INCH -> R.string.template_precipitation_in
                LengthUnit.CENTIMETRE -> R.string.template_precipitation_cm
            }
            TextResource.fromStringId(
                id = precipitationTemplateId,
                TextResource.fromNumber(precipitationValue)
            )
        }
    }
    private fun getWind(
        wind: Wind,
        windSpeedUnit: SpeedUnit
    ): TextResource {
        val windSpeedValue = BigDecimal(
            when (windSpeedUnit) {
                SpeedUnit.METRE_PER_SECOND -> wind.speed.metrePerSecond
                SpeedUnit.KILOMETRE_PER_HOUR -> wind.speed.kilometrePerHour
                SpeedUnit.MILE_PER_HOUR -> wind.speed.milePerHour
                SpeedUnit.KNOT -> wind.speed.knot
            }
        ).setScale(0, RoundingMode.HALF_EVEN)

        val windSpeedText = if (windSpeedValue.compareTo(BigDecimal.ZERO) == 0) null else {
            val windSpeedTemplateId = when (windSpeedUnit) {
                SpeedUnit.METRE_PER_SECOND -> R.string.template_wind_mps
                SpeedUnit.KILOMETRE_PER_HOUR -> R.string.template_wind_kmh
                SpeedUnit.MILE_PER_HOUR -> R.string.template_wind_mph
                SpeedUnit.KNOT -> R.string.template_wind_knots
            }
            TextResource.fromStringId(
                id = windSpeedTemplateId,
                TextResource.fromNumber(windSpeedValue)
            )
        }

        val beaufortText = TextResource.fromStringId(wind.speed.beaufortScale.toStringId())
        return if (windSpeedText == null) beaufortText else TextResource.fromStringId(
            id = R.string.template_wind,
            beaufortText,
            windSpeedText
        )
    }
    private fun getLowHighTemperature(
        lowTemperature: Temperature,
        highTemperature: Temperature,
        temperatureUnit: TemperatureUnit
    ): TextResource = TextResource.fromStringId(
        id = R.string.template_high_low_temperature,
        getTemperature(highTemperature, temperatureUnit),
        getTemperature(lowTemperature, temperatureUnit)
    )
}