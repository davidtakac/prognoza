package hr.dtakac.prognoza.ui.overview

import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.shared.entity.*
import hr.dtakac.prognoza.ui.common.TextResource
import hr.dtakac.prognoza.ui.common.toUvIndexStringId
import hr.dtakac.prognoza.ui.common.wmoCodeToWeatherDescription
import hr.dtakac.prognoza.ui.common.wmoCodeToWeatherIcon
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone

fun Overview.toUiModel(): OverviewDataState = OverviewDataState(
  now = now.toUiModel(),
  hours = hours.mapIndexed { idx, hour ->
    hour.toUiModel(
      isNow = idx == 0,
      timeZone = timeZone
    )
  },
  days = days.days.mapIndexed { idx, day ->
    day.toUiModel(
      isToday = idx == 0,
      currentTemperature = if (idx == 0) now.temperature else null,
      absoluteMinimumTemperature = days.minimumTemperature,
      absoluteMaximumTemperature = days.maximumTemperature,
      timeZone = timeZone
    )
  },
  details = buildList {
    add(rainfall.toUiModel(timeZone).copy(isSnow = false))
    snowfall?.let { add(it.toUiModel(timeZone).copy(isSnow = true)) }
    add(uvIndex.toUiModel(timeZone))
    add(feelsLike.toUiModel())
    add(wind.toUiModel())
    add(humidity.toUiModel())
  }
)

private fun OverviewNow.toUiModel() = OverviewNowState(
  time = TextResource.fromResId(R.string.forecast_label_now),
  temperature = TextResource.fromTemperature(temperature),
  maximumTemperature = TextResource.fromTemperature(maximumTemperature),
  minimumTemperature = TextResource.fromTemperature(minimumTemperature),
  feelsLikeTemperature = TextResource.fromTemperature(feelsLike),
  weatherIcon = wmoCodeToWeatherIcon(wmoCode, isDay),
  weatherDescription = TextResource.fromResId(wmoCodeToWeatherDescription(wmoCode, isDay)),
)

private fun OverviewHour.toUiModel(isNow: Boolean, timeZone: TimeZone) = when (this) {
  is OverviewHour.Sunrise -> OverviewHourState.Sunrise(
    TextResource.fromUnixSecondToShortTime(
      unixSecond,
      timeZone
    )
  )
  is OverviewHour.Sunset -> OverviewHourState.Sunset(
    TextResource.fromUnixSecondToShortTime(
      unixSecond,
      timeZone
    )
  )
  is OverviewHour.Weather -> OverviewHourState.Weather(
    time = if (isNow) TextResource.fromResId(R.string.forecast_label_now)
    else TextResource.fromUnixSecondToShortTime(unixSecond, timeZone),
    temperature = TextResource.fromTemperature(temperature),
    weatherIcon = wmoCodeToWeatherIcon(wmoCode, isDay),
    pop = pop.takeUnless { it == 0 }?.let(TextResource::fromPercentage)
  )
}

private fun OverviewDay.toUiModel(
  isToday: Boolean,
  currentTemperature: Temperature?,
  absoluteMinimumTemperature: Temperature,
  absoluteMaximumTemperature: Temperature,
  timeZone: TimeZone
): OverviewDayState {
  val temperatureRange = absoluteMaximumTemperature - absoluteMinimumTemperature
  return OverviewDayState(
    dayOfWeek = if (isToday) TextResource.fromResId(R.string.forecast_label_today)
    else TextResource.fromUnixSecondToShortDayOfWeek(startUnixSecond, timeZone),
    pop = maximumPop.takeUnless { it == 0 }?.let(TextResource::fromPercentage),
    weatherIcon = wmoCodeToWeatherIcon(representativeWmoCode.wmoCode, representativeWmoCode.isDay),
    minimumTemperature = TextResource.fromTemperature(minimumTemperature),
    maximumTemperature = TextResource.fromTemperature(maximumTemperature),
    temperatureBarStartFraction = ((minimumTemperature - absoluteMinimumTemperature) / temperatureRange).toFloat(),
    temperatureBarEndFraction = ((absoluteMaximumTemperature - maximumTemperature) / temperatureRange).toFloat(),
    currentTemperatureCenterFraction = currentTemperature?.let { (it - absoluteMinimumTemperature) / temperatureRange }
      ?.toFloat()
  )
}

