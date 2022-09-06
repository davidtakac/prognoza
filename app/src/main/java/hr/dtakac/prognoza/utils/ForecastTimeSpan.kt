package hr.dtakac.prognoza.utils

import ca.rmen.sunrisesunset.SunriseSunset
import hr.dtakac.prognoza.data.database.forecast.ForecastMeta
import hr.dtakac.prognoza.data.database.forecast.ForecastTimeSpan
import hr.dtakac.prognoza.data.database.place.Place
import hr.dtakac.prognoza.repomodel.*
import hr.dtakac.prognoza.uimodel.MeasurementUnit
import hr.dtakac.prognoza.uimodel.RepresentativeWeatherDescription
import hr.dtakac.prognoza.uimodel.WEATHER_ICONS
import hr.dtakac.prognoza.uimodel.cell.DayUiModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import java.util.*

fun hr.dtakac.prognoza.data.database.forecast.ForecastTimeSpan.toHourUiModel(unit: MeasurementUnit) =
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

suspend fun List<hr.dtakac.prognoza.data.database.forecast.ForecastTimeSpan>.toDayUiModel(
    coroutineScope: CoroutineScope,
    unit: MeasurementUnit,
    place: hr.dtakac.prognoza.data.database.place.Place
): DayUiModel {
    val weatherIconAsync = coroutineScope.async { representativeWeatherIcon(place) }
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

fun List<hr.dtakac.prognoza.data.database.forecast.ForecastTimeSpan>.toForecastResult(
    meta: hr.dtakac.prognoza.data.database.forecast.ForecastMeta?,
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

fun List<hr.dtakac.prognoza.data.database.forecast.ForecastTimeSpan>.highestTemperature(): Double? {
    val max = maxOf { it.airTemperatureMax ?: Double.MIN_VALUE }
    return if (max == Double.MIN_VALUE) {
        null
    } else {
        max
    }
}

fun List<hr.dtakac.prognoza.data.database.forecast.ForecastTimeSpan>.lowestTemperature(): Double? {
    val min = minOf { it.airTemperatureMin ?: Double.MAX_VALUE }
    return if (min == Double.MAX_VALUE) {
        null
    } else {
        min
    }
}

fun List<hr.dtakac.prognoza.data.database.forecast.ForecastTimeSpan>.highestRelativeHumidity(): Double? {
    val max = maxOf { it.instantRelativeHumidity ?: Double.MIN_VALUE }
    return if (max == Double.MIN_VALUE) {
        null
    } else {
        max
    }
}

fun List<hr.dtakac.prognoza.data.database.forecast.ForecastTimeSpan>.highestPressure(): Double? {
    val max = maxOf { it.instantAirPressureAtSeaLevel ?: Double.MIN_VALUE }
    return if (max == Double.MIN_VALUE) {
        null
    } else {
        max
    }
}

fun List<hr.dtakac.prognoza.data.database.forecast.ForecastTimeSpan>.representativeWeatherIcon(place: hr.dtakac.prognoza.data.database.place.Place): RepresentativeWeatherDescription? {
    val timeSpansGroupedByIsDay = groupBy {
        SunriseSunset.isDay(
            GregorianCalendar.from(it.startTime),
            place.latitude,
            place.longitude
        )
    }
    val dayTimeSpans = timeSpansGroupedByIsDay[true] ?: listOf()
    val nightTimeSpans = timeSpansGroupedByIsDay[false] ?: listOf()
    val eligibleSymbolCodes = if (dayTimeSpans.isEmpty()) {
        nightTimeSpans
    } else {
        dayTimeSpans
    }.mapNotNull { it.symbolCode }
    val representativeSymbolCode = eligibleSymbolCodes.mostCommon()
    val weatherIcon = WEATHER_ICONS[representativeSymbolCode]
    return if (weatherIcon == null) {
        null
    } else {
        RepresentativeWeatherDescription(
            weatherDescription = weatherIcon,
            isMostly = eligibleSymbolCodes.any { it != representativeSymbolCode }
        )
    }
}

fun List<hr.dtakac.prognoza.data.database.forecast.ForecastTimeSpan>.totalPrecipitationAmount(): Double {
    return sumOf { it.precipitationAmount ?: 0.0 }
}

fun List<hr.dtakac.prognoza.data.database.forecast.ForecastTimeSpan>.hourWithMaxWindSpeed() = maxWithOrNull { o1, o2 ->
    val difference =
        (o1.instantWindSpeed ?: Double.MIN_VALUE) - (o2.instantWindSpeed ?: Double.MIN_VALUE)
    when {
        difference < 0 -> -1
        difference > 0 -> 1
        else -> 0
    }
}