package hr.dtakac.prognoza.core.utils

import ca.rmen.sunrisesunset.SunriseSunset
import hr.dtakac.prognoza.core.model.database.ForecastMeta
import hr.dtakac.prognoza.core.model.database.ForecastTimeSpan
import hr.dtakac.prognoza.core.model.database.Place
import hr.dtakac.prognoza.core.model.repository.*
import hr.dtakac.prognoza.core.model.ui.RepresentativeWeatherDescription
import hr.dtakac.prognoza.core.model.ui.WEATHER_ICONS
import java.util.*

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

fun List<ForecastTimeSpan>.highestTemperature(): Double? {
    val max = maxOf { it.airTemperatureMax ?: Double.MIN_VALUE }
    return if (max == Double.MIN_VALUE) {
        null
    } else {
        max
    }
}

fun List<ForecastTimeSpan>.lowestTemperature(): Double? {
    val min = minOf { it.airTemperatureMin ?: Double.MAX_VALUE }
    return if (min == Double.MAX_VALUE) {
        null
    } else {
        min
    }
}

fun List<ForecastTimeSpan>.highestRelativeHumidity(): Double? {
    val max = maxOf { it.instantRelativeHumidity ?: Double.MIN_VALUE }
    return if (max == Double.MIN_VALUE) {
        null
    } else {
        max
    }
}

fun List<ForecastTimeSpan>.highestPressure(): Double? {
    val max = maxOf { it.instantAirPressureAtSeaLevel ?: Double.MIN_VALUE }
    return if (max == Double.MIN_VALUE) {
        null
    } else {
        max
    }
}

fun List<ForecastTimeSpan>.representativeWeatherIcon(place: Place): RepresentativeWeatherDescription? {
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

fun List<ForecastTimeSpan>.totalPrecipitationAmount(): Double {
    return sumOf { it.precipitationAmount ?: 0.0 }
}

fun List<ForecastTimeSpan>.hourWithMaxWindSpeed() = maxWithOrNull { o1, o2 ->
    val difference =
        (o1.instantWindSpeed ?: Double.MIN_VALUE) - (o2.instantWindSpeed ?: Double.MIN_VALUE)
    when {
        difference < 0 -> -1
        difference > 0 -> 1
        else -> 0
    }
}