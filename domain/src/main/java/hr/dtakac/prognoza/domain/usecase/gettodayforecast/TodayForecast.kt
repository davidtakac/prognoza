package hr.dtakac.prognoza.domain.usecase.gettodayforecast

import hr.dtakac.prognoza.entities.forecast.ForecastDatum
import hr.dtakac.prognoza.entities.forecast.ForecastDescription
import hr.dtakac.prognoza.entities.forecast.precipitation.Precipitation
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

    val airTemperature: Temperature = data.first().temperature

    val feelsLikeTemperature: Temperature = data.first().feelsLike

    val wind: Wind = data.first().wind

    val description: ForecastDescription = data.first().description

    val highTemperature: Temperature = data.maxOf { it.temperature }

    val lowTemperature: Temperature = data.minOf { it.temperature }

    val dailyPrecipitation: DailyPrecipitation? = data
        .firstOrNull { it.precipitation.amount.millimeters > 0 }
        ?.let { datum ->
            DailyPrecipitation(
                precipitation = datum.precipitation,
                at = datum.start
            )
        }

    val smallData: List<SmallForecastDatum> = data.drop(1).map { datum ->
        SmallForecastDatum(
            time = datum.start,
            description = datum.description,
            temperature = datum.temperature,
            precipitation = datum.precipitation
        )
    }
}

data class DailyPrecipitation(
    val precipitation: Precipitation,
    val at: ZonedDateTime
)

data class SmallForecastDatum(
    val time: ZonedDateTime,
    val description: ForecastDescription,
    val temperature: Temperature,
    val precipitation: Precipitation
)