package hr.dtakac.prognoza.common

import android.content.res.Resources
import android.util.TypedValue
import android.view.View
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.api.ForecastTimeStepData
import hr.dtakac.prognoza.database.entity.ForecastHour
import hr.dtakac.prognoza.database.entity.ForecastMeta
import hr.dtakac.prognoza.database.entity.Place
import hr.dtakac.prognoza.databinding.CellDayBinding
import hr.dtakac.prognoza.databinding.LayoutWindAndPrecipitationBinding
import hr.dtakac.prognoza.forecast.uimodel.*
import hr.dtakac.prognoza.places.PlaceUiModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

// region various
fun <T> List<T>.mostCommon(): T? =
    groupingBy { it }.eachCount().maxByOrNull { it.value }?.key

// courtesy of https://stackoverflow.com/a/6327095
val Number.toPx
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    )

fun ZonedDateTime.atStartOfDay(): ZonedDateTime =
    toLocalDate().atStartOfDay(ZoneId.systemDefault())
// endregion

// region Temperature feels like
/**
 * Calculates the "Feels like" temperature. Assumes [temperature] in degrees Celsius,
 * [windSpeed] in m/s and [relativeHumidity] in percent.
 *
 * @return The "Feels like" temperature in degrees Celsius or null if it can't be calculated with
 * the provided parameters.
 */
fun calculateFeelsLikeTemperature(
    temperature: Float,
    windSpeed: Float?,
    relativeHumidity: Float?
): Float? {
    val airTemperatureFahrenheit = (temperature * 1.8f) + 32f
    val feelsLikeFahrenheit = if (airTemperatureFahrenheit in -50f..50f) {
        if (windSpeed == null) {
            null
        } else {
            val windSpeedMph = windSpeed * 2.236936f
            calculateWindChill(airTemperatureFahrenheit, windSpeedMph)
        }
    } else {
        if (relativeHumidity == null) {
            null
        } else {
            calculateHeatIndex(airTemperatureFahrenheit, relativeHumidity)
        }
    }
    return if (feelsLikeFahrenheit == null) null else (feelsLikeFahrenheit - 32f) / 1.8f
}

/**
 * Calculates wind chill. Assumes [temperature] in degrees Fahrenheit and [windSpeed] in miles per hour.
 *
 * Based on [The Wind Chill Equation](https://www.weather.gov/media/epz/wxcalc/windChill.pdf)
 *
 * @return Wind chill temperature in degrees Fahrenheit
 */
private fun calculateWindChill(
    temperature: Float,
    windSpeed: Float
): Float {
    return 35.74f + (0.6215f * temperature) - (35.75f * windSpeed.pow(0.16f)) +
            (0.4275f * temperature * windSpeed.pow(0.16f))
}

/**
 * Calculates heat index. Assumes [temperature] in degrees Fahrenheit and [humidity] in percent.
 *
 * Based on [The Heat Index Equation](https://www.wpc.ncep.noaa.gov/html/heatindex_equation.shtml)
 *
 * @return Heat index temperature in degrees Fahrenheit
 */
private fun calculateHeatIndex(
    temperature: Float,
    humidity: Float
): Float {
    val simpleHeatIndex = 0.5f * (temperature + 61 + (temperature - 68) * 1.2f + humidity * 0.094f)
    return if (simpleHeatIndex < 80f) {
        simpleHeatIndex
    } else {
        val rothfuszHeatIndex = -42.379f + 2.04901523f * temperature + 10.14333127f * humidity -
                0.22475541f * temperature * humidity - 0.00683783f * temperature * temperature -
                0.05481717f * humidity * humidity + 0.00122874f * temperature * temperature * humidity +
                0.00085282f * temperature * humidity * humidity - 0.00000199f * temperature * temperature * humidity * humidity

        if (humidity < 13f && temperature in 80f..112f) {
            val adjustment = ((13f - humidity) / 4) * sqrt((17f - abs(temperature - 95f)) / 17f)
            rothfuszHeatIndex - adjustment
        } else if (humidity > 85f && temperature in 80f..87f) {
            val adjustment = ((humidity - 85f) / 10f) * ((87f - temperature) / 5f)
            rothfuszHeatIndex + adjustment
        } else {
            rothfuszHeatIndex
        }
    }
}

// endregion

// region ForecastHour
fun ForecastHour.toHourUiModel() =
    HourUiModel(
        temperature = temperature?.roundToInt(),
        feelsLike = if (temperature == null) {
            null
        } else {
            calculateFeelsLikeTemperature(temperature, windSpeed, relativeHumidity)?.roundToInt()
        },
        precipitation = precipitationAmount,
        windSpeed = windSpeed?.toKilometresPerHour(),
        windIconRotation = windFromDirection?.plus(180f),
        weatherIcon = WEATHER_ICONS[symbolCode],
        time = time,
        relativeHumidity = relativeHumidity,
        windFromCompassDirection = windFromDirection?.toCompassDirection(),
        pressure = pressure
    )

