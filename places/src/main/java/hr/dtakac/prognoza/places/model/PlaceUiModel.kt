package hr.dtakac.prognoza.places.model

import androidx.compose.runtime.Immutable

@Immutable
data class PlaceUiModel(
    val id: String,
    val name: String,
    val fullName: String,
    val isSaved: Boolean,
    val isSelected: Boolean,
)