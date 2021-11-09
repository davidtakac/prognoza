package hr.dtakac.prognoza.places.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable

@Immutable
data class EmptyPlacesUiModel(
    @StringRes val reason: Int
)