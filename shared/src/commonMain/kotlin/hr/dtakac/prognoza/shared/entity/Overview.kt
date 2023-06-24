package hr.dtakac.prognoza.shared.entity

class Overview internal constructor(
    val temperature: Temperature,
    val feelsLike: Temperature,
    val wmoCode: Int,
    val day: Boolean,
    val minimumTemperature: Temperature,
    val maximumTemperature: Temperature,
    val hours: List<OverviewHour>,
    val days: OverviewDays,
    val totalPrecipitation: Length,
    val snowFall: Length,
    val rainFall: Length,
    val pressure: Pressure,
    val uvIndex: UvIndex,
    val wind: Speed,
    val gust: Speed,
    val windDirection: Angle,
    val humidity: Double,
    val dewPoint: Temperature,
    val visibility: Length
) {
    companion object {
        fun build(forecast: Forecast, system: MeasurementSystem): Overview? {
            val hours = forecast.futureHours.take(24).takeIf { it.isNotEmpty() } ?: return null
            val days = forecast.futureDays.takeIf { it.isNotEmpty() } ?: return null
            val now = hours[0]
            val today = days[0]
            return Overview(
                temperature = now.temperature.toSystem(system),
                feelsLike = now.feelsLike.toSystem(system),
                minimumTemperature = today.minimumTemperature.toSystem(system),
                maximumTemperature = today.maximumTemperature.toSystem(system),
                wmoCode = now.wmoCode,
                day = now.day,
                hours = buildHours(hours, days, system),
                days = buildDays(days, system),
                totalPrecipitation = now.totalPrecipitation.totalPrecipitationToSystem(system),
                snowFall = now.snow.snowToSystem(system),
                rainFall = (now.rain + now.showers).rainToSystem(system),
                pressure = now.pressureAtSeaLevel.toSystem(system),
                uvIndex = now.uvIndex,
                wind = now.wind.toSystem(system),
                windDirection = now.windDirection,
                gust = now.gust.toSystem(system),
                humidity = now.relativeHumidity,
                dewPoint = now.dewPoint.toSystem(system),
                visibility = now.visibility.visibilityToSystem(system)
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

        private fun Length.totalPrecipitationToSystem(system: MeasurementSystem) =
            rainToSystem(system)

        private fun Length.rainToSystem(system: MeasurementSystem) = convertTo(
            if (system == MeasurementSystem.Imperial) LengthUnit.Inch
            else LengthUnit.Millimetre
        )

        private fun Length.snowToSystem(system: MeasurementSystem) = convertTo(
            if (system == MeasurementSystem.Imperial) LengthUnit.Inch
            else LengthUnit.Centimetre
        )

        private fun Length.visibilityToSystem(system: MeasurementSystem) = convertTo(
            if (system == MeasurementSystem.Imperial) LengthUnit.Mile
            else LengthUnit.Kilometre
        )

        private fun Pressure.toSystem(system: MeasurementSystem) = convertTo(
            if (system == MeasurementSystem.Imperial) PressureUnit.InchOfMercury
            else PressureUnit.Millibar
        )

        private fun Speed.toSystem(system: MeasurementSystem) = convertTo(
            if (system == MeasurementSystem.Imperial) SpeedUnit.MilePerHour
            else SpeedUnit.MetrePerSecond
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