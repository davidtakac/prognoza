package hr.dtakac.prognoza.presentation

import androidx.annotation.DrawableRes
import hr.dtakac.prognoza.presentation.strings.TextResource
import java.time.ZonedDateTime

sealed interface TodayUiState {
    object Loading : TodayUiState

    data class Empty(
        val text: TextResource
    ) : TodayUiState

    data class Success(
        val placeName: TextResource,
        val time: TextResource,
        val temperature: TextResource,
        val feelsLike: TextResource,
        val wind: TextResource,
        val description: TextResource,
        @DrawableRes
        val descriptionIcon: Int,
        val lowTemperature: TextResource,
        val highTemperature: TextResource,
        val precipitation: TextResource,
        val hours: List<TodayHour>
    ) : TodayUiState
}

data class TodayHour(
    val time: TextResource,
    @DrawableRes
    val icon: Int,
    val temperature: TextResource,
    val precipitation: TextResource?
)