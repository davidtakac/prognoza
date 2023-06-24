package hr.dtakac.prognoza.shared.entity

class Overview private constructor(
    val temperature: Temperature,
    val minimumTemperature: Temperature,
    val maximumTemperature: Temperature,
    val feelsLike: Temperature,
    val wmoCode: Int,
    val hours: List<OverviewHour>,
    val days: OverviewDays,
) {
    companion object {
        fun build(forecast: Forecast, system: MeasurementSystem): Overview? {
            val hours = forecast.futureHours.take(24).takeIf { it.isNotEmpty() } ?: return null
            val days = forecast.futureDays.takeIf { it.isNotEmpty() } ?: return null
            return Overview(
                temperature = hours[0].temperature.toSystem(system),
                feelsLike = hours[0].feelsLike.toSystem(system),
                minimumTemperature = days[0].minimumTemperature.toSystem(system),
                maximumTemperature = days[0].maximumTemperature.toSystem(system),
                wmoCode = hours[0].wmoCode,
                hours = buildHours(hours, days, system),
                days = buildDays(days, system),
            )
        }

        private fun buildHours(
            hours: List<Hour>,
            days: List<Day>,
            system: MeasurementSystem
        ) = buildList<OverviewHour> {
            val overviewHours = hours.map {
                OverviewHour.Weather(
                    unixSecond = it.unixSecond,
                    temperature = it.temperature.toSystem(system),
                    pop = it.pop,
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

        private fun buildDays(
            days: List<Day>,
            system: MeasurementSystem
        ) = OverviewDays(
            days = days.map {
                OverviewDay(
                    unixSecond = it.startUnixSecond,
                    wmoCode = it.mostExtremeWmoCode,
                    minimumTemperature = it.minimumTemperature.toSystem(system),
                    maximumTemperature = it.maximumTemperature.toSystem(system),
                    maximumPop = it.maximumPop
                )
            },
            minimumTemperature = days.minOf { it.minimumTemperature }.toSystem(system),
            maximumTemperature = days.maxOf { it.maximumTemperature }.toSystem(system)
        )

        private fun Temperature.toSystem(system: MeasurementSystem) = convertTo(
            if (system == MeasurementSystem.Imperial) TemperatureUnit.DegreeFahrenheit
            else TemperatureUnit.DegreeCelsius
        )

        private fun Length.rainToSystem(system: MeasurementSystem) = convertTo(
            if (system == MeasurementSystem.Imperial) LengthUnit.Inch
            else LengthUnit.Millimetre
        )

        private fun Length.snowToSystem(system: MeasurementSystem) = convertTo(
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
        val pop: Double,
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
    val maximumPop: Double
)