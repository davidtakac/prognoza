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
    val nextDistinctPrecipitation: NextDistinctPrecipitation = resolveNextDistinctPrecipitation()

    private fun resolveNextDistinctPrecipitation(): NextDistinctPrecipitation {
        val isPrecipitatingNow = precipitationNow.description != PrecipitationDescription.NONE
        return if (isPrecipitatingNow) {
            val idxFirstBreak = restOfDayData.indexOfFirst {
                it.precipitation.description == PrecipitationDescription.NONE
            }
            if (idxFirstBreak < 0) {
                NextDistinctPrecipitation.RestOfDay
            } else {
                val breakTime = restOfDayData[idxFirstBreak].time
                val idxPrecipitationAfterBreak = restOfDayData
                    .subList(idxFirstBreak + 1, restOfDayData.size)
                    .indexOfFirst {
                        it.precipitation.description != PrecipitationDescription.NONE
                    }
                if (idxPrecipitationAfterBreak < 0) {
                    NextDistinctPrecipitation.Breaks(breakTime)
                } else {
                    NextDistinctPrecipitation.ContinuesAfterBreak(
                        breakTime = breakTime,
                        continueTime = restOfDayData[idxPrecipitationAfterBreak].time,
                    )
                }
            }
        } else {
            val idxFirstPrecipitation = restOfDayData.indexOfFirst {
                it.precipitation.description != PrecipitationDescription.NONE
            }
            if (idxFirstPrecipitation < 0) {
                NextDistinctPrecipitation.None
            } else {
                NextDistinctPrecipitation.Starts(restOfDayData[idxFirstPrecipitation].time)
            }
        }
    }
}

data class SmallForecastDatum(
    val time: ZonedDateTime,
    val description: ForecastDescription,
    val temperature: Temperature,
    val precipitation: Precipitation,
    val wind: Wind
)

sealed interface NextDistinctPrecipitation {
    object None : NextDistinctPrecipitation
    object RestOfDay : NextDistinctPrecipitation
    data class Starts(
        val at: ZonedDateTime
    ) : NextDistinctPrecipitation

    data class Breaks(
        val at: ZonedDateTime
    ) : NextDistinctPrecipitation

    data class ContinuesAfterBreak(
        val breakTime: ZonedDateTime,
        val continueTime: ZonedDateTime,
    ) : NextDistinctPrecipitation
}