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
            isDay = now.isDay,
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
                    isDay = it.isDay
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
            days = days.map { day ->
                val (repCode, repCodeIsDay) = getRepresentativeWmoCode(day.hours)
                OverviewDay(
                    unixSecond = day.startUnixSecond,
                    representativeWmoCode = repCode,
                    representativeWmoCodeIsDay = repCodeIsDay,
                    minimumTemperature = day.minimumTemperature.toSystem(system),
                    maximumTemperature = day.maximumTemperature.toSystem(system),
                    maximumPop = day.maximumPop,
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

        private fun getRepresentativeWmoCode(hours: List<Hour>): Pair<Int, Boolean> =
            if (hours.any { it.wmoCode > 48 }) {
                // The most severe weather condition is at least light drizzle. Because this involves
                // precipitation, it could affect people's plans for the day. In this case they would
                // likely want to know the most severe weather condition so they can prepare accordingly
                val mostExtremeHour = hours.maxBy { it.wmoCode }
                mostExtremeHour.wmoCode to mostExtremeHour.isDay
            } else {
                // The most severe weather condition is depositing rime fog. Because this doesn't
                // involve precipitation, it rarely affects people's plans for the day. In this case
                // they would likely want to know what most of the day looks like instead
                val priorityHours = hours
                    // Prioritize daytime if possible
                    .filter { it.isDay }
                    .takeUnless { it.isEmpty() } ?: hours
                val mostCommonWmoCode = priorityHours
                    // Map WMO code to amount of times it appears in the list
                    .groupingBy { it.wmoCode }.eachCount()
                    // Find the most severe WMO code (sortedByDescending) that appears the most (maxBy)
                    .entries.sortedByDescending { it.key }.maxBy { it.value }.key
                mostCommonWmoCode to priorityHours[0].isDay
            }
        }
}

data class OverviewNow(
    val unixSecond: Long,
    val temperature: Temperature,
    val minimumTemperature: Temperature,
    val maximumTemperature: Temperature,
    val feelsLike: Temperature,
    val wmoCode: Int,
    val isDay: Boolean,
    val totalPrecipitation: Length,
    val snowFall: Length,
    val rainFall: Length,
    val pressure: Pressure,
    val uvIndex: UvIndex,
    val wind: Speed,
    val gust: Speed,
    val windDirection: Angle,
    val humidity: Int,
    val dewPoint: Temperature,
    val visibility: Length
)

sealed interface OverviewHour {
    val unixSecond: Long

    class Weather internal constructor(
        override val unixSecond: Long,
        val temperature: Temperature,
        val pop: Int,
        val wmoCode: Int,
        val isDay: Boolean
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
    val representativeWmoCode: Int,
    val representativeWmoCodeIsDay: Boolean,
    val minimumTemperature: Temperature,
    val maximumTemperature: Temperature,
    val maximumPop: Int
)