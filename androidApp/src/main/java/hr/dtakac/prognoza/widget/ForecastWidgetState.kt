package hr.dtakac.prognoza.widget

import hr.dtakac.prognoza.shared.entity.Description
import hr.dtakac.prognoza.shared.entity.Temperature
import hr.dtakac.prognoza.shared.entity.TemperatureUnit
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

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
        val description: Description,
        val hours: List<WidgetHour>
    ) : ForecastWidgetState
}

@Serializable
data class WidgetHour(
    val epochMillis: Long,
    @Serializable(with = TemperatureSerializer::class)
    val temperature: Temperature,
    val description: Description
)

private object TemperatureSerializer : KSerializer<Temperature> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("temperature", PrimitiveKind.DOUBLE)

    override fun deserialize(decoder: Decoder): Temperature {
        return Temperature(value = decoder.decodeDouble(), unit = TemperatureUnit.DEGREE_CELSIUS)
    }

    override fun serialize(encoder: Encoder, value: Temperature) {
        encoder.encodeDouble(value.celsius)
    }
}