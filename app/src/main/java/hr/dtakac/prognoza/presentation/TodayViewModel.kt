package hr.dtakac.prognoza.presentation

import android.text.format.DateUtils
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.domain.usecases.TodayForecastResult
import hr.dtakac.prognoza.domain.usecases.GetTodayForecast
import hr.dtakac.prognoza.entities.forecast.precipitation.PrecipitationDescription
import hr.dtakac.prognoza.entities.forecast.units.LengthUnit
import hr.dtakac.prognoza.entities.forecast.units.SpeedUnit
import hr.dtakac.prognoza.entities.forecast.units.TemperatureUnit
import hr.dtakac.prognoza.presentation.strings.TextResource
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodayViewModel @Inject constructor(
    private val getTodayForecast: GetTodayForecast
) : ViewModel() {

    private val _state: MutableState<TodayUiState> = mutableStateOf(TodayUiState.Loading)
    val state: State<TodayUiState> get() = _state

    fun getState() {
        viewModelScope.launch {
            _state.value = TodayUiState.Loading
            _state.value = when (val todayForecastResult = getTodayForecast()) {
                is TodayForecastResult.Success -> mapToUiState(todayForecastResult)
                TodayForecastResult.ClientError -> TodayUiState.Empty(TextResource.fromStringId(R.string.error_client))
                TodayForecastResult.DatabaseError -> TodayUiState.Empty(TextResource.fromStringId(R.string.error_database))
                TodayForecastResult.NoSelectedPlace -> TodayUiState.Empty(TextResource.fromStringId(R.string.error_no_selected_place))
                TodayForecastResult.ServerError -> TodayUiState.Empty(TextResource.fromStringId(R.string.error_server))
                TodayForecastResult.ThrottleError -> TodayUiState.Empty(TextResource.fromStringId(R.string.error_throttling))
                TodayForecastResult.UnknownError -> TodayUiState.Empty(TextResource.fromStringId(R.string.error_unknown))
            }
        }
    }

    private fun mapToUiState(
        success: TodayForecastResult.Success
    ): TodayUiState.Success = TodayUiState.Success(
        placeName = TextResource.fromText(success.placeName),
        time = TextResource.fromStringIdWithArgs(
            id = R.string.template_today_time,
            TextResource.fromEpochMillis(
                millis = success.todayForecast.time.toInstant().toEpochMilli(),
                flags = DateUtils.FORMAT_SHOW_TIME or DateUtils.FORMAT_ABBREV_TIME
            )
        ),
        temperature = TextResource.fromStringIdWithArgs(
            id = R.string.template_temperature_degrees,
            TextResource.fromNumber(
                success.todayForecast.airTemperature.run {
                    when (success.temperatureUnit) {
                        TemperatureUnit.C -> celsius
                        TemperatureUnit.F -> fahrenheit
                    }
                }
            )
        ),
        feelsLike = TextResource.fromStringIdWithArgs(
            id = R.string.template_feels_like_degrees,
            TextResource.fromNumber(
                success.todayForecast.feelsLikeTemperature.run {
                    when (success.temperatureUnit) {
                        TemperatureUnit.C -> celsius
                        TemperatureUnit.F -> fahrenheit
                    }
                }
            )
        ),
        wind = TextResource.fromStringIdWithArgs(
            id = R.string.template_wind,
            TextResource.fromStringId(success.todayForecast.wind.description.toStringId()),
            TextResource.fromStringId(success.todayForecast.wind.fromDirection.toCompassDirectionStringId()),
            TextResource.fromStringIdWithArgs(
                id = when (success.windUnit) {
                    SpeedUnit.KPH -> R.string.template_wind_kmh
                    SpeedUnit.MPH -> R.string.template_wind_mph
                    SpeedUnit.MPS -> R.string.template_wind_mps
                },
                TextResource.fromNumber(
                    success.todayForecast.wind.speed.run {
                        when (success.windUnit) {
                            SpeedUnit.KPH -> kilometersPerHour
                            SpeedUnit.MPH -> milesPerHour
                            SpeedUnit.MPS -> metersPerSecond
                        }
                    }
                )
            )
        ),
        description = TextResource.fromStringId(success.todayForecast.description.toStringId()),
        descriptionIcon = success.todayForecast.description.toDrawableId(),
        lowTemperature = TextResource.fromStringIdWithArgs(
            id = R.string.template_temperature_degrees,
            TextResource.fromNumber(
                success.todayForecast.lowTemperature.run {
                    when (success.temperatureUnit) {
                        TemperatureUnit.C -> celsius
                        TemperatureUnit.F -> fahrenheit
                    }
                }
            )
        ),
        highTemperature = TextResource.fromStringIdWithArgs(
            id = R.string.template_temperature_degrees,
            TextResource.fromNumber(
                success.todayForecast.highTemperature.run {
                    when (success.temperatureUnit) {
                        TemperatureUnit.C -> celsius
                        TemperatureUnit.F -> fahrenheit
                    }
                }
            )
        ),
        precipitation = success.todayForecast.dailyPrecipitation?.let { dailyPrecipitation ->
            if (dailyPrecipitation.precipitation.description == PrecipitationDescription.NONE) {
                TextResource.fromStringId(R.string.precipitation_none)
            } else {
                TextResource.fromStringIdWithArgs(
                    id = R.string.template_precipitation,
                    TextResource.fromStringId(dailyPrecipitation.precipitation.description.toStringId()),
                    TextResource.fromEpochMillis(
                        millis = dailyPrecipitation.at.toInstant().toEpochMilli(),
                        flags = DateUtils.FORMAT_SHOW_TIME or DateUtils.FORMAT_ABBREV_TIME
                    ),
                    TextResource.fromStringIdWithArgs(
                        id = when (success.precipitationUnit) {
                            LengthUnit.MM -> R.string.template_precipitation_mm
                            LengthUnit.IN -> R.string.template_precipitation_in
                        },
                        TextResource.fromNumber(dailyPrecipitation.precipitation.amount.run {
                            when (success.precipitationUnit) {
                                LengthUnit.MM -> millimeters
                                LengthUnit.IN -> inches
                            }
                        }, decimalPlaces = 2)
                    )
                )
            }
        } ?: TextResource.fromStringId(R.string.precipitation_none),
        hours = success.todayForecast.smallData.map { datum ->
            TodayHour(
                time = TextResource.fromEpochMillis(
                    millis = datum.time.toInstant().toEpochMilli(),
                    flags = DateUtils.FORMAT_SHOW_TIME or DateUtils.FORMAT_ABBREV_TIME
                ),
                icon = datum.description.toDrawableId(),
                temperature = TextResource.fromStringIdWithArgs(
                    id = R.string.template_temperature_degrees,
                    TextResource.fromNumber(
                        datum.temperature.run {
                            when (success.temperatureUnit) {
                                TemperatureUnit.C -> celsius
                                TemperatureUnit.F -> fahrenheit
                            }
                        }
                    )
                ),
                precipitation = datum.precipitation.takeIf { it.millimeters > 0 }?.run {
                    TextResource.fromStringIdWithArgs(
                        id = when (success.precipitationUnit) {
                            LengthUnit.MM -> R.string.template_precipitation_mm
                            LengthUnit.IN -> R.string.template_precipitation_in
                        },
                        TextResource.fromNumber(
                            when (success.precipitationUnit) {
                                LengthUnit.MM -> millimeters
                                LengthUnit.IN -> inches
                            },
                            decimalPlaces = 2
                        )
                    )
                }
            )
        }
    )
}