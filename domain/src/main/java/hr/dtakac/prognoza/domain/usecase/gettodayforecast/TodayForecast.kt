package hr.dtakac.prognoza.domain.usecase.gettodayforecast

import hr.dtakac.prognoza.entities.forecast.ForecastDatum
import hr.dtakac.prognoza.entities.forecast.ForecastDescription
import hr.dtakac.prognoza.entities.forecast.precipitation.Precipitation
import hr.dtakac.prognoza.entities.forecast.precipitation.PrecipitationDescription
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

    val now: ZonedDateTime = data.first().start
    val temperatureNow: Temperature = data.first().temperature
    val feelsLikeNow: Temperature = data.first().feelsLike
    val windNow: Wind = data.first().wind
    val descriptionNow: ForecastDescription = data.first().description
    val precipitationNow: Precipitation = data.first().precipitation

    val restOfDayData: List<SmallForecastDatum> = data.drop(1).map { datum ->
        SmallForecastDatum(
            time = datum.start,
            description = datum.description,
            temperature = datum.temperature,
            precipitation = datum.precipitation,
            wind = datum.wind
        )
    }
    val highTemperature: Temperature = restOfDayData.maxOf { it.temperature }
    val lowTemperature: Temperature = restOfDayData.minOf { it.temperature }
    val nextPrecipitation: NextPrecipitation? = restOfDayData
        .firstOrNull { it.precipitation.description != PrecipitationDescription.NONE }
        ?.let { NextPrecipitation(at = it.time, precipitation = it.precipitation) }
}

data class SmallForecastDatum(
    val time: ZonedDateTime,
    val description: ForecastDescription,
    val temperature: Temperature,
    val precipitation: Precipitation,
    val wind: Wind
)

data class NextPrecipitation(
    val at: ZonedDateTime,
    val precipitation: Precipitation
)