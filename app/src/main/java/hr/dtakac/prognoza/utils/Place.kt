package hr.dtakac.prognoza.utils

import hr.dtakac.prognoza.entity.Place
import hr.dtakac.prognoza.uimodel.cell.PlaceUiModel

val Place.shortenedName get() = fullName.split(", ").getOrNull(0) ?: fullName

fun Place.toPlaceUiModel(isSaved: Boolean, isSelected: Boolean) =
    PlaceUiModel(
        id = id,
        name = shortenedName,
        fullName = fullName,
        isSaved = isSaved,
        isSelected = isSelected
    )