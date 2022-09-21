package hr.dtakac.prognoza.presentation.forecast

import android.text.format.DateUtils
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.domain.usecase.getforecast.*
import hr.dtakac.prognoza.entities.forecast.units.*
import hr.dtakac.prognoza.entities.forecast.wind.Wind
import hr.dtakac.prognoza.presentation.TextResource
import java.time.ZonedDateTime

fun mapToForecastUi(
    placeName: String,
    current: Current,
    today: Day,
    coming: List<Day>,
    temperatureUnit: TemperatureUnit,
    windUnit: SpeedUnit,
    precipitationUnit: LengthUnit
): ForecastUi = ForecastUi(
    place = TextResource.fromText(placeName),
    today = mapToTodayUi(current, today, temperatureUnit, windUnit, precipitationUnit),
    coming = mapToComingUi(coming, temperatureUnit, precipitationUnit)
)

fun mapToError(
    error: GetForecastResult.Error
): TextResource {
    val stringId = when (error) {
        GetForecastResult.Error.Client -> R.string.error_client
        GetForecastResult.Error.Database -> R.string.error_database
        GetForecastResult.Error.NoSelectedPlace -> R.string.error_no_selected_place
        GetForecastResult.Error.Server -> R.string.error_server
        GetForecastResult.Error.Throttle -> R.string.error_throttling
        GetForecastResult.Error.Unknown -> R.string.error_unknown
    }
    return TextResource.fromStringId(stringId)
}

private fun mapToTodayUi(
    current: Current,
    today: Day,
    temperatureUnit: TemperatureUnit,
    windUnit: SpeedUnit,
    precipitationUnit: LengthUnit
): TodayUi = TodayUi(
    time = TextResource.fromEpochMillis(
        millis = current.dateTime.toInstant().toEpochMilli(),
        flags = DateUtils.FORMAT_SHOW_DATE
    ),
    temperature = getTemperature(current.temperature, temperatureUnit),
    feelsLike = TextResource.fromStringId(
        R.string.template_feels_like,
        getTemperature(current.feelsLike, temperatureUnit)
    ),
    description = TextResource.fromStringId(current.description.toStringId()),
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
            flags = DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_WEEKDAY or DateUtils.FORMAT_ABBREV_WEEKDAY
        ),
        lowHighTemperature = getLowHighTemperature(
            lowTemperature = day.lowTemperature,
            highTemperature = day.highTemperature,
            temperatureUnit = temperatureUnit
        ),
        hourly = day.hourly.map { hourlyDatum ->
            getDayHourUi(
                datum = hourlyDatum,
                temperatureUnit = temperatureUnit,
                precipitationUnit = precipitationUnit
            )
        }
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

private fun getShortTime(time: ZonedDateTime): TextResource = TextResource.fromEpochMillis(
    millis = time.toInstant().toEpochMilli(),
    flags = DateUtils.FORMAT_SHOW_TIME or DateUtils.FORMAT_ABBREV_TIME
)

private fun getTemperature(
    temperature: Temperature,
    unit: TemperatureUnit
): TextResource = TextResource.fromStringId(
    id = R.string.template_temperature_degrees,
    TextResource.fromNumber(
        temperature.run {
            when (unit) {
                TemperatureUnit.C -> celsius
                TemperatureUnit.F -> fahrenheit
            }
        }
    )
)

private fun getWind(
    wind: Wind,
    unit: SpeedUnit
): TextResource = TextResource.fromStringId(
    id = when (unit) {
        SpeedUnit.KPH -> R.string.template_wind_kmh
        SpeedUnit.MPH -> R.string.template_wind_mph
        SpeedUnit.MPS -> R.string.template_wind_mps
    },
    TextResource.fromNumber(
        wind.speed.run {
            when (unit) {
                SpeedUnit.KPH -> kilometersPerHour
                SpeedUnit.MPH -> milesPerHour
                SpeedUnit.MPS -> metersPerSecond
            }
        }
    )
)

private fun getPrecipitation(
    precipitation: Length,
    unit: LengthUnit
): TextResource = TextResource.fromStringId(
    id = when (unit) {
        LengthUnit.MM -> R.string.template_precipitation_mm
        LengthUnit.IN -> R.string.template_precipitation_in
    },
    TextResource.fromNumber(precipitation.run {
        when (unit) {
            LengthUnit.MM -> millimeters
            LengthUnit.IN -> inches
        }
    }, decimalPlaces = 2)
)

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
    description = TextResource.fromStringId(datum.description.toStringId())
)