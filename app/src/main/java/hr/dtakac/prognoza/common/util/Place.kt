package hr.dtakac.prognoza.common.util

import hr.dtakac.prognoza.database.entity.Place
import hr.dtakac.prognoza.places.PlaceUiModel

val Place.shortenedName get() = fullName.split(", ").getOrNull(0) ?: fullName

fun Place.toPlaceUiModel(isSaved: Boolean, isSelected: Boolean) =
    PlaceUiModel(
        id = id,
        name = shortenedName,
        fullName = fullName,
        isSaved = isSaved,
        isSelected = isSelected
    )