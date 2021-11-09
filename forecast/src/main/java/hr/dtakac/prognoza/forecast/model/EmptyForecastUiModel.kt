package hr.dtakac.prognoza.forecast.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable

@Immutable
data class EmptyForecastUiModel(
    @StringRes val reason: Int?
)