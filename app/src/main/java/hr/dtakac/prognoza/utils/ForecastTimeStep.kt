package hr.dtakac.prognoza.utils

import hr.dtakac.prognoza.apimodel.ForecastTimeStep
import hr.dtakac.prognoza.dbmodel.ForecastTimeSpan
import java.time.ZonedDateTime

fun ForecastTimeStep.toForecastTimeSpan(
    placeId: String,
    next: ForecastTimeStep?
): ForecastTimeSpan {
    val startTime = ZonedDateTime.parse(time)
    val endTime = next?.time?.let { ZonedDateTime.parse(it) }
    val priorityDetails = when (endTime) {
        startTime.plusHours(1) -> {
            data?.next1Hours
        }
        startTime.plusHours(6) -> {
            data?.next6Hours
        }
        startTime.plusHours(12) -> {
            data?.next12Hours
        }
        else -> {
            null
        }
    }
    val instantAirTemperature = data?.instant?.data?.airTemperature
    return ForecastTimeSpan(
        startTime = startTime,
        endTime = endTime,
        placeId = placeId,
        instantTemperature = instantAirTemperature,
        symbolCode = priorityDetails?.summary?.symbolCode,
        precipitationProbability = priorityDetails?.data?.probabilityOfPrecipitation,
        precipitationAmount = priorityDetails?.data?.precipitationAmount,
        instantWindSpeed = data?.instant?.data?.windSpeed,
        instantWindFromDirection = data?.instant?.data?.windFromDirection,
        instantRelativeHumidity = data?.instant?.data?.relativeHumidity,
        instantAirPressureAtSeaLevel = data?.instant?.data?.airPressureAtSeaLevel,
        airTemperatureMax = priorityDetails?.data?.airTemperatureMax ?: instantAirTemperature,
        airTemperatureMin = priorityDetails?.data?.airTemperatureMin ?: instantAirTemperature
    )
}