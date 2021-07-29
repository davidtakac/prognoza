package hr.dtakac.prognoza.database.entity

import androidx.room.Entity
import androidx.room.TypeConverters
import hr.dtakac.prognoza.database.converter.ForecastHourDateTimeConverter
import java.time.ZonedDateTime

@Entity(primaryKeys = ["time", "placeId"])
@TypeConverters(ForecastHourDateTimeConverter::class)
data class ForecastHour(
    val time: ZonedDateTime,
    val placeId: Long,
    val temperature: Float?,
    val symbolCode: String?,
    val precipitationProbability: Float?,
    val precipitationAmount: Float?,
    val windSpeed: Float?,
)