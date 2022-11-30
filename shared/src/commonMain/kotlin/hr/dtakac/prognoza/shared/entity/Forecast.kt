package hr.dtakac.prognoza.shared.entity

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
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

        val days = groupDays(data, timeZone)
        current = getCurrent(datum = days.first().first())
        today = days
            .getOrElse(index = 0) { listOf() }
            .toMutableList()
            .drop(1) // First hour is displayed in current part
            .takeIf { it.isNotEmpty() }
            ?.let(::getToday)
        coming = days
            .drop(1) // First day is displayed in today part
            .takeIf { it.isNotEmpty() }
            ?.let { getComing(it, timeZone) }
    }

    private fun groupDays(
        data: List<ForecastDatum>,
        timeZone: TimeZone
    ): List<List<ForecastDatum>> {
        val days = data
            .groupBy { getLocalDateTime(it.startEpochMillis, timeZone).date }
            .values
            .toMutableList()
            .apply {
                if (first().any { getLocalDateTime(it.startEpochMillis, timeZone).hour < dayStartHour }) {
                    add(0, listOf())
                }
            }
            .toList()
        val resultDays = mutableListOf<MutableList<ForecastDatum>>(mutableListOf())
        var resultDayIndex = 0

        for (i in days.indices) {
            val currentDay = days[i]
            val nextDay = days.getOrNull(i + 1)

            resultDays[resultDayIndex].addAll(
                currentDay.filter {
                    getLocalDateTime(it.startEpochMillis, timeZone).hour >= dayStartHour
                }
            )
            if (nextDay != null) {
                resultDays[resultDayIndex].addAll(
                    nextDay.filter {
                        getLocalDateTime(it.startEpochMillis, timeZone).hour < dayStartHour
                    }
                )
                resultDays.add(mutableListOf())
                resultDayIndex++
            }
        }

        return resultDays.apply { removeAll { it.isEmpty() } }
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

    private fun getComing(listOfData: List<List<ForecastDatum>>, timeZone: TimeZone): List<Day> {
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
                },
                night = data.getRepresentativeDescription(hourSpan = night, timeZone),
                morning = data.getRepresentativeDescription(hourSpan = morning, timeZone),
                afternoon = data.getRepresentativeDescription(hourSpan = afternoon, timeZone),
                evening = data.getRepresentativeDescription(hourSpan = evening, timeZone)
            )
        }
    }

    private fun List<ForecastDatum>.getRepresentativeDescription(
        hourSpan: IntRange,
        timeZone: TimeZone
    ): Description? {
        return filter { getLocalDateTime(it.startEpochMillis, timeZone).hour in hourSpan }
            .map { it.description }
            .getMostCommon()
    }

    companion object {
        internal const val dayStartHour = 5
        private val morning = dayStartHour until dayStartHour + 6
        private val afternoon = morning.last until (morning.last + 6)
        private val evening = afternoon.last until (afternoon.last + 6)
        private val night = (dayStartHour - 6) until dayStartHour
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
    val night: Description?,
    val morning: Description?,
    val afternoon: Description?,
    val evening: Description?,
    val hours: List<HourlyDatum>
)

private fun getLocalDateTime(epochMillis: Long, timeZone: TimeZone): LocalDateTime {
    return Instant.fromEpochMilliseconds(epochMillis).toLocalDateTime(timeZone)
}

private fun <T> List<T>.getMostCommon(): T? {
    if (isEmpty()) return null
    val itemsToOccurrences = groupingBy { it }.eachCount()
    val occurrences = itemsToOccurrences.values.toList()

    return if (occurrences.all { it == occurrences[0] }) {
        itemsToOccurrences.keys.last()
    } else {
        itemsToOccurrences.maxBy { it.value }.key
    }
}