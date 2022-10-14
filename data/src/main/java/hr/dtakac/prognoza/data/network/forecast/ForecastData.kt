package hr.dtakac.prognoza.data.network.forecast

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LocationForecastResponse(
    @SerialName("properties")
    val forecast: Forecast
)

@Serializable
data class Forecast(
    @SerialName("meta")
    val meta: ForecastMeta,
    @SerialName("timeseries")
    val forecastTimeSteps: List<ForecastTimeStep>
)

@Serializable
data class ForecastTimeStep(
    @SerialName("time")
    val time: String,
    @SerialName("data")
    val data: ForecastTimeStepData
)

@Serializable
data class ForecastTimeStepData(
    @SerialName("instant")
    val instant: ForecastTimeInstant,
    @SerialName("next_1_hours")
    val next1Hours: ForecastTimePeriod? = null,
    @SerialName("next_6_hours")
    val next6Hours: ForecastTimePeriod? = null,
    @SerialName("next_12_hours")
    val next12Hours: ForecastTimePeriod? = null
)

@Serializable
data class ForecastTimeInstant(
    @SerialName("details")
    val data: ForecastInstantData,
)

@Serializable
data class ForecastInstantData(
    @SerialName("air_temperature")
    val airTemperature: Double,
    @SerialName("cloud_area_fraction")
    val cloudAreaFraction: Double,
    @SerialName("wind_from_direction")
    val windFromDirection: Double,
    @SerialName("air_pressure_at_sea_level")
    val airPressureAtSeaLevel: Double,
    @SerialName("relative_humidity")
    val relativeHumidity: Double,
    @SerialName("wind_speed")
    val windSpeed: Double
)

@Serializable
data class ForecastTimePeriod(
    @SerialName("details")
    val data: ForecastTimePeriodData? = null,
    @SerialName("summary")
    val summary: ForecastTimePeriodSummary
)

@Serializable
data class ForecastTimePeriodSummary(
    @SerialName("symbol_code")
    val symbolCode: String
)

@Serializable
data class ForecastTimePeriodData(
    @SerialName("probability_of_thunder")
    val probabilityOfThunder: Double? = null,
    @SerialName("ultraviolet_index_clear_sky_max")
    val ultravioletIndexClearSkyMax: Double? = null,
    @SerialName("air_temperature_min")
    val airTemperatureMin: Double? = null,
    @SerialName("precipitation_amount_min")
    val precipitationAmountMin: Double? = null,
    @SerialName("precipitation_amount_max")
    val precipitationAmountMax: Double? = null,
    @SerialName("precipitation_amount")
    val precipitationAmount: Double? = null,
    @SerialName("air_temperature_max")
    val airTemperatureMax: Double? = null,
    @SerialName("probability_of_precipitation")
    val probabilityOfPrecipitation: Double? = null
)

@Serializable
data class ForecastMeta(
    @SerialName("units")
    val units: ForecastUnits,
    @SerialName("updated_at")
    val updatedAt: String
)

@Serializable
data class ForecastUnits(
    @SerialName("fog_area_fraction")
    val fogAreaFraction: String? = null,
    @SerialName("dew_point_temperature")
    val dewPointTemperature: String? = null,
    @SerialName("air_temperature_min")
    val airTemperatureMin: String? = null,
    @SerialName("relative_humidity")
    val relativeHumidity: String? = null,
    @SerialName("air_temperature_max")
    val airTemperatureMax: String? = null,
    @SerialName("cloud_area_fraction")
    val cloudAreaFraction: String? = null,
    @SerialName("air_pressure_at_sea_level")
    val airPressureAtSeaLevel: String? = null,
    @SerialName("cloud_area_fraction_high")
    val cloudAreaFractionHigh: String? = null,
    @SerialName("cloud_area_fraction_low")
    val cloudAreaFractionLow: String? = null,
    @SerialName("air_temperature")
    val airTemperature: String? = null,
    @SerialName("wind_speed")
    val windSpeed: String? = null,
    @SerialName("precipitation_amount_min")
    val precipitationAmountMin: String? = null,
    @SerialName("precipitation_amount_max")
    val precipitationAmountMax: String? = null,
    @SerialName("precipitation_amount")
    val precipitationAmount: String? = null,
    @SerialName("probability_of_precipitation")
    val probabilityOfPrecipitation: String? = null,
    @SerialName("wind_speed_of_gust")
    val windSpeedOfGust: String? = null,
    @SerialName("ultraviolet_index_clear_sky_max")
    val ultravioletIndexClearSkyMax: Int? = null,
    @SerialName("probability_of_thunder")
    val probabilityOfThunder: String? = null,
    @SerialName("wind_from_direction")
    val windFromDirection: String? = null,
    @SerialName("cloud_area_fraction_medium")
    val cloudAreaFractionMedium: String? = null
)