package hr.dtakac.prognoza.metnorwayforecastprovider.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.TypeConverters
import hr.dtakac.prognoza.metnorwayforecastprovider.database.converter.Rfc1123DateTimeConverter
import java.time.ZonedDateTime

@Entity(
    tableName = "forecast_meta",
    primaryKeys = ["latitude", "longitude"]
)
@TypeConverters(Rfc1123DateTimeConverter::class)
data class ForecastMetaDbModel(
    @ColumnInfo(name = "latitude")
    val latitude: Double,
    @ColumnInfo(name = "longitude")
    val longitude: Double,
    @ColumnInfo(name = "expires")
    val expires: ZonedDateTime?,
    @ColumnInfo(name = "last_modified")
    val lastModified: ZonedDateTime?
)