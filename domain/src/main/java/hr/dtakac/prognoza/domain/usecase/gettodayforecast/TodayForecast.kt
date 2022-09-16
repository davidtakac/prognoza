package hr.dtakac.prognoza.domain.usecase.gettodayforecast

import hr.dtakac.prognoza.entities.forecast.ForecastDatum
import hr.dtakac.prognoza.entities.forecast.ForecastDescription
import hr.dtakac.prognoza.entities.forecast.units.Length
import hr.dtakac.prognoza.entities.forecast.units.Temperature
import hr.dtakac.prognoza.entities.forecast.wind.Wind
import java.lang.IllegalStateException
import java.time.ZonedDateTime

class TodayForecast(data: List<ForecastDatum>) {
    init {
        if (data.isEmpty()) {
            throw IllegalStateException("Forecast data must not be empty.")
        }
    }

    val time: ZonedDateTime = data.first().start
    val temperature: Temperature = data.first().temperature
    val feelsLike: Temperature = data.first().feelsLike
    val wind: Wind = data.first().wind
    val description: ForecastDescription = data.first().description
    val precipitation: Length = data.first().precipitation

    val hourly: List<HourlyDatum> = data.drop(1).map { datum ->
        HourlyDatum(
            time = datum.start,
            description = datum.description,
            temperature = datum.temperature,
            precipitation = datum.precipitation,
            wind = datum.wind
        )
    }
    val highTemperature: Temperature = hourly.maxOf { it.temperature }
    val lowTemperature: Temperature = hourly.minOf { it.temperature }
}

data class HourlyDatum(
    val time: ZonedDateTime,
    val description: ForecastDescription,
    val temperature: Temperature,
    val precipitation: Length,
    val wind: Wind
)