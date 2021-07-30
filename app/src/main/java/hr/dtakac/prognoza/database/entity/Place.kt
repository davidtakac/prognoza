package hr.dtakac.prognoza.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Place(
    @PrimaryKey
    val id: String,
    val fullName: String,
    val latitude: Float,
    val longitude: Float,
    val isSaved: Boolean
)

val Place.shortenedName get() = fullName.split(", ").getOrNull(0) ?: fullName