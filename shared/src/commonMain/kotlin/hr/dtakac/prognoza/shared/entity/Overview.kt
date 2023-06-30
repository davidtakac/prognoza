package hr.dtakac.prognoza.shared.entity

import kotlinx.datetime.TimeZone

// TODO: probably figure out a way to convert all these to the measurement system at the time of creation.
//  These conversions will have to be repeated for future classes, too. We only have to standardise the
//  measurement system for the graphs feature. The rest can be opinionated from the very start.
class Overview internal constructor(
    val timeZone: TimeZone,
    val now: OverviewNow,
    val hours: List<OverviewHour>,
    val days: OverviewDays,
) {
    companion object {
        fun build(forecast: Forecast, system: MeasurementSystem): Overview? {
            val hours = forecast.futureHours.take(24).takeIf { it.isNotEmpty() } ?: return null
            val days = forecast.futureDays.takeIf { it.isNotEmpty() } ?: return null
            return Overview(
                timeZone = forecast.timeZone,
                now = buildNow(now = hours[0], today = days[0], system = system),
                hours = buildHours(hours = hours, days = days, system = system),
                days = buildDays(days = days, system = system),
            )
        }

        private fun buildNow(
            now: Hour,
            today: Day,
            system: MeasurementSystem
        ) = OverviewNow(
            unixSecond = now.unixSecond,
            temperature = now.temperature.toSystem(system),
            minimumTemperature = today.minimumTemperature.toSystem(system),
            maximumTemperature = today.maximumTemperature.toSystem(system),
            feelsLike = now.feelsLike.toSystem(system),
            wmoCode = now.wmoCode,
            day = now.day,
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
                    wmoCode = it.wmoCode,
                    day = it.day
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

data class OverviewNow(
    val unixSecond: Long,
    val temperature: Temperature,
    val minimumTemperature: Temperature,
    val maximumTemperature: Temperature,
    val feelsLike: Temperature,
    val wmoCode: Int,
    val day: Boolean,
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
)

sealed interface OverviewHour {
    val unixSecond: Long

    class Weather internal constructor(
        override val unixSecond: Long,
        val temperature: Temperature,
        val pop: Double,
        val wmoCode: Int,
        val day: Boolean
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