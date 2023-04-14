package hr.dtakac.prognoza.shared.data.openmeteo.network

import hr.dtakac.prognoza.shared.entity.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.math.abs

@Serializable
internal data class OpenMeteoResponse(
    @SerialName("hourly")
    val hourly: OpenMeteoHourly,
    @SerialName("daily")
    val daily: OpenMeteoDaily
)

@Serializable
internal data class OpenMeteoHourly(
    @SerialName("time")
    val epochSeconds: List<Long>,
    @SerialName("temperature_2m")
    val temperature: List<Double>,
    @SerialName("relativehumidity_2m")
    val relativeHumidity: List<Double>,
    @SerialName("apparent_temperature")
    val feelsLike: List<Double>,
    @SerialName("precipitation")
    val precipitation: List<Double>,
    @SerialName("weathercode")
    val weatherCode: List<Int>,
    @SerialName("surface_pressure")
    val airPressure: List<Double>,
    @SerialName("windspeed_10m")
    val windSpeed: List<Double>,
    @SerialName("winddirection_10m")
    val windDirection: List<Int>
)

@Serializable
internal data class OpenMeteoDaily(
    @SerialName("time")
    val dayStartEpochSeconds: List<Long>,
    @SerialName("sunrise")
    val sunriseEpochSeconds: List<Long>,
    @SerialName("sunset")
    val sunsetEpochSeconds: List<Long>
)

internal fun OpenMeteoResponse.mapToEntities(): List<Hour> {
    val result = mutableListOf<Hour>()
    for (i in hourly.epochSeconds.indices) {
        val currEpochSeconds = hourly.epochSeconds[i]
        val nextEpochSeconds = hourly.epochSeconds.getOrNull(i + 1) ?: break
        val description = mapWmoCodeToDescription(
            wmoCode = hourly.weatherCode[i],
            isDay = isTimestampDay(
                timestamp = currEpochSeconds,
                sunrises = daily.sunriseEpochSeconds,
                sunsets = daily.sunsetEpochSeconds
            )
        )
        val datum = Hour(
            unixSecond = currEpochSeconds * 1000,
            endEpochMillis = nextEpochSeconds * 1000,
            temperature = Temperature(
                value = hourly.temperature[i],
                unit = TemperatureUnit.DegreeCelsius
            ),
            precipitation = Length(
                value = hourly.precipitation[i],
                unit = LengthUnit.Millimetre
            ),
            wind = Wind(
                speed = Speed(
                    value = hourly.windSpeed[i],
                    unit = SpeedUnit.KilometrePerHour
                ),
                direction = Angle(
                    value = hourly.windDirection[i].toDouble(),
                    unit = AngleUnit.Degree
                )
            ),
            pressureAtSeaLevel = Pressure(
                value = hourly.airPressure[i],
                unit = PressureUnit.Millibar
            ),
            description = description,
            mood = Mood.fromDescription(description),
            relativeHumidity = Percentage(
                value = hourly.relativeHumidity[i],
                unit = PercentageUnit.Percent
            ),
            feelsLike = Temperature(
                value = hourly.feelsLike[i],
                unit = TemperatureUnit.DegreeCelsius
            )
        )
        result.add(datum)
    }
    return result
}

// See https://open-meteo.com/en/docs for mapping
private fun mapWmoCodeToDescription(
    wmoCode: Int,
    isDay: Boolean
): Description = when (wmoCode) {
    0 -> when (isDay) {
        true -> Description.CLEAR_SKY_DAY
        false -> Description.CLEAR_SKY_NIGHT
    }

    1 -> when (isDay) {
        true -> Description.FAIR_DAY
        false -> Description.FAIR_NIGHT
    }

    2 -> when (isDay) {
        true -> Description.PARTLY_CLOUDY_DAY
        false -> Description.PARTLY_CLOUDY_NIGHT
    }

    3 -> Description.CLOUDY

    45, // Fog
    48  // Depositing rime fog
    -> Description.FOG

    51, // Light drizzle
    56, // Light freezing drizzle
    61, // Slight rain
    66  // Light freezing rain
    -> Description.LIGHT_RAIN

    53, // Moderate drizzle
    63  // Moderate rain
    -> Description.RAIN

    55, // Dense drizzle
    57, // Dense freezing drizzle
    65, // Heavy rain
    67  // Heavy freezing rain
    -> Description.HEAVY_RAIN

    71 -> Description.LIGHT_SNOW
    73, // Moderate snow
    77  // Snow grains
    -> Description.SNOW
    75 -> Description.HEAVY_SNOW

    80 -> when (isDay) {
        true -> Description.LIGHT_RAIN_SHOWERS_DAY
        false -> Description.LIGHT_RAIN_SHOWERS_NIGHT
    }
    81 -> when (isDay) {
        true -> Description.RAIN_SHOWERS_DAY
        false -> Description.RAIN_SHOWERS_NIGHT
    }
    82 -> when (isDay) {
        true -> Description.HEAVY_RAIN_SHOWERS_DAY
        false -> Description.HEAVY_RAIN_SHOWERS_NIGHT
    }

    85 -> when (isDay) {
        true -> Description.LIGHT_SNOW_SHOWERS_DAY
        false -> Description.LIGHT_SNOW_SHOWERS_NIGHT
    }

    86 -> when (isDay) {
        true -> Description.HEAVY_SNOW_SHOWERS_DAY
        false -> Description.HEAVY_SNOW_SHOWERS_NIGHT
    }

    95 -> Description.RAIN_AND_THUNDER
    96 -> Description.LIGHT_SLEET_AND_THUNDER
    99 -> Description.HEAVY_SLEET_AND_THUNDER

    else -> Description.UNKNOWN
}

internal fun isTimestampDay(
    timestamp: Long,
    sunrises: List<Long>,
    sunsets: List<Long>
): Boolean {
    val closestSunrise = sunrises.minBy { abs(timestamp - it) }
    val closestSunset = sunsets.minBy { abs(timestamp - it) }

    return if (closestSunrise == 0L && closestSunset == 0L) {
        false
    } else if (closestSunrise < closestSunset) {
        timestamp in closestSunrise until closestSunset
    } else {
        timestamp !in closestSunset until closestSunrise
    }
}