package hr.dtakac.prognoza.places.mapping

import hr.dtakac.prognoza.core.model.database.Place
import hr.dtakac.prognoza.core.utils.shortenedName
import hr.dtakac.prognoza.places.model.PlaceUiModel

fun Place.toPlaceUiModel(
    isSaved: Boolean,
    isSelected: Boolean
) = PlaceUiModel(
    id = id,
    name = shortenedName,
    fullName = fullName,
    isSaved = isSaved,
    isSelected = isSelected
)