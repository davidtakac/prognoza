package hr.dtakac.prognoza.shared.entity

data class Day(
    val unixSecond: Long,
    val wmoCode: Byte,
    val sunriseUnixSecond: Long?,
    val sunsetUnixSecond: Long?,
    val minimumTemperature: Temperature,
    val maximumTemperature: Temperature,
    val totalPrecipitation: Precipitation,
    val maximumProbabilityOfPrecipitation: Precipitation,
    val maximumWind: Wind,
    val maximumUvIndex: Byte
)