package hr.dtakac.prognoza.dbmodel

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import hr.dtakac.prognoza.database.converter.ForecastMetaDateTimeConverter
import java.time.ZonedDateTime

@Entity
@TypeConverters(ForecastMetaDateTimeConverter::class)
data class ForecastMeta(
    @PrimaryKey
    val placeId: String,
    val expires: ZonedDateTime?,
    val lastModified: ZonedDateTime?
)