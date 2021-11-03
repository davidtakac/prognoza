package hr.dtakac.prognoza.common.utils

import hr.dtakac.prognoza.model.database.Place
import hr.dtakac.prognoza.model.ui.cell.PlaceUiModel

val Place.shortenedName get() = fullName.split(", ").getOrNull(0) ?: fullName

fun Place.toPlaceUiModel(isSaved: Boolean, isSelected: Boolean) =
    PlaceUiModel(
        id = id,
        name = shortenedName,
        fullName = fullName,
        isSaved = isSaved,
        isSelected = isSelected
    )