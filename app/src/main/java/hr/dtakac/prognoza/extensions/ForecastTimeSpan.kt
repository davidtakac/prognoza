package hr.dtakac.prognoza.extensions

import ca.rmen.sunrisesunset.SunriseSunset
import hr.dtakac.prognoza.dbmodel.ForecastMeta
import hr.dtakac.prognoza.dbmodel.ForecastTimeSpan
import hr.dtakac.prognoza.repomodel.*
import hr.dtakac.prognoza.uimodel.MeasurementUnit
import hr.dtakac.prognoza.uimodel.NIGHT_SYMBOL_CODES
import hr.dtakac.prognoza.uimodel.RepresentativeWeatherDescription
import hr.dtakac.prognoza.uimodel.WEATHER_ICONS
import hr.dtakac.prognoza.uimodel.cell.DayUiModel
import hr.dtakac.prognoza.uimodel.cell.HourUiModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import java.util.*

fun ForecastTimeSpan.toHourUiModel(unit: MeasurementUnit) =
    HourUiModel(
        id = "$placeId-$startTime",
        temperature = instantTemperature,
        feelsLike = if (instantTemperature == null) {
            null
        } else {
            calculateFeelsLikeTemperature(
                instantTemperature,
                instantWindSpeed,
                instantRelativeHumidity
            )
        },
        precipitationAmount = precipitationAmount,
        windSpeed = instantWindSpeed,
        weatherDescription = WEATHER_ICONS[symbolCode],
        time = startTime,
        relativeHumidity = instantRelativeHumidity,
        windFromCompassDirection = instantWindFromDirection?.toCompassDirection(),
        airPressureAtSeaLevel = instantAirPressureAtSeaLevel,
        displayDataInUnit = unit
    )

suspend fun List<ForecastTimeSpan>.toDayUiModel(
    coroutineScope: CoroutineScope,
    unit: MeasurementUnit
): DayUiModel {
    val weatherIconAsync = coroutineScope.async { representativeWeatherIcon() }
    val lowTempAsync = coroutineScope.async { lowestTemperature() }
    val highTempAsync = coroutineScope.async { highestTemperature() }
    val precipitationAsync = coroutineScope.async { totalPrecipitationAmount() }
    val hourWithMaxWindSpeedAsync = coroutineScope.async { hourWithMaxWindSpeed() }
    val maxHumidityAsync = coroutineScope.async { highestRelativeHumidity() }
    val maxPressureAsync = coroutineScope.async { highestPressure() }
    val firstHour = get(0)
    return DayUiModel(
        id = "${firstHour.placeId}-${firstHour.startTime}",
        time = firstHour.startTime,
        representativeWeatherDescription = weatherIconAsync.await(),
        lowTemperature = lowTempAsync.await(),
        highTemperature = highTempAsync.await(),
        totalPrecipitationAmount = precipitationAsync.await(),
        maxWindSpeed = hourWithMaxWindSpeedAsync.await()?.instantWindSpeed,
        windFromCompassDirection = hourWithMaxWindSpeedAsync.await()?.instantWindFromDirection?.toCompassDirection(),
        maxHumidity = maxHumidityAsync.await(),
        maxPressure = maxPressureAsync.await(),
        displayDataInUnit = unit
    )
}

fun List<ForecastTimeSpan>.toForecastResult(
    meta: ForecastMeta?,
    error: ForecastError?
): ForecastResult {
    return if (isNullOrEmpty()) {
        Empty(error)
    } else {
        val success = Success(meta, this)
        if (error == null) {
            success
        } else {
            CachedSuccess(success, error)
        }
    }
}

fun List<ForecastTimeSpan>.highestTemperature(): Float? {
    val max = maxOf { it.airTemperatureMax ?: Float.MIN_VALUE }
    return if (max == Float.MIN_VALUE) {
        null
    } else {
        max
    }
}

fun List<ForecastTimeSpan>.lowestTemperature(): Float? {
    val min = minOf { it.airTemperatureMin ?: Float.MAX_VALUE }
    return if (min == Float.MAX_VALUE) {
        null
    } else {
        min
    }
}

fun List<ForecastTimeSpan>.highestRelativeHumidity(): Float? {
    val max = maxOf { it.instantRelativeHumidity ?: Float.MIN_VALUE }
    return if (max == Float.MIN_VALUE) {
        null
    } else {
        max
    }
}

fun List<ForecastTimeSpan>.highestPressure(): Float? {
    val max = maxOf { it.instantAirPressureAtSeaLevel ?: Float.MIN_VALUE }
    return if (max == Float.MIN_VALUE) {
        null
    } else {
        max
    }
}

fun List<ForecastTimeSpan>.representativeWeatherIcon(): RepresentativeWeatherDescription? {
    val eligibleSymbolCodes = filter {
        SunriseSunset.isDay(GregorianCalendar.from(it.startTime), 45.0, 18.0)
    }
    val mostCommonSymbolCode = eligibleSymbolCodes.mostCommon()
    val weatherIcon = WEATHER_ICONS[mostCommonSymbolCode]
    return if (weatherIcon == null) {
        null
    } else {
        RepresentativeWeatherDescription(
            weatherDescription = weatherIcon,
            isMostly = eligibleSymbolCodes.any { it != mostCommonSymbolCode }
        )
    }
}

fun List<ForecastTimeSpan>.totalPrecipitationAmount(): Float {
    return sumOf { it.precipitationAmount?.toDouble() ?: 0.0 }.toFloat()
}

fun List<ForecastTimeSpan>.hourWithMaxWindSpeed() = maxWithOrNull { o1, o2 ->
    val difference =
        (o1.instantWindSpeed ?: Float.MIN_VALUE) - (o2.instantWindSpeed ?: Float.MIN_VALUE)
    when {
        difference < 0f -> -1
        difference > 0f -> 1
        else -> 0
    }
}