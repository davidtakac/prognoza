package hr.dtakac.prognoza.shared.entity

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class Forecast(
    data: List<ForecastDatum>,
    timeZone: TimeZone
) {
    val current: Current
    val today: Today?
    val coming: List<Day>?

    init {
        if (data.isEmpty()) {
            throw IllegalStateException("Forecast data must not be empty.")
        }

        current = getCurrent(data.first())
        val dataGroupedByDay = data
            .groupBy { Instant.fromEpochMilliseconds(it.startEpochMillis).toLocalDateTime(timeZone).date }
            .values
            .toList()

        val todayHours = dataGroupedByDay.getOrElse(index = 0) { listOf() }.toMutableList()
        val tomorrowHours = dataGroupedByDay.getOrElse(index = 1) { listOf() }
        if (todayHours.size <= 5 && tomorrowHours.isNotEmpty()) {
            // Overflow into next day if there are not many hours left in the day
            todayHours += tomorrowHours.take(n = 7)
        }
        today = todayHours.drop(1).takeIf { it.isNotEmpty() }?.let { getToday(it) }
        coming = dataGroupedByDay.drop(1).takeIf { it.isNotEmpty() }?.let { getComing(it) }
    }

    private fun getCurrent(datum: ForecastDatum): Current {
        return Current(
            epochMillis = datum.startEpochMillis,
            temperature = datum.temperature,
            feelsLike = datum.feelsLike,
            wind = datum.wind,
            description = datum.description,
            mood = datum.mood,
            precipitation = datum.precipitation
        )
    }

    private fun getToday(data: List<ForecastDatum>): Today {
        val hourly = data.map { datum ->
            HourlyDatum(
                epochMillis = datum.startEpochMillis,
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

    private fun getComing(listOfData: List<List<ForecastDatum>>): List<Day> {
        return listOfData.map { data ->
            Day(
                epochMillis = data.first().startEpochMillis,
                highTemperature = data.maxOf { it.temperature },
                lowTemperature = data.minOf { it.temperature },
                totalPrecipitation = Length(
                    data.sumOf { it.precipitation.millimetre },
                    LengthUnit.MILLIMETRE
                ),
                hours = data.map { datum ->
                    HourlyDatum(
                        epochMillis = datum.startEpochMillis,
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
    val epochMillis: Long,
    val temperature: Temperature,
    val feelsLike: Temperature,
    val wind: Wind,
    val description: Description,
    val mood: Mood,
    val precipitation: Length,
)

data class HourlyDatum(
    val epochMillis: Long,
    val description: Description,
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
    val epochMillis: Long,
    val highTemperature: Temperature,
    val lowTemperature: Temperature,
    val totalPrecipitation: Length,
    val hours: List<HourlyDatum>
)