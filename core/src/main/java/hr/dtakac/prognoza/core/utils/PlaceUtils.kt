package hr.dtakac.prognoza.core.utils

import hr.dtakac.prognoza.core.model.database.Place

val Place.shortenedName get() = fullName.split(", ").getOrNull(0) ?: fullName