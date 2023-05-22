package hr.dtakac.prognoza.shared.data

import hr.dtakac.prognoza.shared.entity.*
import hr.dtakac.prognoza.shared.platform.DotDecimalFormatter
import io.github.aakira.napier.Napier
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.datetime.TimeZone
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

private const val Tag = "ForecastService"

internal class ForecastService(
    private val client: HttpClient,
    private val userAgent: String,
    private val dotDecimalFormatter: DotDecimalFormatter,
    private val computationDispatcher: CoroutineDispatcher
) {
    suspend fun getForecast(coordinates: Coordinates): List<Day>? {
        val response = try {
            client.get("https://api.open-meteo.com/v1//forecast") {
                header(HttpHeaders.UserAgent, userAgent)
                parameter("latitude", dotDecimalFormatter.format(coordinates.latitude, decimalPlaces = 2))
                parameter("longitude", dotDecimalFormatter.format(coordinates.longitude, decimalPlaces = 2))
                parameter("timezone", "auto")
                parameter("timeformat", "unixtime")
                parameter("hourly", hourlyParams)
                parameter("daily", dailyParams)
                parameter("windspeed_unit", "ms")
            }.body<Response>()
        } catch (e: Exception) {
            Napier.e(Tag, e)
            return null
        }

        return try {
            withContext(computationDispatcher) {
                response.toEntity()
            }
        } catch (e: Exception) {
            Napier.e(Tag, e)
            return null
        }
    }

    private val hourlyParams = listOf(
        "temperature_2m",
        "relativehumidity_2m",
        "dewpoint_2m",
        "apparent_temperature",
        "precipitation_probability",
        "precipitation",
        "rain",
        "showers",
        "snowfall",
        "weathercode",
        "pressure_msl",
        "visibility",
        "windspeed_10m",
        "windgusts_10m",
        "winddirection_10m",
        "uv_index",
        "is_day"
    ).joinToString(",")

    private val dailyParams = listOf(
        "weathercode",
        "temperature_2m_max",
        "temperature_2m_min",
        "apparent_temperature_max",
        "apparent_temperature_min",
        "sunrise",
        "sunset",
        "uv_index_max",
        "precipitation_sum",
        "rain_sum",
        "showers_sum",
        "snowfall_sum",
        "precipitation_probability_max",
        "windspeed_10m_max",
        "windgusts_10m_max",
        "winddirection_10m_dominant"
    ).joinToString(",")
}

@Serializable
private data class Response(
    @SerialName("timezone")
    val timeZone: String,
    @SerialName("hourly")
    val hourly: Hourly,
    @SerialName("daily")
    val daily: Daily
)

@Serializable
private data class Hourly(
    @SerialName("time") var time: List<Long>,
    @SerialName("temperature_2m") var temperature2m: List<Double>,
    @SerialName("relativehumidity_2m") var relativehumidity2m: List<Double>,
    @SerialName("dewpoint_2m") var dewpoint2m: List<Double>,
    @SerialName("apparent_temperature") var apparentTemperature: List<Double>,
    @SerialName("precipitation_probability") var precipitationProbability: List<Double>,
    @SerialName("precipitation") var precipitation: List<Double>,
    @SerialName("rain") var rain: List<Double>,
    @SerialName("showers") var showers: List<Double>,
    @SerialName("snowfall") var snowfall: List<Double>,
    @SerialName("weathercode") var weathercode: List<Int>,
    @SerialName("pressure_msl") var pressureMsl: List<Double>,
    @SerialName("visibility") var visibility: List<Double>,
    @SerialName("windspeed_10m") var windspeed10m: List<Double>,
    @SerialName("windgusts_10m") var windgusts10m: List<Double>,
    @SerialName("winddirection_10m") var winddirection10m: List<Double>,
    @SerialName("uv_index") var uvIndex: List<Double>,
    @SerialName("is_day") var isDay: List<Int>
)

