package hr.dtakac.prognoza.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Place(
    @PrimaryKey
    val id: String,
    val fullName: String,
    val latitude: Double,
    val longitude: Double,
)