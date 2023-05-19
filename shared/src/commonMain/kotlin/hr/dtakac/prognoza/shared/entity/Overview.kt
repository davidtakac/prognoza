package hr.dtakac.prognoza.shared.entity

class Overview private constructor(
    val temperature: Temperature,
    val feelsLike: Temperature,
    val minimumTemperature: Temperature,
    val maximumTemperature: Temperature,
    val wmoCode: Int,
    val hours: List<OverviewHour>,
    val days: OverviewDays
) {
    companion object {
        fun create(forecast: Forecast, measurementSystem: MeasurementSystem): Overview {
            val hours = forecast.futureHours.take(24)
            val days = forecast.futureDays
            return Overview(
                temperature = hours[0].temperature.inSystem(measurementSystem),
                feelsLike = hours[0].feelsLike.inSystem(measurementSystem),
                minimumTemperature = days[0].minimumTemperature.inSystem(measurementSystem),
                maximumTemperature = days[0].maximumTemperature.inSystem(measurementSystem),
                wmoCode = hours[0].wmoCode,
                hours = createHours(hours, days, measurementSystem),
                days = createDays(days, measurementSystem)
            )
        }

        private fun createHours(
            hours: List<Hour>,
            days: List<Day>,
            measurementSystem: MeasurementSystem
        ): List<OverviewHour> = buildList {
            val overviewHours = hours.map {
                OverviewHour.Weather(
                    unixSecond = it.unixSecond,
                    temperature = it.temperature.inSystem(measurementSystem),
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

        private fun createDays(days: List<Day>, measurementSystem: MeasurementSystem) = OverviewDays(
            days = days.map {
                OverviewDay(
                    unixSecond = it.unixSecond,
                    wmoCode = it.wmoCode,
                    minimumTemperature = it.minimumTemperature.inSystem(measurementSystem),
                    maximumTemperature = it.maximumTemperature.inSystem(measurementSystem),
                    maximumProbabilityOfPrecipitation = it.maximumProbabilityOfPrecipitation
                )
            },
            minimumTemperature = days.minOf { it.minimumTemperature }.inSystem(measurementSystem),
            maximumTemperature = days.maxOf { it.maximumTemperature }.inSystem(measurementSystem)
        )

        private fun Temperature.inSystem(system: MeasurementSystem) = convertTo(
            if (system == MeasurementSystem.Imperial) TemperatureUnit.DegreeFahrenheit
            else TemperatureUnit.DegreeCelsius
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