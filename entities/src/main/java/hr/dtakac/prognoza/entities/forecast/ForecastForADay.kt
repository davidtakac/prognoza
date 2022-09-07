package hr.dtakac.prognoza.entities.forecast

import hr.dtakac.prognoza.entities.forecast.units.Length
import hr.dtakac.prognoza.entities.forecast.units.Temperature
import hr.dtakac.prognoza.entities.forecast.wind.Wind
import java.lang.IllegalStateException
import java.time.Duration
import java.time.ZonedDateTime

class ForecastForADay(data: List<ForecastDatum>) {
    init {
        if (data.isEmpty()) {
            throw IllegalStateException("Forecast data must not be empty.")
        }
        if (Duration.between(data.first().start, data.last().start) > Duration.ofDays(1L)) {
            throw IllegalStateException("Forecast data must encompass 24 hours or less.")
        }
    }

    private val firstDatum: ForecastDatum = data.first()

    val time: ZonedDateTime = firstDatum.start
    val airTemperature: Temperature = firstDatum.temperature
    val feelsLikeTemperature: Temperature = firstDatum.feelsLike
    val wind: Wind = firstDatum.wind
    val description: ForecastDescription = firstDatum.description
    val highTemperature: Temperature = data.maxOf { it.temperature }
    val lowTemperature: Temperature = data.minOf { it.temperature }
    val precipitation: PrecipitationForADay? = data
        .firstOrNull { it.precipitation.millimeters > 0 }
        ?.let(this::mapToPrecipitationForADay)
    val smallData: List<SmallForecastDatum> = data.map(this::mapToSmallForecastDatum)

    private fun mapToPrecipitationForADay(datum: ForecastDatum): PrecipitationForADay =
        PrecipitationForADay(
            amount = datum.precipitation,
            at = datum.start
        )

    private fun mapToSmallForecastDatum(datum: ForecastDatum): SmallForecastDatum =
        SmallForecastDatum(
            time = datum.start,
            description = datum.description,
            temperature = datum.temperature,
            precipitation = datum.precipitation
        )
}

data class PrecipitationForADay(
    val amount: Length,
    val at: ZonedDateTime
)

data class SmallForecastDatum(
    val time: ZonedDateTime,
    val description: ForecastDescription,
    val temperature: Temperature,
    val precipitation: Length
)