private fun PrecipitationToday.toUiModel(timeZone: TimeZone) =
  OverviewDetailState.Precipitation(
    amountInLastPeriod = TextResource.fromLength(amountInLastPeriod),
    hoursInLastPeriod = TextResource.fromResId(
      R.string.precipitation_value_last_hours,
      hoursInLastPeriod
    ),
    nextExpected = when {
      amountInNextPeriod.value > 0 -> TextResource.fromResId(
        R.string.precipitation_value_next_hours,
        TextResource.fromLength(amountInNextPeriod),
        hoursInNextPeriod
      )
      startUnixSecondOfNextWetDay != null -> TextResource.fromResId(
        R.string.precipitation_value_on_day,
        TextResource.fromLength(amountInNextWetDay),
        TextResource.fromUnixSecondToShortDayOfWeek(startUnixSecondOfNextWetDay!!, timeZone)
      )
      else -> TextResource.fromResId(
        R.string.precipitation_value_none_in_coming_days,
        numNextDaysScanned
      )
    },
    isSnow = false
  )

private fun OverviewUvIndex.toUiModel(timeZone: TimeZone): OverviewDetailState.UvIndex {
  val now = Clock.System.now().epochSeconds
  val protection = sunProtection
  return OverviewDetailState.UvIndex(
    uvIndex = TextResource.fromNumberToInt(uvIndex.preciseValue),
    level = TextResource.fromResId(uvIndex.toUvIndexStringId()),
    valueCenterFraction = (uvIndex.preciseValue / 11).toFloat().coerceAtMost(1f),
    recommendations = if (protection == null || now >= protection.untilUnixSecond) {
      TextResource.fromResId(R.string.uv_value_protection_none)
    } else if (protection.fromUnixSecond == protection.untilUnixSecond) {
      TextResource.fromResId(
        R.string.uv_value_protection_at,
        TextResource.fromUnixSecondToShortTime(protection.fromUnixSecond, timeZone)
      )
    } else if (now >= protection.fromUnixSecond) {
      TextResource.fromResId(
        R.string.uv_value_protection_until,
        TextResource.fromUnixSecondToShortTime(protection.untilUnixSecond, timeZone)
      )
    } else {
      TextResource.fromResId(
        R.string.uv_value_protection_from_until,
        TextResource.fromUnixSecondToShortTime(protection.fromUnixSecond, timeZone),
        TextResource.fromUnixSecondToShortTime(protection.untilUnixSecond, timeZone)
      )
    }
  )
}

private fun OverviewFeelsLike.toUiModel() = OverviewDetailState.FeelsLike(
  feelsLike = TextResource.fromTemperature(feelsLike),
  description = TextResource.fromResId(
    when (feelsHotter) {
      true -> R.string.feels_like_value_higher
      false -> R.string.feels_like_value_lower
      null -> R.string.feels_like_value_equal
    }
  )
)

private fun OverviewWind.toUiModel() = OverviewDetailState.Wind(
  speed = TextResource.fromSpeed(speed),
  maximumGust = TextResource.fromResId(
    R.string.wind_value_max_gust,
    TextResource.fromSpeed(maximumGust)
  ),
  angle = direction.value.toFloat()
)

private fun OverviewHumidity.toUiModel() = OverviewDetailState.Humidity(
  humidity = TextResource.fromPercentage(humidity),
  dewPoint = TextResource.fromResId(
    R.string.dew_point_value,
    TextResource.fromTemperature(dewPoint)
  )
)