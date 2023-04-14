package hr.dtakac.prognoza.shared.entity

data class Hour(
    val unixSecond: Long,
    val wmoCode: Byte,
    val temperature: Temperature,
    val precipitation: Precipitation,
    val probabilityOfPrecipitation: Percentage,
    val wind: Wind,
    val pressureAtSeaLevel: Pressure,
    val relativeHumidity: Percentage,
    val dewPoint: Temperature,
    val visibility: Length,
    val uvIndex: Byte,
    val day: Boolean,
    val feelsLike: Temperature
)