suspend fun List<ForecastHour>.toDayUiModel(coroutineScope: CoroutineScope): DayUiModel {
    val weatherIconAsync = coroutineScope.async { representativeWeatherIcon() }
    val lowTempAsync = coroutineScope.async { minTemperature() }
    val highTempAsync = coroutineScope.async { maxTemperature() }
    val precipitationAsync = coroutineScope.async { totalPrecipitationAmount() }
    val maxWindSpeedAsync = coroutineScope.async { maxWindSpeed() }
    return DayUiModel(
        time = get(0).time,
        weatherIcon = weatherIconAsync.await(),
        lowTemperature = lowTempAsync.await(),
        highTemperature = highTempAsync.await(),
        totalPrecipitationAmount = precipitationAsync.await(),
        maxWindSpeed = maxWindSpeedAsync.await().toKilometresPerHour()
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

fun Float?.isWindSpeedSignificant() = this != null && this > 1f

fun Float.toKilometresPerHour() = this * 3.6f

fun Float.toCompassDirection() = when (this.roundToInt()) {
    in 0..44 -> R.string.direction_n
    in 45..89 -> R.string.direction_ne
    in 90..134 -> R.string.direction_e
    in 135..179 -> R.string.direction_se
    in 180..224 -> R.string.direction_s
    in 225..269 -> R.string.direction_sw
    in 270..314 -> R.string.direction_w
    else -> R.string.direction_n
}
// endregion

// region ForecastData (server response)
fun ForecastTimeStepData.findSymbolCode() =
    next1Hours?.summary?.symbolCode
        ?: next6Hours?.summary?.symbolCode
        ?: next12Hours?.summary?.symbolCode

fun ForecastTimeStepData.findPrecipitationProbability() =
    next1Hours?.data?.probabilityOfPrecipitation
        ?: next6Hours?.data?.probabilityOfPrecipitation
        ?: next12Hours?.data?.probabilityOfPrecipitation

fun ForecastTimeStepData.findPrecipitationAmount() =
    next1Hours?.data?.precipitationAmount
        ?: next6Hours?.data?.precipitationAmount
        ?: next12Hours?.data?.precipitationAmount
// endregion

// region Place
val Place.shortenedName get() = fullName.split(", ").getOrNull(0) ?: fullName

fun Place.toPlaceUiModel(isSaved: Boolean, isSelected: Boolean) =
    PlaceUiModel(
        id = id,
        name = shortenedName,
        fullName = fullName,
        isSaved = isSaved,
        isSelected = isSelected
    )
// endregion

// region ForecastMeta
fun ForecastMeta.hasExpired(): Boolean = ZonedDateTime.now() > expires
// endregion

// region CelLDayBinding
fun CellDayBinding.bind(uiModel: DayUiModel) {
    val dateTimeFormatter = DateTimeFormatter.ofPattern("EE, d LLLL", Locale.getDefault())
    val resources = root.context
    tvDateTime.text =
        uiModel.time.withZoneSameInstant(ZoneId.systemDefault()).format(dateTimeFormatter)
    tvTemperatureHigh.text =
        resources.getString(R.string.template_temperature_universal, uiModel.highTemperature)
    tvTemperatureLow.text =
        resources.getString(R.string.template_temperature_universal, uiModel.lowTemperature)
    tvDescription.text =
        resources.getString(
            R.string.template_mostly,
            resources.getString(
                uiModel.weatherIcon?.descriptionResourceId ?: R.string.placeholder_description
            ).lowercase()
        )
    ivWeatherIcon.setImageResource(
        uiModel.weatherIcon?.iconResourceId ?: R.drawable.ic_cloud
    )
    windAndPrecipitation.bind(uiModel.maxWindSpeed, uiModel.totalPrecipitationAmount)
    windAndPrecipitation.root.visibility = View.VISIBLE
}
// endregion

// region LayoutWindAndPrecipitationBinding
fun LayoutWindAndPrecipitationBinding.bind(
    windSpeed: Float?, windFromDirection: Float?, precipitationAmount: Float?
) {
    val resources = root.context.resources
    root.visibility = View.VISIBLE
    tvPrecipitationAmount.text =
        if (precipitationAmount.isPrecipitationAmountSignificant()) {
            resources.getString(R.string.template_precipitation_metric, precipitationAmount)
        } else {
            resources.getString(R.string.placeholder_precipitation)
        }
    if (windSpeed.isWindSpeedSignificant()) {
        tvWindSpeed.text =
            resources.getString(R.string.template_wind_metric, windSpeed)
        ivWindFromDirection.visibility = View.VISIBLE
        ivWindFromDirection.rotation = windFromDirection ?: 0f
    } else {
        tvWindSpeed.text = resources.getString(R.string.placeholder_wind_speed)
        ivWindFromDirection.visibility = View.GONE
    }
}

fun LayoutWindAndPrecipitationBinding.bind(
    maxWindSpeed: Float?, totalPrecipitationAmount: Float?
) {
    val resources = root.context.resources
    ivWindFromDirection.visibility = View.GONE
    tvPrecipitationAmount.text =
        resources.getString(
            R.string.template_total_precipitation,
            if (totalPrecipitationAmount.isPrecipitationAmountSignificant()) {
                resources.getString(R.string.template_precipitation_metric, totalPrecipitationAmount)
            } else {
                resources.getString(R.string.placeholder_precipitation)
            }
        )
    tvWindSpeed.text =
        resources.getString(
            R.string.template_max_wind,
            if (maxWindSpeed.isWindSpeedSignificant()) {
                resources.getString(R.string.template_wind_metric, maxWindSpeed)
            } else {
                resources.getString(R.string.placeholder_wind_speed)
            }
        )
}
// endregion