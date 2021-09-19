package hr.dtakac.prognoza.entity

import androidx.room.Entity
import androidx.room.TypeConverters
import hr.dtakac.prognoza.database.converter.ForecastTimeSpanDateTimeConverter
import java.time.ZonedDateTime

@Entity(primaryKeys = ["startTime", "placeId"])
@TypeConverters(ForecastTimeSpanDateTimeConverter::class)
data class ForecastTimeSpan(
    val startTime: ZonedDateTime,
    val endTime: ZonedDateTime?,
    val placeId: String,
    val instantTemperature: Double?,
    val symbolCode: String?,
    val precipitationProbability: Double?,
    val precipitationAmount: Double?,
    val instantWindSpeed: Double?,
    val instantWindFromDirection: Double?,
    val instantRelativeHumidity: Double?,
    val instantAirPressureAtSeaLevel: Double?,
    val airTemperatureMax: Double?,
    val airTemperatureMin: Double?
)