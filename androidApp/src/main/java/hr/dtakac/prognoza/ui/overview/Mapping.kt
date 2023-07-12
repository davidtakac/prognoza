package hr.dtakac.prognoza.ui.overview

import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.shared.entity.*
import hr.dtakac.prognoza.ui.common.TextResource
import hr.dtakac.prognoza.ui.common.toUvIndexStringId
import hr.dtakac.prognoza.ui.common.wmoCodeToWeatherDescription
import hr.dtakac.prognoza.ui.common.wmoCodeToWeatherIcon
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
    else TextResource.fromUnixSecondToShortDayOfWeek(unixSecond, timeZone),
    pop = maximumPop.takeUnless { it == 0 }?.let(TextResource::fromPercentage),
    weatherIcon = wmoCodeToWeatherIcon(representativeWmoCode, representativeWmoCodeIsDay),
    minimumTemperature = TextResource.fromTemperature(minimumTemperature),
    maximumTemperature = TextResource.fromTemperature(maximumTemperature),
    temperatureBarStartFraction = ((minimumTemperature - absoluteMinimumTemperature) / temperatureRange).toFloat(),
    temperatureBarEndFraction = ((absoluteMaximumTemperature - maximumTemperature) / temperatureRange).toFloat(),
    currentTemperatureCenterFraction = currentTemperature?.let { (it - absoluteMinimumTemperature) / temperatureRange }
      ?.toFloat()
  )
}

private fun OverviewPrecipitation.toUiModel(timeZone: TimeZone) =
  OverviewDetailState.Precipitation(
    amountInLastPeriod = TextResource.fromLength(past.amount),
    hoursInLastPeriod = TextResource.fromResId(R.string.precipitation_value_last_hours, past.hours),
    nextExpected = when (val future = future) {
      is OverviewPrecipitation.Future.DayOfWeek -> TextResource.fromResId(
        id = R.string.precipitation_value_next_expected_on_day,
        TextResource.fromLength(future.amount),
        TextResource.fromUnixSecondToShortDayOfWeek(future.startUnixSecond, timeZone)
      )
      is OverviewPrecipitation.Future.NoneExpected -> TextResource.fromResId(
        R.string.precipitation_value_none_expected,
        future.days
      )
      is OverviewPrecipitation.Future.Today -> when (future) {
        is OverviewPrecipitation.Future.Today.WillEnd -> TextResource.fromResId(
          R.string.precipitation_value_will_end_today,
          TextResource.fromLength(future.amount),
          TextResource.fromUnixSecondToShortTime(future.endUnixSecond, timeZone)
        )
        is OverviewPrecipitation.Future.Today.WillStart -> TextResource.fromResId(
          R.string.precipitation_value_will_start_today,
          TextResource.fromLength(future.amount),
          TextResource.fromUnixSecondToShortTime(future.startUnixSecond, timeZone)
        )
        is OverviewPrecipitation.Future.Today.WillStartThenEnd -> TextResource.fromResId(
          R.string.precipitation_value_will_start_and_end_today,
          TextResource.fromLength(future.amount),
          TextResource.fromUnixSecondToShortTime(future.startUnixSecond, timeZone),
          TextResource.fromUnixSecondToShortTime(future.endUnixSecond, timeZone)
        )
      }
    },
    isSnow = false
  )

private fun OverviewUvIndex.toUiModel(timeZone: TimeZone): OverviewDetailState.UvIndex {
  return OverviewDetailState.UvIndex(
    value = TextResource.fromNumberToInt(uvIndex.preciseValue),
    level = TextResource.fromResId(uvIndex.toUvIndexStringId()),
    valueCenterFraction = (uvIndex.preciseValue / UvIndex.Extreme).toFloat().coerceAtMost(1f),
    recommendations = when (val protection = protection) {
      OverviewUvIndex.Protection.None -> TextResource.fromResId(R.string.uv_value_protection_none)
      is OverviewUvIndex.Protection.WillEnd -> TextResource.fromResId(
        R.string.uv_value_protection_until,
        TextResource.fromUnixSecondToShortTime(protection.endUnixSecond, timeZone)
      )
      is OverviewUvIndex.Protection.WillStart -> TextResource.fromResId(
        R.string.uv_value_protection_from,
        TextResource.fromUnixSecondToShortTime(protection.startUnixSecond, timeZone)
      )
      is OverviewUvIndex.Protection.WillStartAndEnd ->
        if (protection.startUnixSecond == protection.endUnixSecond)
          TextResource.fromResId(
            R.string.uv_value_protection_at,
            TextResource.fromUnixSecondToShortTime(protection.startUnixSecond, timeZone)
          )
        else
          TextResource.fromResId(
            R.string.uv_value_protection_from_until,
            TextResource.fromUnixSecondToShortTime(protection.startUnixSecond, timeZone),
            TextResource.fromUnixSecondToShortTime(protection.endUnixSecond, timeZone)
          )
    }
  )
}

private fun OverviewFeelsLike.toUiModel() = OverviewDetailState.FeelsLike(
  value = TextResource.fromTemperature(feelsLike),
  description = TextResource.fromResId(
    when (higherThanActualTemperature) {
      true -> R.string.feels_like_value_higher
      false -> R.string.feels_like_value_lower
      null -> R.string.feels_like_value_equal
    }
  )
)