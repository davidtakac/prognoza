package hr.dtakac.prognoza.presentation.today

import android.text.format.DateUtils
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.domain.usecase.gettodayforecast.NextPrecipitation
import hr.dtakac.prognoza.domain.usecase.gettodayforecast.TodayForecastResult
import hr.dtakac.prognoza.domain.usecase.gettodayforecast.TodayForecast
import hr.dtakac.prognoza.domain.usecase.gettodayforecast.SmallForecastDatum
import hr.dtakac.prognoza.entities.forecast.ForecastDescription
import hr.dtakac.prognoza.entities.forecast.precipitation.Precipitation
import hr.dtakac.prognoza.entities.forecast.precipitation.PrecipitationDescription
import hr.dtakac.prognoza.entities.forecast.units.LengthUnit
import hr.dtakac.prognoza.entities.forecast.units.SpeedUnit
import hr.dtakac.prognoza.entities.forecast.units.Temperature
import hr.dtakac.prognoza.entities.forecast.units.TemperatureUnit
import hr.dtakac.prognoza.entities.forecast.wind.Wind
import hr.dtakac.prognoza.presentation.TextResource
import hr.dtakac.prognoza.presentation.toCompassDirectionStringId
import hr.dtakac.prognoza.presentation.toDrawableId
import hr.dtakac.prognoza.presentation.toStringId
import java.time.ZonedDateTime

fun mapToTodayUiState(
    placeName: String,
    todayForecast: TodayForecast,
    temperatureUnit: TemperatureUnit,
    windUnit: SpeedUnit,
    precipitationUnit: LengthUnit
): TodayUiState.Success = TodayUiState.Success(
    placeName = TextResource.fromText(placeName),
    time = getLongTime(todayForecast.now),
    temperature = getTemperature(todayForecast.temperatureNow, temperatureUnit),
    feelsLike = TextResource.fromStringId(
        R.string.template_feels_like,
        getTemperature(todayForecast.feelsLikeNow, temperatureUnit)
    ),
    currentDescription = TextResource.fromStringId(
        id = R.string.template_current_description,
        getDescription(
            todayForecast.precipitationNow,
            todayForecast.descriptionNow,
            precipitationUnit
        ),
        getWindLong(todayForecast.windNow, windUnit)
    ),
    descriptionIcon = todayForecast.descriptionNow.toDrawableId(),
    restOfDayDescription = TextResource.fromStringId(
        id = R.string.template_rest_of_day_description,
        getNextPrecipitation(todayForecast.nextPrecipitation, precipitationUnit),
        getLowHighTemperature(todayForecast.lowTemperature, todayForecast.highTemperature, temperatureUnit)
    ),
    hours = todayForecast.restOfDayData.map { getHour(it, temperatureUnit, precipitationUnit, windUnit) }
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

fun mapToEmptyTodayUiState(
    error: TodayForecastResult.Error
): TodayUiState.Empty {
    val stringId = when (error) {
        TodayForecastResult.Error.Client -> R.string.error_client
        TodayForecastResult.Error.Database -> R.string.error_database
        TodayForecastResult.Error.NoSelectedPlace -> R.string.error_no_selected_place
        TodayForecastResult.Error.Server -> R.string.error_server
        TodayForecastResult.Error.Throttle -> R.string.error_throttling
        TodayForecastResult.Error.Unknown -> R.string.error_unknown
    }
    return TodayUiState.Empty(TextResource.fromStringId(stringId))
}

private fun getDescription(
    precipitation: Precipitation,
    description: ForecastDescription,
    precipitationUnit: LengthUnit
): TextResource {
    return if (precipitation.description == PrecipitationDescription.NONE) {
        TextResource.fromStringId(description.toStringId())
    } else {
        TextResource.fromStringId(
            id = R.string.template_precipitation,
            TextResource.fromStringId(precipitation.description.toStringId()),
            getPrecipitation(precipitation, precipitationUnit)
        )
    }
}

private fun getNextPrecipitation(
    nextPrecipitation: NextPrecipitation?,
    unit: LengthUnit
): TextResource {
    return if (nextPrecipitation == null || nextPrecipitation.precipitation.description == PrecipitationDescription.NONE) {
        TextResource.fromStringId(R.string.precipitation_none)
    } else {
        TextResource.fromStringId(
            id = R.string.template_precipitation_at,
            TextResource.fromStringId(nextPrecipitation.precipitation.description.toStringId()),
            getPrecipitation(nextPrecipitation.precipitation, unit),
            getShortTime(nextPrecipitation.at)
        )
    }
}

private fun getLongTime(time: ZonedDateTime): TextResource = TextResource.fromEpochMillis(
    millis = time.toInstant().toEpochMilli(),
    flags = DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_TIME or DateUtils.FORMAT_ABBREV_TIME
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

private fun getWindLong(
    wind: Wind,
    unit: SpeedUnit
): TextResource = TextResource.fromStringId(
    id = R.string.template_wind,
    TextResource.fromStringId(wind.description.toStringId()),
    TextResource.fromStringId(wind.fromDirection.toCompassDirectionStringId()),
    getWind(wind, unit)
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
    precipitation: Precipitation,
    unit: LengthUnit
): TextResource = TextResource.fromStringId(
    id = when (unit) {
        LengthUnit.MM -> R.string.template_precipitation_mm
        LengthUnit.IN -> R.string.template_precipitation_in
    },
    TextResource.fromNumber(precipitation.amount.run {
        when (unit) {
            LengthUnit.MM -> millimeters
            LengthUnit.IN -> inches
        }
    }, decimalPlaces = 2)
)

private fun getHour(
    datum: SmallForecastDatum,
    temperatureUnit: TemperatureUnit,
    precipitationUnit: LengthUnit,
    windUnit: SpeedUnit
): TodayHour = TodayHour(
    time = getShortTime(datum.time),
    icon = datum.description.toDrawableId(),
    temperature = getTemperature(datum.temperature, temperatureUnit),
    precipitation = datum.precipitation
        .takeIf { it.description != PrecipitationDescription.NONE }
        ?.let { getPrecipitation(it, precipitationUnit) },
    wind = getWind(datum.wind, windUnit),
    windIconRotation = datum.wind.fromDirection.degrees.toFloat()
)