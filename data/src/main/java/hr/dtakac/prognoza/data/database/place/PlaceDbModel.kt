package hr.dtakac.prognoza.data.database.place

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "place",
    primaryKeys = ["latitude", "longitude"]
)
data class PlaceDbModel(
    @ColumnInfo(name = "latitude")
    val latitude: Double,
    @ColumnInfo(name = "longitude")
    val longitude: Double,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "details")
    val details: String?
)