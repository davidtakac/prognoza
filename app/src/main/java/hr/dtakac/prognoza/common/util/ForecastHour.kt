package hr.dtakac.prognoza.common.util

import hr.dtakac.prognoza.database.entity.ForecastHour
import hr.dtakac.prognoza.forecast.uimodel.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async

fun ForecastHour.toHourUiModel() =
    HourUiModel(
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
        weatherIcon = WEATHER_ICONS[symbolCode],
        time = time,
        relativeHumidity = relativeHumidity,
        windFromCompassDirection = windFromDirection?.toCompassDirection(),
        pressure = pressure
    )

suspend fun List<ForecastHour>.toDayUiModel(coroutineScope: CoroutineScope): DayUiModel {
    val weatherIconAsync = coroutineScope.async { representativeWeatherIcon() }
    val lowTempAsync = coroutineScope.async { minTemperature() }
    val highTempAsync = coroutineScope.async { maxTemperature() }
    val precipitationAsync = coroutineScope.async { totalPrecipitationAmount() }
    val hourWithMaxWindSpeedAsync = coroutineScope.async { hourWithMaxWindSpeed() }
    val maxHumidityAsync = coroutineScope.async { maxHumidity() }
    val maxPressureAsync = coroutineScope.async { maxPressure() }
    return DayUiModel(
        time = get(0).time,
        representativeWeatherIcon = weatherIconAsync.await(),
        lowTemperature = lowTempAsync.await(),
        highTemperature = highTempAsync.await(),
        totalPrecipitationAmount = precipitationAsync.await(),
        maxWindSpeed = hourWithMaxWindSpeedAsync.await()?.windSpeed,
        windFromCompassDirection = hourWithMaxWindSpeedAsync.await()?.windFromDirection?.toCompassDirection(),
        maxHumidity = maxHumidityAsync.await(),
        maxPressure = maxPressureAsync.await()
    )
}

fun List<ForecastHour>.maxTemperature() = maxOf { it.temperature ?: Float.MIN_VALUE }

fun List<ForecastHour>.minTemperature() = minOf { it.temperature ?: Float.MAX_VALUE }

fun List<ForecastHour>.maxHumidity() = maxOf { it.relativeHumidity ?: Float.MIN_VALUE }

fun List<ForecastHour>.maxPressure() = maxOf { it.pressure ?: Float.MIN_VALUE }

fun List<ForecastHour>.representativeWeatherIcon(): RepresentativeWeatherIcon? {
    val eligibleSymbolCodes = filter { it.symbolCode != null }
        .map { it.symbolCode!! }
        .filter { it !in NIGHT_SYMBOL_CODES }
    val mostCommonSymbolCode = eligibleSymbolCodes.mostCommon()
    val weatherIcon = WEATHER_ICONS[mostCommonSymbolCode]
    return if (weatherIcon == null) {
        null
    } else {
        RepresentativeWeatherIcon(
            weatherIcon = weatherIcon,
            isMostly = eligibleSymbolCodes.any { it != mostCommonSymbolCode }
        )
    }
}

fun List<ForecastHour>.totalPrecipitationAmount() =
    sumOf { it.precipitationAmount?.toDouble() ?: 0.0 }.toFloat()

fun List<ForecastHour>.hourWithMaxWindSpeed() = maxWithOrNull { o1, o2 ->
    val difference = (o1.windSpeed ?: Float.MIN_VALUE) - (o2.windSpeed ?: Float.MIN_VALUE)
    when {
        difference < 0f -> -1
        difference > 0f -> 1
        else -> 0
    }
}