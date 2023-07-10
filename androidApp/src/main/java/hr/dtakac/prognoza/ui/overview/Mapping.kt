package hr.dtakac.prognoza.ui.overview

import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.shared.entity.*
import hr.dtakac.prognoza.ui.common.TextResource
import hr.dtakac.prognoza.ui.common.toUvIndexStringId
import hr.dtakac.prognoza.ui.common.wmoCodeToWeatherDescription
import hr.dtakac.prognoza.ui.common.wmoCodeToWeatherIcon
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

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
    add(rainfall.toUiModel(numDays = days.days.size, timeZone = timeZone))
    snowfall?.let {
      add(it.toUiModel(numDays = days.days.size, timeZone = timeZone).copy(isSnow = true))
    }
    add(uvIndex.toUiModel(timeZone))
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
  val temperatureRange = absoluteMaximumTemperature.value - absoluteMinimumTemperature.value
  return OverviewDayState(
    dayOfWeek = if (isToday) TextResource.fromResId(R.string.forecast_label_today)
    else TextResource.fromUnixSecondToShortDayOfWeek(unixSecond, timeZone),
    pop = maximumPop.takeUnless { it == 0 }?.let(TextResource::fromPercentage),
    weatherIcon = wmoCodeToWeatherIcon(representativeWmoCode, representativeWmoCodeIsDay),
    minimumTemperature = TextResource.fromTemperature(minimumTemperature),
    maximumTemperature = TextResource.fromTemperature(maximumTemperature),
    temperatureBarStartFraction = ((minimumTemperature.value - absoluteMinimumTemperature.value) / temperatureRange).toFloat(),
    temperatureBarEndFraction = ((absoluteMaximumTemperature.value - maximumTemperature.value) / temperatureRange).toFloat(),
    currentTemperatureCenterFraction = currentTemperature?.let { (it.value - absoluteMinimumTemperature.value) / temperatureRange }
      ?.toFloat()
  )
}

private fun OverviewPrecipitation.toUiModel(numDays: Int, timeZone: TimeZone) =
  OverviewDetailState.Precipitation(
    amountInLastPeriod = TextResource.fromLength(amountInLastPeriod),
    hoursInLastPeriod = TextResource.fromResId(
      R.string.precipitation_value_last_hours,
      hoursInLastPeriod
    ),
    nextExpected = startUnixSecondOfNextExpectedAmount?.let {
      val nowDate = Clock.System.now().toLocalDateTime(timeZone).date
      val nextExpectedDate = Instant.fromEpochSeconds(it).toLocalDateTime(timeZone).date
      val nextExpectedAmount = TextResource.fromLength(nextExpectedAmount)
      if (nextExpectedDate == nowDate) {
        TextResource.fromResId(
          id = R.string.precipitation_value_next_expected_from_time,
          nextExpectedAmount,
          TextResource.fromUnixSecondToShortTime(it, timeZone)
        )
      } else {
        TextResource.fromResId(
          id = R.string.precipitation_value_next_expected_on_day,
          nextExpectedAmount,
          TextResource.fromUnixSecondToShortDayOfWeek(it, timeZone)
        )
      }
    } ?: TextResource.fromResId(R.string.precipitation_value_none_expected, numDays),
    isSnow = false
  )

private fun OverviewUvIndex.toUiModel(timeZone: TimeZone): OverviewDetailState.UvIndex {
  val protectionStartUnixSecond = protectionStartUnixSecond
  val protectionEndUnixSecond = protectionEndUnixSecond
  return OverviewDetailState.UvIndex(
    value = TextResource.fromNumberToInt(uvIndex.preciseValue),
    level = TextResource.fromResId(uvIndex.toUvIndexStringId()),
    valueCenterFraction = (uvIndex.preciseValue / 11).toFloat().coerceAtMost(1f),
    recommendations = if (protectionStartUnixSecond == null) {
      if (protectionEndUnixSecond == null) {
        TextResource.fromResId(R.string.uv_value_protection_none)
      } else {
        TextResource.fromResId(
          R.string.uv_value_protection_until,
          TextResource.fromUnixSecondToShortTime(protectionEndUnixSecond, timeZone)
        )
      }
    } else {
      when (protectionEndUnixSecond) {
        null -> {
          TextResource.fromResId(
            R.string.uv_value_protection_from,
            TextResource.fromUnixSecondToShortTime(protectionStartUnixSecond, timeZone)
          )
        }
        protectionStartUnixSecond -> {
          TextResource.fromResId(
            R.string.uv_value_protection_at,
            TextResource.fromUnixSecondToShortTime(protectionStartUnixSecond, timeZone)
          )
        }
        else -> {
          TextResource.fromResId(
            R.string.uv_value_protection_from_until,
            TextResource.fromUnixSecondToShortTime(protectionStartUnixSecond, timeZone),
            TextResource.fromUnixSecondToShortTime(protectionEndUnixSecond, timeZone)
          )
        }
      }
    }
  )
}