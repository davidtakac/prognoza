package hr.dtakac.prognoza.shared.entity

class Overview private constructor(
    val temperature: Temperature,
    val minimumTemperature: Temperature,
    val maximumTemperature: Temperature,
    val wmoCode: Int,
    val hours: List<OverviewHour>,
    val days: OverviewDays,
    val feelsLike: Temperature,
    val rainfall: OverviewPrecipitation,
    val snowfall: OverviewPrecipitation?
) {
    companion object {
        private const val FutureHourCount = 24

        fun create(forecast: Forecast, system: MeasurementSystem): Overview {
            val hours = forecast.futureHours.take(FutureHourCount)
            val days = forecast.futureDays
            return Overview(
                temperature = hours[0].temperature.inSystem(system),
                feelsLike = hours[0].feelsLike.inSystem(system),
                minimumTemperature = days[0].minimumTemperature.inSystem(system),
                maximumTemperature = days[0].maximumTemperature.inSystem(system),
                wmoCode = hours[0].wmoCode,
                hours = createHours(forecast, system),
                days = createDays(forecast, system),
                rainfall = createRainfall(forecast, system),
                snowfall = createSnowfall(forecast, system)
            )
        }

        private fun createHours(
            forecast: Forecast,
            system: MeasurementSystem
        ): List<OverviewHour> = buildList {
            val hours = forecast.futureHours.take(FutureHourCount)
            val days = forecast.futureDays

            val overviewHours = hours.map {
                OverviewHour.Weather(
                    unixSecond = it.unixSecond,
                    temperature = it.temperature.inSystem(system),
                    probabilityOfPrecipitation = it.probabilityOfPrecipitation,
                    wmoCode = it.wmoCode
                )
            }
            addAll(overviewHours)

            val sunrises = days
                .mapNotNull { it.sunriseUnixSecond }
                .filter { it in hours.first().unixSecond..hours.last().unixSecond }
                .map { OverviewHour.Sunrise(it) }
            addAll(sunrises)

            val sunsets = days
                .mapNotNull { it.sunsetUnixSecond }
                .filter { it in hours.first().unixSecond..hours.last().unixSecond }
                .map { OverviewHour.Sunset(it) }
            addAll(sunsets)

            // Ensures sunrises and sunsets are placed in between Weather hours
            sortBy { it.unixSecond }
        }

        private fun createDays(forecast: Forecast, system: MeasurementSystem): OverviewDays {
            val days = forecast.futureDays
            return OverviewDays(
                days = days.map {
                    OverviewDay(
                        unixSecond = it.unixSecond,
                        wmoCode = it.wmoCode,
                        minimumTemperature = it.minimumTemperature.inSystem(system),
                        maximumTemperature = it.maximumTemperature.inSystem(system),
                        maximumProbabilityOfPrecipitation = it.maximumProbabilityOfPrecipitation
                    )
                },
                minimumTemperature = days.minOf { it.minimumTemperature }.inSystem(system),
                maximumTemperature = days.maxOf { it.maximumTemperature }.inSystem(system)
            )
        }

        private fun createRainfall(
            forecast: Forecast,
            system: MeasurementSystem
        ): OverviewPrecipitation {
            val pastHours = forecast.hours - forecast.futureHours.toSet()
            var pastRainfall = Length(0.0, LengthUnit.Metre)
            for (h in pastHours) { pastRainfall += h.rain + h.showers }
            val pastPrecipitation = pastRainfall.takeIf { it.value > 0.0 }?.let {
                PrecipitationOverHours(
                    hours = pastHours.size,
                    amount = it.rainInSystem(system)
                )
            }

            val futureHours = forecast.futureHours.take(FutureHourCount)
            var futureRainfall = Length(0.0, LengthUnit.Metre)
            for (h in futureHours) { futureRainfall += h.rain + h.showers }
            val futurePrecipitation = futureRainfall.takeIf { it.value > 0.0 }?.let {
                PrecipitationOverHours(
                    hours = futureHours.size,
                    amount = it.rainInSystem(system)
                )
            }

            val nextRainyDay = forecast.futureDays
                .drop(1)
                .firstOrNull { (it.rain + it.showers).value > 0.0 }
                ?.let {
                    NextPrecipitation(
                        unixSecond = it.unixSecond,
                        amount = (it.rain + it.showers).rainInSystem(system)
                    )
                }

            return OverviewPrecipitation(
                past = pastPrecipitation,
                future = futurePrecipitation,
                nextExpected = nextRainyDay
            )
        }

        private fun createSnowfall(
            forecast: Forecast,
            system: MeasurementSystem
        ): OverviewPrecipitation? {
            val pastHours = forecast.hours - forecast.futureHours.toSet()
            var pastSnowfall = Length(0.0, LengthUnit.Metre)
            for (h in pastHours) { pastSnowfall += h.snow }
            val pastPrecipitation = pastSnowfall.takeIf { it.value > 0.0 }?.let {
                PrecipitationOverHours(
                    hours = pastHours.size,
                    amount = pastSnowfall.snowInSystem(system)
                )
            }

            val futureHours = forecast.futureHours.take(FutureHourCount)
            var futureSnowfall = Length(0.0, LengthUnit.Metre)
            for (h in futureHours) { futureSnowfall += h.snow }
            val futurePrecipitation = futureSnowfall.takeIf { it.value > 0.0 }?.let {
                PrecipitationOverHours(
                    hours = futureHours.size,
                    amount = it.snowInSystem(system)
                )
            }

            val nextSnowyDay = forecast.futureDays
                .drop(1)
                .firstOrNull { it.snow.value > 0.0 }
                ?.let {
                    NextPrecipitation(
                        unixSecond = it.unixSecond,
                        amount = (it.rain + it.showers).snowInSystem(system)
                    )
                }

            return if (pastPrecipitation != null || futurePrecipitation != null || nextSnowyDay != null)
                OverviewPrecipitation(
                    past = pastPrecipitation,
                    future = futurePrecipitation,
                    nextExpected = if (futurePrecipitation == null) nextSnowyDay else null
                ) else null
        }

        private fun Temperature.inSystem(system: MeasurementSystem) = convertTo(
            if (system == MeasurementSystem.Imperial) TemperatureUnit.DegreeFahrenheit
            else TemperatureUnit.DegreeCelsius
        )

        private fun Length.rainInSystem(system: MeasurementSystem) = convertTo(
            if (system == MeasurementSystem.Imperial) LengthUnit.Inch
            else LengthUnit.Millimetre
        )

        private fun Length.snowInSystem(system: MeasurementSystem) = convertTo(
            if (system == MeasurementSystem.Imperial) LengthUnit.Inch
            else LengthUnit.Centimetre
        )
    }
}

sealed interface OverviewHour {
    val unixSecond: Long

    class Weather internal constructor(
        override val unixSecond: Long,
        val temperature: Temperature,
        val probabilityOfPrecipitation: Double,
        val wmoCode: Int
    ) : OverviewHour

    class Sunrise internal constructor(override val unixSecond: Long) : OverviewHour

    class Sunset internal constructor(override val unixSecond: Long) : OverviewHour
}

class OverviewDays internal constructor(
    val days: List<OverviewDay>,
    val minimumTemperature: Temperature,
    val maximumTemperature: Temperature
)

class OverviewDay internal constructor(
    val unixSecond: Long,
    val wmoCode: Int,
    val minimumTemperature: Temperature,
    val maximumTemperature: Temperature,
    val maximumProbabilityOfPrecipitation: Double
)

class OverviewPrecipitation(
    val past: PrecipitationOverHours?,
    val future: PrecipitationOverHours?,
    val nextExpected: NextPrecipitation?
)

class PrecipitationOverHours(
    val hours: Int,
    val amount: Length
)

class NextPrecipitation(
    val unixSecond: Long,
    val amount: Length
)