package hr.dtakac.prognoza.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Place(
    @PrimaryKey
    val id: String,
    val name: String,
    val fullName: String,
    val latitude: Float,
    val longitude: Float
)