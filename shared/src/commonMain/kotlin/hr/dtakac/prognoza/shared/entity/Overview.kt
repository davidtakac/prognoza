package hr.dtakac.prognoza.shared.entity

data class Overview(
    val temperature: Temperature,
    val minimumTemperature: Temperature,
    val maximumTemperature: Temperature,
    val wmoCode: Int,
    val hours: OverviewHours,
    val days: OverviewDays
)

data class OverviewHours(val hours: List<OverviewHour>) {
    val hourWhenWeatherChanges: OverviewHour? =
        hours
            .filterIsInstance<OverviewHour.Weather>()
            .let { weatherHours ->
                weatherHours.firstOrNull { it.wmoCode != weatherHours.first().wmoCode }
            }
}

sealed interface OverviewHour {
    val unixSecond: Long

    data class Weather(
        override val unixSecond: Long,
        val temperature: Temperature,
        val probabilityOfPrecipitation: Percentage,
        val wmoCode: Int
    ) : OverviewHour

    data class Sunrise(override val unixSecond: Long) : OverviewHour

    data class Sunset(override val unixSecond: Long) : OverviewHour
}

class OverviewDays(days: List<Day>) {
    val days: List<OverviewDay> = days.map {
        OverviewDay(
            unixSecond = it.unixSecond,
            wmoCode = it.wmoCode,
            minimumTemperature = it.minimumTemperature,
            maximumTemperature = it.maximumTemperature
        )
    }
    val minimumTemperature: Temperature = days.minOf { it.minimumTemperature }
    val maximumTemperature: Temperature = days.maxOf { it.maximumTemperature }
}

data class OverviewDay(
    val unixSecond: Long,
    val wmoCode: Int,
    val minimumTemperature: Temperature,
    val maximumTemperature: Temperature
)