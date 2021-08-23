package hr.dtakac.prognoza.extensions

import hr.dtakac.prognoza.dbmodel.ForecastHour
import hr.dtakac.prognoza.dbmodel.ForecastMeta
import hr.dtakac.prognoza.repomodel.*
import hr.dtakac.prognoza.uimodel.MeasurementUnit
import hr.dtakac.prognoza.uimodel.NIGHT_SYMBOL_CODES
import hr.dtakac.prognoza.uimodel.RepresentativeWeatherDescription
import hr.dtakac.prognoza.uimodel.WEATHER_ICONS
import hr.dtakac.prognoza.uimodel.cell.DayCellModel
import hr.dtakac.prognoza.uimodel.cell.HourCellModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async

fun ForecastHour.toHourUiModel(unit: MeasurementUnit) =
    HourCellModel(
        id = "$placeId-$time",
        temperature = temperature,
        feelsLike = if (temperature == null) {
            null
        } else {
            calculateFeelsLikeTemperature(temperature, windSpeed, relativeHumidity)
        },
        precipitation = precipitationAmount,
        windSpeed = windSpeed,
        windIconRotation = windFromDirection?.plus(180f),
        weatherDescription = WEATHER_ICONS[symbolCode],
        time = time,
        relativeHumidity = relativeHumidity,
        windFromCompassDirection = windFromDirection?.toCompassDirection(),
        pressure = pressure,
        displayDataInUnit = unit
    )

suspend fun List<ForecastHour>.toDayUiModel(
    coroutineScope: CoroutineScope,
    unit: MeasurementUnit
): DayCellModel {
    val weatherIconAsync = coroutineScope.async { representativeWeatherIcon() }
    val lowTempAsync = coroutineScope.async { minTemperature() }
    val highTempAsync = coroutineScope.async { maxTemperature() }
    val precipitationAsync = coroutineScope.async { totalPrecipitationAmount() }
    val hourWithMaxWindSpeedAsync = coroutineScope.async { hourWithMaxWindSpeed() }
    val maxHumidityAsync = coroutineScope.async { maxHumidity() }
    val maxPressureAsync = coroutineScope.async { maxPressure() }
    val firstHour = get(0)
    return DayCellModel(
        id = "${firstHour.placeId}-${firstHour.time}",
        time = firstHour.time,
        representativeWeatherDescription = weatherIconAsync.await(),
        lowTemperature = lowTempAsync.await(),
        highTemperature = highTempAsync.await(),
        totalPrecipitationAmount = precipitationAsync.await(),
        maxWindSpeed = hourWithMaxWindSpeedAsync.await()?.windSpeed,
        windFromCompassDirection = hourWithMaxWindSpeedAsync.await()?.windFromDirection?.toCompassDirection(),
        maxHumidity = maxHumidityAsync.await(),
        maxPressure = maxPressureAsync.await(),
        displayDataInUnit = unit
    )
}

fun List<ForecastHour>.toForecastResult(
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

fun List<ForecastHour>.maxTemperature(): Float? {
    val max = maxOf { it.temperature ?: Float.MIN_VALUE }
    return if (max == Float.MIN_VALUE) {
        null
    } else {
        max
    }
}

fun List<ForecastHour>.minTemperature(): Float? {
    val min = minOf { it.temperature ?: Float.MAX_VALUE }
    return if (min == Float.MAX_VALUE) {
        null
    } else {
        min
    }
}

fun List<ForecastHour>.maxHumidity(): Float? {
    val max = maxOf { it.relativeHumidity ?: Float.MIN_VALUE }
    return if (max == Float.MIN_VALUE) {
        null
    } else {
        max
    }
}

fun List<ForecastHour>.maxPressure(): Float? {
    val max = maxOf { it.pressure ?: Float.MIN_VALUE }
    return if (max == Float.MIN_VALUE) {
        null
    } else {
        max
    }
}

fun List<ForecastHour>.representativeWeatherIcon(): RepresentativeWeatherDescription? {
    val eligibleSymbolCodes = filter { it.symbolCode != null }
        .map { it.symbolCode!! }
        .filter { it !in NIGHT_SYMBOL_CODES }
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

fun List<ForecastHour>.totalPrecipitationAmount(): Float {
    return sumOf { it.precipitationAmount?.toDouble() ?: 0.0 }.toFloat()
}

fun List<ForecastHour>.hourWithMaxWindSpeed() = maxWithOrNull { o1, o2 ->
    val difference = (o1.windSpeed ?: Float.MIN_VALUE) - (o2.windSpeed ?: Float.MIN_VALUE)
    when {
        difference < 0f -> -1
        difference > 0f -> 1
        else -> 0
    }
}