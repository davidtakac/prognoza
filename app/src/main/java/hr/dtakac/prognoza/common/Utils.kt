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
import kotlin.math.roundToInt

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

// region ForecastHour
fun ForecastHour.toHourUiModel() =
    HourUiModel(
        temperature = temperature?.roundToInt(),
        precipitationAmount = precipitationAmount,
        windSpeed = windSpeed?.toKilometresPerHour(),
        windFromDirection = windFromDirection,
        weatherIcon = WEATHER_ICONS[symbolCode],
        time = time
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
        resources.getString(R.string.template_temperature, uiModel.highTemperature)
    tvTemperatureLow.text =
        resources.getString(R.string.template_temperature, uiModel.lowTemperature)
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
            resources.getString(R.string.template_precipitation, precipitationAmount)
        } else {
            resources.getString(R.string.placeholder_precipitation)
        }
    if (windSpeed.isWindSpeedSignificant()) {
        tvWindSpeed.text =
            resources.getString(R.string.template_wind_speed, windSpeed)
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
                resources.getString(R.string.template_precipitation, totalPrecipitationAmount)
            } else {
                resources.getString(R.string.placeholder_precipitation)
            }
        )
    tvWindSpeed.text =
        resources.getString(
            R.string.template_maximum_wind_speed,
            if (maxWindSpeed.isWindSpeedSignificant()) {
                resources.getString(R.string.template_wind_speed, maxWindSpeed)
            } else {
                resources.getString(R.string.placeholder_wind_speed)
            }
        )
}
// endregion