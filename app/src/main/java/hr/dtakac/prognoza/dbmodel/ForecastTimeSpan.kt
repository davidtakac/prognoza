package hr.dtakac.prognoza.dbmodel

import androidx.room.Entity
import androidx.room.TypeConverters
import hr.dtakac.prognoza.database.converter.ForecastTimeSpanDateTimeConverter
import java.time.ZonedDateTime

@Entity(primaryKeys = ["startTime", "placeId"])
@TypeConverters(ForecastTimeSpanDateTimeConverter::class)
data class ForecastTimeSpan(
    val startTime: ZonedDateTime,
    val placeId: String,
    val instantTemperature: Float?,
    val symbolCode: String?,
    val precipitationProbability: Float?,
    val precipitationAmount: Float?,
    val instantWindSpeed: Float?,
    val instantWindFromDirection: Float?,
    val instantRelativeHumidity: Float?,
    val instantAirPressureAtSeaLevel: Float?,
    val airTemperatureMax: Float?,
    val airTemperatureMin: Float?
)