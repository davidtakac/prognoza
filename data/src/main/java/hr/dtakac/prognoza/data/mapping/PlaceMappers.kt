package hr.dtakac.prognoza.data.mapping

import hr.dtakac.prognoza.data.network.place.PlaceResponse
import hr.dtakac.prognoza.data.database.place.PlaceDbModel
import hr.dtakac.prognoza.entities.Place

fun mapDbModelToEntity(dbModel: PlaceDbModel): Place {
    return Place(
        name = dbModel.name,
        details = dbModel.details,
        latitude = dbModel.latitude,
        longitude = dbModel.longitude
    )
}

fun mapEntityToDbModel(entity: Place): PlaceDbModel {
    return PlaceDbModel(
        latitude = entity.latitude,
        longitude = entity.longitude,
        name = entity.name,
        details = entity.details
    )
}

fun mapResponseToEntity(response: PlaceResponse): Place {
    return Place(
        name = response.displayName.split(", ").getOrNull(0)
            ?: response.displayName,
        details = response.displayName,
        latitude = response.latitude,
        longitude = response.longitude
    )
}