package hr.dtakac.prognoza.core.model.database

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.TypeConverters
import hr.dtakac.prognoza.core.database.converter.ForecastInstantDateTimeConverter
import java.time.ZonedDateTime

@Entity(primaryKeys = ["time", "placeId"])
@TypeConverters(ForecastInstantDateTimeConverter::class)
data class ForecastInstant(
    val time: ZonedDateTime,
    val placeId: String,
    val temperature: Double?,
    val windSpeed: Double?,
    val windFromDirection: Double?,
    val relativeHumidity: Double?,
    val airPressure: Double?,

    @Embedded(prefix = "nextOneHours")
    val nextOneHours: NextOneHours?,
    @Embedded(prefix = "nextSixHours")
    val nextSixHours: NextSixHours?,
    @Embedded(prefix = "nextTwelveHours")
    val nextTwelveHours: NextTwelveHours?
)

data class NextOneHours(
    val precipitationAmount: Double,
    val symbolCode: String
)

data class NextSixHours(
    val precipitationAmount: Double,
    val symbolCode: String
)

data class NextTwelveHours(
    val symbolCode: String
)