package hr.dtakac.prognoza.forecast.mapping

import hr.dtakac.prognoza.core.mapping.*
import hr.dtakac.prognoza.core.model.database.ForecastTimeSpan
import hr.dtakac.prognoza.core.model.database.Place
import hr.dtakac.prognoza.core.model.ui.WEATHER_ICONS
import hr.dtakac.prognoza.core.utils.*
import hr.dtakac.prognoza.forecast.model.DayUiModel
import hr.dtakac.prognoza.forecast.model.HourUiModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async

suspend fun List<ForecastTimeSpan>.toDayUiModel(
    coroutineScope: CoroutineScope,
    place: Place
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
        maxPressure = maxPressureAsync.await()
    )
}

fun ForecastTimeSpan.toHourUiModel() =
    HourUiModel(
        id = "$placeId-$startTime",
        temperature = instantTemperature,
        feelsLike = instantTemperature?.let {
            calculateFeelsLikeTemperature(
                it, instantWindSpeed,
                instantRelativeHumidity
            )
        },
        precipitationAmount = precipitationAmount,
        windSpeed = instantWindSpeed,
        weatherDescription = WEATHER_ICONS[symbolCode],
        time = startTime,
        relativeHumidity = instantRelativeHumidity,
        windFromCompassDirection = instantWindFromDirection?.toCompassDirection(),
        airPressureAtSeaLevel = instantAirPressureAtSeaLevel
    )