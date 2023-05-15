package hr.dtakac.prognoza.shared.domain

import hr.dtakac.prognoza.shared.entity.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class GetOverview internal constructor(
    private val getForecast: GetForecast,
    private val computationDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(): OverviewResult =
        when (val result = getForecast()) {
            ForecastResult.Failure -> OverviewResult.Failure
            ForecastResult.NoPlace -> OverviewResult.NoPlace
            is ForecastResult.Success -> {
                val overview = try {
                    Overview(
                        hours = buildOverviewHours(result.forecast),
                        days = buildOverviewDays(result.forecast)
                    )
                } catch (_: Exception) {
                    null
                }
                overview?.let { OverviewResult.Success(it) } ?: OverviewResult.Failure
            }
        }

    private suspend fun buildOverviewHours(forecast: Forecast) =
        withContext(computationDispatcher) {
            OverviewHours(
                hours = buildList {
                    val overviewHours = forecast.futureHours.take(24).map {
                        OverviewHour.Weather(
                            unixSecond = it.unixSecond,
                            temperature = it.temperature,
                            probabilityOfPrecipitation = it.probabilityOfPrecipitation,
                            wmoCode = it.wmoCode
                        )
                    }

                    val sunrises = forecast.days
                        .mapNotNull { it.sunriseUnixSecond }
                        .filter { it in overviewHours.first().unixSecond..overviewHours.last().unixSecond }
                        .map { OverviewHour.Sunrise(it) }

                    val sunsets = forecast.days
                        .mapNotNull { it.sunsetUnixSecond }
                        .filter { it in overviewHours.first().unixSecond..overviewHours.last().unixSecond }
                        .map { OverviewHour.Sunset(it) }

                    addAll(overviewHours)
                    addAll(sunrises)
                    addAll(sunsets)
                    sortBy { it.unixSecond }
                }
            )
        }

    private suspend fun buildOverviewDays(forecast: Forecast) = withContext(computationDispatcher) {
        OverviewDays(
            days = forecast.futureDays.map {
                OverviewDay(
                    unixSecond = it.unixSecond,
                    wmoCode = it.wmoCode,
                    minimumTemperature = it.minimumTemperature,
                    maximumTemperature = it.maximumTemperature
                )
            }
        )
    }
}

sealed interface OverviewResult {
    object NoPlace : OverviewResult
    object Failure : OverviewResult
    data class Success(val overview: Overview) : OverviewResult
}