package hr.dtakac.prognoza.extensions

import hr.dtakac.prognoza.dbmodel.Place
import hr.dtakac.prognoza.uimodel.PlaceUiModel

val Place.shortenedName get() = fullName.split(", ").getOrNull(0) ?: fullName

fun Place.toPlaceUiModel(isSaved: Boolean, isSelected: Boolean) =
    PlaceUiModel(
        id = id,
        name = shortenedName,
        fullName = fullName,
        isSaved = isSaved,
        isSelected = isSelected
    )