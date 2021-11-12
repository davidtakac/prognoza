package hr.dtakac.prognoza.forecast.mapping

import hr.dtakac.prognoza.core.model.database.ForecastInstant
import hr.dtakac.prognoza.core.model.ui.WEATHER_DESCRIPTIONS
import hr.dtakac.prognoza.core.utils.calculateFeelsLike
import hr.dtakac.prognoza.core.utils.toWindCompassDirectionId
import hr.dtakac.prognoza.forecast.model.InstantUiModel
import java.time.temporal.ChronoUnit

fun ForecastInstant.toInstantUiModel(nextInstant: ForecastInstant?): InstantUiModel {
    return InstantUiModel(
        time = time,
        nextInstantTime = nextInstant?.time,
        showNextInstantTime = nextInstant?.time?.let {
            ChronoUnit.HOURS.between(time, it) > 1L
        } ?: false,
        temperature = temperature,
        feelsLike = temperature?.let {
            calculateFeelsLike(
                temperature = it,
                windSpeed = windSpeed,
                relativeHumidity = relativeHumidity
            )
        },
        precipitationAmount = nextOneHours?.precipitationAmount
            ?: nextSixHours?.precipitationAmount,
        windSpeed = windSpeed,
        windCompassDirectionId = windFromDirection?.toWindCompassDirectionId(),
        relativeHumidity = relativeHumidity,
        airPressure = airPressure,
        weatherDescription = WEATHER_DESCRIPTIONS[
                nextOneHours?.symbolCode ?: nextSixHours?.symbolCode
        ]
    )
}