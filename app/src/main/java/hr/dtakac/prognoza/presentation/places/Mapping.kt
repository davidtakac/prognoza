package hr.dtakac.prognoza.presentation.places

import hr.dtakac.prognoza.entities.Place
import hr.dtakac.prognoza.presentation.TextResource

fun mapToPlaceUi(
    place: Place,
    isSelected: Boolean
): PlaceUi = PlaceUi(
    name = TextResource.fromText(place.name),
    details = TextResource.fromText(place.details ?: ""),
    isSelected = isSelected
)