@Serializable
private data class Daily(
    @SerialName("time") var time: List<Long>,
    @SerialName("weathercode") var weathercode: List<Int>,
    @SerialName("temperature_2m_max") var temperature2mMax: List<Double>,
    @SerialName("temperature_2m_min") var temperature2mMin: List<Double>,
    @SerialName("apparent_temperature_max") var apparentTemperatureMax: List<Double>,
    @SerialName("apparent_temperature_min") var apparentTemperatureMin: List<Double>,
    @SerialName("sunrise") var sunrise: List<Long>,
    @SerialName("sunset") var sunset: List<Long>,
    @SerialName("uv_index_max") var uvIndexMax: List<Double>,
    @SerialName("precipitation_sum") var precipitationSum: List<Double>,
    @SerialName("rain_sum") var rainSum: List<Double>,
    @SerialName("showers_sum") var showersSum: List<Double>,
    @SerialName("snowfall_sum") var snowfallSum: List<Double>,
    @SerialName("precipitation_probability_max") var precipitationProbabilityMax: List<Double>,
    @SerialName("windspeed_10m_max") var windspeed10mMax: List<Double>,
    @SerialName("windgusts_10m_max") var windgusts10mMax: List<Double>,
    @SerialName("winddirection_10m_dominant") var winddirection10mDominant: List<Double>
)

private fun Response.toEntity(): List<Day>? {
    val hours = buildList {
        with(hourly) {
            for (i in time.indices) {
                val hour = Hour(
                    unixSecond = time[i],
                    wmoCode = weathercode[i],
                    temperature = Temperature(temperature2m[i], TemperatureUnit.DegreeCelsius),
                    rain = Length(rain[i], LengthUnit.Millimetre),
                    showers = Length(rain[i], LengthUnit.Millimetre),
                    snow = Length(snowfall[i], LengthUnit.Centimetre),
                    pop = precipitationProbability[i],
                    gust = Speed(windgusts10m[i], SpeedUnit.MetrePerSecond),
                    wind = Speed(windspeed10m[i], SpeedUnit.MetrePerSecond),
                    windDirection = Angle(winddirection10m[i], AngleUnit.Degree),
                    pressureAtSeaLevel = Pressure(pressureMsl[i], PressureUnit.Millibar),
                    relativeHumidity = relativehumidity2m[i],
                    dewPoint = Temperature(dewpoint2m[i], TemperatureUnit.DegreeCelsius),
                    visibility = Length(visibility[i], LengthUnit.Metre),
                    uvIndex = UvIndex(uvIndex[i]),
                    day = isDay[i] == 1,
                    feelsLike = Temperature(apparentTemperature[i], TemperatureUnit.DegreeCelsius)
                )
                add(hour)
            }
        }
    }
    val days = buildList {
        with(daily) {
            for (i in time.indices) {
                val day = Day(
                    unixSecond = time[i],
                    mostExtremeWmoCode = weathercode[i],
                    sunriseUnixSecond = sunrise[i].takeUnless { it == 0L },
                    sunsetUnixSecond = sunset[i].takeUnless { it == 0L },
                    minimumTemperature = Temperature(temperature2mMin[i], TemperatureUnit.DegreeCelsius),
                    maximumTemperature = Temperature(temperature2mMax[i], TemperatureUnit.DegreeCelsius),
                    minimumFeelsLike = Temperature(apparentTemperatureMin[i], TemperatureUnit.DegreeCelsius),
                    maximumFeelsLike = Temperature(apparentTemperatureMax[i], TemperatureUnit.DegreeCelsius),
                    totalRain = Length(rainSum[i], LengthUnit.Millimetre),
                    totalShowers = Length(showersSum[i], LengthUnit.Millimetre),
                    totalSnow = Length(snowfallSum[i], LengthUnit.Centimetre),
                    maximumGust = Speed(windgusts10mMax[i], SpeedUnit.MetrePerSecond),
                    maximumPop = precipitationProbabilityMax[i],
                    maximumWind = Speed(windspeed10mMax[i], SpeedUnit.MetrePerSecond),
                    dominantWindDirection = Angle(winddirection10mDominant[i], AngleUnit.Degree),
                    maximumUvIndex = UvIndex(uvIndexMax[i])
                )
                add(day)
            }
        }
    }
    return try {
        Forecast(TimeZone.of(timeZone), hours, days)
    } catch (_: Exception) {
        null
    }
}