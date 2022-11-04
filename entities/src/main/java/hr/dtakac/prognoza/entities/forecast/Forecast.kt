package hr.dtakac.prognoza.entities.forecast

import hr.dtakac.prognoza.entities.forecast.units.Length
import hr.dtakac.prognoza.entities.forecast.units.LengthUnit
import hr.dtakac.prognoza.entities.forecast.units.Temperature
import java.lang.IllegalStateException
import java.time.ZoneId
import java.time.ZonedDateTime

class Forecast(data: List<ForecastDatum>) {
    val current: Current
    val today: Today?
    val coming: List<Day>?

    init {
        if (data.isEmpty()) {
            throw IllegalStateException("Forecast data must not be empty.")
        }

        current = resolveCurrent(data.first())
        val dataGroupedByDay = data.groupBy { datum ->
            datum.start.withZoneSameInstant(ZoneId.systemDefault()).toLocalDate()
        }.values.toList()

        val todayHours = dataGroupedByDay.getOrElse(index = 0) { listOf() }.toMutableList()
        val tomorrowHours = dataGroupedByDay.getOrElse(index = 1) { listOf() }
        if (todayHours.size <= 5 && tomorrowHours.isNotEmpty()) {
            // Overflow into next day if there are not many hours left in the day
            todayHours += tomorrowHours.take(n = 7)
        }
        today = todayHours.drop(n = 1).takeIf { it.isNotEmpty() }?.let { resolveToday(it) }
        coming = dataGroupedByDay.drop(n = 1).takeIf { it.isNotEmpty() }?.let { resolveComing(it) }
    }

    private fun resolveCurrent(datum: ForecastDatum): Current {
        return Current(
            dateTime = datum.start,
            temperature = datum.temperature,
            feelsLike = datum.feelsLike,
            wind = datum.wind,
            description = datum.description,
            precipitation = datum.precipitation
        )
    }

    private fun resolveToday(data: List<ForecastDatum>): Today {
        val hourly = data.map { datum ->
            HourlyDatum(
                dateTime = datum.start,
                description = datum.description,
                temperature = datum.temperature,
                precipitation = datum.precipitation,
                wind = datum.wind
            )
        }
        return Today(
            hourly = hourly,
            highTemperature = hourly.maxOf { it.temperature },
            lowTemperature = hourly.minOf { it.temperature },
        )
    }

    private fun resolveComing(listOfData: List<List<ForecastDatum>>): List<Day> {
        return listOfData.map { data ->
            Day(
                dateTime = data.first().start,
                highTemperature = data.maxOf { it.temperature },
                lowTemperature = data.minOf { it.temperature },
                totalPrecipitation = Length(
                    data.sumOf { it.precipitation.millimeters },
                    LengthUnit.MM
                ),
                hours = data.map { datum ->
                    HourlyDatum(
                        dateTime = datum.start,
                        description = datum.description,
                        temperature = datum.temperature,
                        precipitation = datum.precipitation,
                        wind = datum.wind
                    )
                }
            )
        }
    }
}

data class Current(
    val dateTime: ZonedDateTime,
    val temperature: Temperature,
    val feelsLike: Temperature,
    val wind: Wind,
    val description: ForecastDescription,
    val precipitation: Length,
)

data class HourlyDatum(
    val dateTime: ZonedDateTime,
    val description: ForecastDescription,
    val temperature: Temperature,
    val precipitation: Length,
    val wind: Wind
)

data class Today(
    val highTemperature: Temperature,
    val lowTemperature: Temperature,
    val hourly: List<HourlyDatum>
)

data class Day(
    val dateTime: ZonedDateTime,
    val highTemperature: Temperature,
    val lowTemperature: Temperature,
    val totalPrecipitation: Length,
    val hours: List<HourlyDatum>
)