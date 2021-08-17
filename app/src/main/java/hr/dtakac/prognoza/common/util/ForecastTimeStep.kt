package hr.dtakac.prognoza.common.util

import hr.dtakac.prognoza.api.ForecastTimeStep
import hr.dtakac.prognoza.database.entity.ForecastHour
import java.time.ZonedDateTime

fun ForecastTimeStep.toForecastHour(placeId: String): ForecastHour {
    return ForecastHour(
        time = ZonedDateTime.parse(time),
        placeId = placeId,
        temperature = data?.instant?.data?.airTemperature,
        symbolCode = data?.findSymbolCode(),
        precipitationProbability = data?.findPrecipitationProbability(),
        precipitationAmount = data?.findPrecipitationAmount(),
        windSpeed = data?.instant?.data?.windSpeed,
        windFromDirection = data?.instant?.data?.windFromDirection,
        relativeHumidity = data?.instant?.data?.relativeHumidity,
        pressure = data?.instant?.data?.airPressureAtSeaLevel
    )
}