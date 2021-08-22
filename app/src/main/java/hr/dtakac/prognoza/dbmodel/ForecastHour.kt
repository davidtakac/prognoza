package hr.dtakac.prognoza.dbmodel

import androidx.room.Entity
import androidx.room.TypeConverters
import hr.dtakac.prognoza.database.converter.ForecastHourDateTimeConverter
import java.time.ZonedDateTime

@Entity(primaryKeys = ["time", "placeId"])
@TypeConverters(ForecastHourDateTimeConverter::class)
data class ForecastHour(
    val time: ZonedDateTime,
    val placeId: String,
    /**
     * In degrees Celsius (°C, metric).
     */
    val temperature: Float?,
    val symbolCode: String?,
    val precipitationProbability: Float?,
    /**
     * In millimeters (mm, metric).
     */
    val precipitationAmount: Float?,
    /**
     * In meters per second (m/s, metric).
     */
    val windSpeed: Float?,
    /**
     * In degrees (°, metric).
     */
    val windFromDirection: Float?,
    /**
     * In percentage (%, metric).
     */
    val relativeHumidity: Float?,
    /**
     * In hectopascal (hPa, metric).
     */
    val pressure: Float?
)