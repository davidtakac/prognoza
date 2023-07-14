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
import kotlin.math.roundToInt

private const val Tag = "ForecastService"

internal class ForecastService(
  private val client: HttpClient,
  private val userAgent: String,
  private val dotDecimalFormatter: DotDecimalFormatter,
  private val computationDispatcher: CoroutineDispatcher
) {
  suspend fun getForecast(coordinates: Coordinates): Forecast? {
    val response = try {
      client.get("https://api.open-meteo.com/v1//forecast") {
        header(HttpHeaders.UserAgent, userAgent)
        parameter(
          "latitude",
          dotDecimalFormatter.format(coordinates.latitude, decimalPlaces = 2)
        )
        parameter(
          "longitude",
          dotDecimalFormatter.format(coordinates.longitude, decimalPlaces = 2)
        )
        parameter("timezone", "auto")
        parameter("timeformat", "unixtime")
        parameter("past_days", 1)
        parameter("forecast_days", 10)
        parameter("hourly", hourlyParams)
        parameter("daily", dailyParams)
        parameter("temperature_unit", "celsius")
        parameter("windspeed_unit", "ms")
        parameter("precipitation_unit", "mm")
      }.body<Response>()
    } catch (e: Exception) {
      Napier.e(Tag, e)
      return null
    }

    return try {
      withContext(computationDispatcher) { response.toForecast() }
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

  private val dailyParams = listOf("sunrise", "sunset",).joinToString(",")
}

@Serializable
private data class Response(
  @SerialName("timezone")
  val timeZoneId: String,
  @SerialName("hourly")
  val hourly: Hourly,
  @SerialName("daily")
  val daily: Daily
) {
  fun toForecast(): Forecast {
    val timeZone = TimeZone.of(timeZoneId)
    return Forecast(
      timeZone = timeZone,
      hours = buildHours(),
      sunriseEpochSeconds = daily.sunrise,
      sunsetEpochSeconds = daily.sunset
    )
  }

  private fun buildHours() = buildList {
    for (i in hourly.startUnixSecond.indices) {
      add(
        Hour(
          startUnixSecond = hourly.startUnixSecond[i],
          wmoCode = hourly.weathercode[i],
          temperature = Temperature(
            hourly.temperature2m[i],
            TemperatureUnit.DegreeCelsius
          ),
          rain = Length(hourly.rain[i], LengthUnit.Millimetre),
          showers = Length(hourly.showers[i], LengthUnit.Millimetre),
          snow = Length(hourly.snowfall[i], LengthUnit.Centimetre),
          pop = Pop(hourly.precipitationProbability[i]),
          gust = Speed(hourly.windGusts10m[i], SpeedUnit.MetrePerSecond),
          wind = Speed(hourly.windSpeed10m[i], SpeedUnit.MetrePerSecond),
          windDirection = Angle(hourly.windDirection10m[i], AngleUnit.Degree),
          pressure = Pressure(hourly.pressureMsl[i], PressureUnit.Millibar),
          humidity = hourly.relativeHumidity2m[i],
          dewPoint = Temperature(hourly.dewpoint2m[i], TemperatureUnit.DegreeCelsius),
          visibility = Length(hourly.visibility[i], LengthUnit.Metre),
          uvIndex = UvIndex(hourly.uvIndex[i].roundToInt()),
          isDay = hourly.isDay[i] == 1,
          feelsLike = Temperature(
            hourly.apparentTemperature[i],
            TemperatureUnit.DegreeCelsius
          )
        )
      )
    }
  }
}

@Serializable
private data class Hourly(
  @SerialName("time") var startUnixSecond: List<Long>,
  @SerialName("temperature_2m") var temperature2m: List<Double>,
  @SerialName("relativehumidity_2m") var relativeHumidity2m: List<Int>,
  @SerialName("dewpoint_2m") var dewpoint2m: List<Double>,
  @SerialName("apparent_temperature") var apparentTemperature: List<Double>,
  @SerialName("precipitation_probability") var precipitationProbability: List<Int>,
  @SerialName("precipitation") var precipitation: List<Double>,
  @SerialName("rain") var rain: List<Double>,
  @SerialName("showers") var showers: List<Double>,
  @SerialName("snowfall") var snowfall: List<Double>,
  @SerialName("weathercode") var weathercode: List<Int>,
  @SerialName("pressure_msl") var pressureMsl: List<Double>,
  @SerialName("visibility") var visibility: List<Double>,
  @SerialName("windspeed_10m") var windSpeed10m: List<Double>,
  @SerialName("windgusts_10m") var windGusts10m: List<Double>,
  @SerialName("winddirection_10m") var windDirection10m: List<Double>,
  @SerialName("uv_index") var uvIndex: List<Double>,
  @SerialName("is_day") var isDay: List<Int>
)

@Serializable
private data class Daily(
  @SerialName("sunrise") var sunrise: List<Long>,
  @SerialName("sunset") var sunset: List<Long>,
)