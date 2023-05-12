package hr.dtakac.prognoza.shared.entity

import kotlinx.datetime.Clock

class Overview(private val forecast: Forecast) {
    val temperature: Temperature = forecast.hours[0].temperature
    val minimumTemperature: Temperature = forecast.days[0].minimumTemperature
    val maximumTemperature: Temperature = forecast.days[0].maximumTemperature
    val wmoCode: Int = forecast.hours[0].wmoCode
    val hours: OverviewHours = OverviewHours(forecast)
    val days: OverviewDays = OverviewDays(forecast.days)
}

class OverviewHours(private val forecast: Forecast) {
    val hours: List<OverviewHour> = buildOverviewHours()
    val hourWhenWeatherChanges: OverviewHour? = findHourWhenWeatherChanges()

    private fun buildOverviewHours(): List<OverviewHour> = buildList {
        val hours = forecast.hours
            .filter { it.unixSecond >= Clock.System.now().epochSeconds }
            .take(24)
            .map {
                OverviewHour.Weather(
                    unixSecond = it.unixSecond,
                    temperature = it.temperature,
                    probabilityOfPrecipitation = it.probabilityOfPrecipitation,
                    wmoCode = it.wmoCode
                )
            }

        val sunrises = forecast.days
            .mapNotNull { it.sunriseUnixSecond }
            .filter { it in hours.first().unixSecond..hours.last().unixSecond }
            .map { OverviewHour.Sunrise(it) }

        val sunsets = forecast.days
            .mapNotNull { it.sunsetUnixSecond }
            .filter { it in hours.first().unixSecond..hours.last().unixSecond }
            .map { OverviewHour.Sunset(it) }

        addAll(hours)
        addAll(sunrises)
        addAll(sunsets)
        sortBy { it.unixSecond }
    }

    private fun findHourWhenWeatherChanges(): OverviewHour.Weather? {
        val weatherHours = hours.filterIsInstance<OverviewHour.Weather>()
        return weatherHours.firstOrNull { it.wmoCode != weatherHours.first().wmoCode }
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