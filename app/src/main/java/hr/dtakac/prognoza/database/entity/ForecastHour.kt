package hr.dtakac.prognoza.database.entity

import androidx.room.Entity
import androidx.room.TypeConverters
import hr.dtakac.prognoza.database.converter.ForecastHourDateTimeConverter
import hr.dtakac.prognoza.forecast.uimodel.*
import hr.dtakac.prognoza.mostCommon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.math.roundToInt

@Entity(primaryKeys = ["time", "placeId"])
@TypeConverters(ForecastHourDateTimeConverter::class)
data class ForecastHour(
    val time: ZonedDateTime,
    val placeId: String,
    val temperature: Float?,
    val symbolCode: String?,
    val precipitationProbability: Float?,
    val precipitationAmount: Float?,
    val windSpeed: Float?,
    val windFromDirection: Float?
)

fun List<ForecastHour>.toHourUiModels() =
    map {
        HourUiModel(
            temperature = it.temperature?.roundToInt(),
            precipitationAmount = it.precipitationAmount,
            windSpeed = it.windSpeed,
            windFromDirection = it.windFromDirection,
            weatherIcon = WEATHER_ICONS[it.symbolCode],
            time = it.time
        )
    }

suspend fun List<ForecastHour>.toDayUiModels(coroutineScope: CoroutineScope) =
    groupBy { it.time.withZoneSameInstant(ZoneId.systemDefault()).toLocalDate() }
        .map { it.value }
        .filter { it.isNotEmpty() }
        .map { hours ->
            val weatherIconAsync = coroutineScope.async { hours.representativeWeatherIcon() }
            val lowTempAsync = coroutineScope.async { hours.minTemperature() }
            val highTempAsync = coroutineScope.async { hours.maxTemperature() }
            val precipitationAsync = coroutineScope.async { hours.totalPrecipitationAmount() }
            val windAsync = coroutineScope.async { hours.maxWindSpeed() }
            DayUiModel(
                time = hours[0].time,
                weatherIcon = weatherIconAsync.await(),
                lowTemperature = lowTempAsync.await(),
                highTemperature = highTempAsync.await(),
                precipitationAmount = precipitationAsync.await(),
                maxWindSpeed = windAsync.await()
            )
        }

fun List<ForecastHour>.maxTemperature() = maxOf { it.temperature ?: Float.MIN_VALUE }.roundToInt()

fun List<ForecastHour>.minTemperature() = minOf { it.temperature ?: Float.MAX_VALUE }.roundToInt()

fun List<ForecastHour>.representativeWeatherIcon(): WeatherIcon? {
    val representativeSymbolCode = filter { it.symbolCode != null }
        .map { it.symbolCode!! }
        .filter { it !in NIGHT_SYMBOL_CODES }
        .mostCommon()
    return WEATHER_ICONS[representativeSymbolCode]
}

fun List<ForecastHour>.totalPrecipitationAmount() =
    sumOf { it.precipitationAmount?.toDouble() ?: 0.0 }.toFloat()

fun List<ForecastHour>.maxWindSpeed() = maxOf { it.windSpeed ?: Float.MIN_VALUE }

fun Float?.isPrecipitationAmountSignificant() = this != null && this >= 0.1f

fun Float?.isWindSpeedSignificant() = this != null && this >= 0.1f