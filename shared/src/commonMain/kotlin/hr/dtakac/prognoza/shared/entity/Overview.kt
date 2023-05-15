package hr.dtakac.prognoza.shared.entity

data class Overview(
    val hours: OverviewHours,
    val days: OverviewDays
) {
    val temperature: Temperature = hours.first.temperature
    val minimumTemperature: Temperature = days.first.minimumTemperature
    val maximumTemperature: Temperature = days.first.maximumTemperature
    val wmoCode: Int = hours.first.wmoCode
}

data class OverviewHours(val hours: List<OverviewHour>) {
    init {
        if (hours.filterIsInstance<OverviewHour.Weather>().isEmpty()) {
            throw IllegalStateException("Hours must contain at least one Weather hour.")
        }
    }

    val first: OverviewHour.Weather = hours.filterIsInstance<OverviewHour.Weather>()[0]
    val hourOfWeatherChange: OverviewHour? =
        hours.filterIsInstance<OverviewHour.Weather>().let { weatherHours ->
            weatherHours.firstOrNull { hour ->
                hour.wmoCode != weatherHours.first().wmoCode
            }
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

class OverviewDays(val days: List<OverviewDay>) {
    init {
        if (days.isEmpty()) {
            throw IllegalStateException("Days must not be empty.")
        }
    }

    val first: OverviewDay = days[0]
    val minimumTemperature: Temperature = days.minOf { it.minimumTemperature }
    val maximumTemperature: Temperature = days.maxOf { it.maximumTemperature }
}

data class OverviewDay(
    val unixSecond: Long,
    val wmoCode: Int,
    val minimumTemperature: Temperature,
    val maximumTemperature: Temperature
)