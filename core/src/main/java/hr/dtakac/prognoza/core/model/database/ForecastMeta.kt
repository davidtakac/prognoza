package hr.dtakac.prognoza.core.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import hr.dtakac.prognoza.core.database.converter.ForecastMetaDateTimeConverter
import java.time.ZonedDateTime

@Entity
@TypeConverters(ForecastMetaDateTimeConverter::class)
data class ForecastMeta(
    @PrimaryKey
    val placeId: String,
    val expires: ZonedDateTime?,
    val lastModified: ZonedDateTime?
)