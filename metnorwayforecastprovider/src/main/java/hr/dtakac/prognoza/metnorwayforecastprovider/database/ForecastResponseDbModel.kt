package hr.dtakac.prognoza.metnorwayforecastprovider.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.TypeConverters
import hr.dtakac.prognoza.metnorwayforecastprovider.LocationForecastResponse
import hr.dtakac.prognoza.metnorwayforecastprovider.database.converter.ForecastResponseConverter

@Entity(
    tableName = "forecast_response",
    primaryKeys = ["latitude", "longitude"]
)
@TypeConverters(ForecastResponseConverter::class)
data class ForecastResponseDbModel(
    @ColumnInfo(name = "latitude")
    val latitude: Double,
    @ColumnInfo(name = "longitude")
    val longitude: Double,
    @ColumnInfo(name = "json")
    val response: LocationForecastResponse
)