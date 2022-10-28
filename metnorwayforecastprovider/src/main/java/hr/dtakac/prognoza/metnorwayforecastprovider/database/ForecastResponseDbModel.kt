package hr.dtakac.prognoza.metnorwayforecastprovider.database

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "forecast_response",
    primaryKeys = ["latitude", "longitude"]
)
data class ForecastResponseDbModel(
    @ColumnInfo(name = "latitude")
    val latitude: Double,
    @ColumnInfo(name = "longitude")
    val longitude: Double,
    @ColumnInfo(name = "json")
    val json: String
)