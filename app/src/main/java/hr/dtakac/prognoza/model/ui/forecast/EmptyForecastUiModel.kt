package hr.dtakac.prognoza.model.ui.forecast

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable

@Immutable
data class EmptyForecastUiModel(
    @StringRes val reasonResourceId: Int?
)