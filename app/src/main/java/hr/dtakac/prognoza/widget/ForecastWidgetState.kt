package hr.dtakac.prognoza.widget

import hr.dtakac.prognoza.entities.forecast.ForecastDescription
import hr.dtakac.prognoza.entities.forecast.units.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.Instant
import java.time.ZoneOffset
import java.time.ZonedDateTime

@Serializable
sealed interface ForecastWidgetState {
    @Serializable
    object Error : ForecastWidgetState

    @Serializable
    object Loading : ForecastWidgetState

    @Serializable
    object Unavailable : ForecastWidgetState

    @Serializable
    data class Success(
        val placeName: String,
        val temperatureUnit: TemperatureUnit,
        @Serializable(with = TemperatureSerializer::class)
        val temperature: Temperature,
        @Serializable(with = TemperatureSerializer::class)
        val lowTemperature: Temperature,
        @Serializable(with = TemperatureSerializer::class)
        val highTemperature: Temperature,
        val description: ForecastDescription,
        val hours: List<WidgetHour>
    ) : ForecastWidgetState
}

@Serializable
data class WidgetHour(
    @Serializable(with = ZonedDateTimeSerializer::class)
    val dateTime: ZonedDateTime,
    @Serializable(with = TemperatureSerializer::class)
    val temperature: Temperature,
    val description: ForecastDescription
)

private object TemperatureSerializer : KSerializer<Temperature> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("temperature", PrimitiveKind.DOUBLE)

    override fun deserialize(decoder: Decoder): Temperature {
        return Temperature(value = decoder.decodeDouble(), unit = TemperatureUnit.C)
    }

    override fun serialize(encoder: Encoder, value: Temperature) {
        encoder.encodeDouble(value.celsius)
    }
}

private object ZonedDateTimeSerializer : KSerializer<ZonedDateTime> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("zoned_date_time", PrimitiveKind.LONG)

    override fun deserialize(decoder: Decoder): ZonedDateTime {
        return Instant.ofEpochMilli(decoder.decodeLong()).atZone(ZoneOffset.UTC)
    }

    override fun serialize(encoder: Encoder, value: ZonedDateTime) {
        encoder.encodeLong(value.toInstant().toEpochMilli())
    }
}