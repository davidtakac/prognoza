package hr.dtakac.prognoza.domain.usecase.gettodayforecast

import hr.dtakac.prognoza.entities.forecast.ForecastDatum
import hr.dtakac.prognoza.entities.forecast.ForecastDescription
import hr.dtakac.prognoza.entities.forecast.units.Length
import hr.dtakac.prognoza.entities.forecast.units.Temperature
import hr.dtakac.prognoza.entities.forecast.wind.Wind
import java.lang.IllegalStateException
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime

class Forecast(data: List<ForecastDatum>) {
    init {
        if (data.isEmpty()) {
            throw IllegalStateException("Forecast data must not be empty.")
        }
    }

    private val dataGroupedByDay: Map<LocalDate, List<ForecastDatum>> = data.groupBy { datum ->
        datum.start.withZoneSameInstant(ZoneId.systemDefault()).toLocalDate()
    }

    val current: Current = resolveCurrent(datum = dataGroupedByDay.values.first().first())
    val today: Day = resolveToday(data = dataGroupedByDay.values.first())
    val coming: List<Day> = resolveComing(listOfData = dataGroupedByDay.values.drop(1))

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

    private fun resolveToday(data: List<ForecastDatum>): Day {
        val hourly = data.drop(1).map { datum ->
            HourlyDatum(
                dateTime = datum.start,
                description = datum.description,
                temperature = datum.temperature,
                precipitation = datum.precipitation,
                wind = datum.wind
            )
        }
        return Day(
            dateTime = data.first().start,
            hourly = hourly,
            highTemperature = hourly.maxOf { it.temperature },
            lowTemperature = hourly.minOf { it.temperature },
        )
    }

    private fun resolveComing(listOfData: List<List<ForecastDatum>>): List<Day> {
        return listOfData.map { data ->
            val hourly = data.map { datum ->
                HourlyDatum(
                    dateTime = datum.start,
                    description = datum.description,
                    temperature = datum.temperature,
                    precipitation = datum.precipitation,
                    wind = datum.wind
                )
            }
            Day(
                dateTime = data.first().start,
                hourly = hourly,
                highTemperature = hourly.maxOf { it.temperature },
                lowTemperature = hourly.minOf { it.temperature },
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

data class Day(
    val dateTime: ZonedDateTime,
    val highTemperature: Temperature,
    val lowTemperature: Temperature,
    val hourly: List<HourlyDatum>
)