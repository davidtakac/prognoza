package hr.dtakac.prognoza.forecast.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable

sealed interface EmptyForecastUiModel

@Immutable
data class EmptyForecastBecauseReason(
    @StringRes val reason: Int?
) : EmptyForecastUiModel

@Immutable
object EmptyForecastBecauseNoSelectedPlace : EmptyForecastUiModel