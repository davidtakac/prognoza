package hr.dtakac.prognoza.presentation.today

import android.text.format.DateUtils
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.domain.usecase.gettodayforecast.TodayForecastResult
import hr.dtakac.prognoza.domain.usecase.gettodayforecast.TodayForecast
import hr.dtakac.prognoza.domain.usecase.gettodayforecast.HourlyDatum
import hr.dtakac.prognoza.entities.forecast.units.*
import hr.dtakac.prognoza.entities.forecast.wind.Wind
import hr.dtakac.prognoza.presentation.TextResource
import hr.dtakac.prognoza.presentation.toStringId
import java.time.ZonedDateTime

fun mapToTodayContent(
    placeName: String,
    todayForecast: TodayForecast,
    temperatureUnit: TemperatureUnit,
    windUnit: SpeedUnit,
    precipitationUnit: LengthUnit
): TodayContent = TodayContent(
    placeName = TextResource.fromText(placeName),
    time = getLongTime(todayForecast.time),
    temperature = getTemperature(todayForecast.temperature, temperatureUnit),
    feelsLike = TextResource.fromStringId(
        R.string.template_feels_like,
        getTemperature(todayForecast.feelsLike, temperatureUnit)
    ),
    description = TextResource.fromStringId(todayForecast.description.toStringId()),
    lowHighTemperature = getLowHighTemperature(
        todayForecast.lowTemperature,
        todayForecast.highTemperature,
        temperatureUnit
    ),
    wind = TextResource.fromStringId(
        id = R.string.template_wind,
        getWind(todayForecast.wind, windUnit)
    ),
    precipitation = TextResource.fromStringId(
        id = R.string.template_precipitation,
        getPrecipitation(todayForecast.precipitation, precipitationUnit)
    ),
    shortDescription = todayForecast.description.short,
    hours = todayForecast.hourly.map { datum ->
        getHour(
            datum,
            temperatureUnit,
            precipitationUnit,
        )
    }
)

fun getLowHighTemperature(
    lowTemperature: Temperature,
    highTemperature: Temperature,
    temperatureUnit: TemperatureUnit
): TextResource = TextResource.fromStringId(
    id = R.string.template_high_low_temperature,
    getTemperature(highTemperature, temperatureUnit),
    getTemperature(lowTemperature, temperatureUnit)
)

fun mapToTodayError(
    error: TodayForecastResult.Error
): TextResource {
    val stringId = when (error) {
        TodayForecastResult.Error.Client -> R.string.error_client
        TodayForecastResult.Error.Database -> R.string.error_database
        TodayForecastResult.Error.NoSelectedPlace -> R.string.error_no_selected_place
        TodayForecastResult.Error.Server -> R.string.error_server
        TodayForecastResult.Error.Throttle -> R.string.error_throttling
        TodayForecastResult.Error.Unknown -> R.string.error_unknown
    }
    return TextResource.fromStringId(stringId)
}

private fun getLongTime(time: ZonedDateTime): TextResource = TextResource.fromEpochMillis(
    millis = time.toInstant().toEpochMilli(),
    flags = DateUtils.FORMAT_SHOW_DATE
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

private fun getHour(
    datum: HourlyDatum,
    temperatureUnit: TemperatureUnit,
    precipitationUnit: LengthUnit
): TodayHour = TodayHour(
    time = getShortTime(datum.time),
    temperature = getTemperature(datum.temperature, temperatureUnit),
    precipitation = getPrecipitation(datum.precipitation, precipitationUnit),
    description = TextResource.fromStringId(datum.description.toStringId())
)