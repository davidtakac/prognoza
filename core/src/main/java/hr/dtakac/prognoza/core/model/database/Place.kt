package hr.dtakac.prognoza.core.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import hr.dtakac.prognoza.core.utils.INVALID_PLACE_ID

@Entity
data class Place(
    @PrimaryKey
    val id: String = INVALID_PLACE_ID,
    val fullName: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
)