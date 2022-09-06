package hr.dtakac.prognoza.utils

import hr.dtakac.prognoza.data.database.place.Place

val hr.dtakac.prognoza.data.database.place.Place.shortenedName get() = fullName.split(", ").getOrNull(0) ?: fullName

fun hr.dtakac.prognoza.data.database.place.Place.toPlaceUiModel(isSaved: Boolean, isSelected: Boolean) =
    PlaceUiModel(
        id = id,
        name = shortenedName,
        fullName = fullName,
        isSaved = isSaved,
        isSelected = isSelected
    )