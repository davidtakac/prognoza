package hr.dtakac.prognoza.model.api

import com.google.gson.annotations.SerializedName

data class LocationForecastResponse(
    @SerializedName("properties")
    val forecast: Forecast
)

data class Forecast(
    @SerializedName("meta")
    val meta: ForecastMeta,
    @SerializedName("timeseries")
    val forecastTimeSteps: List<ForecastTimeStep>
)

data class ForecastTimeStep(
    @SerializedName("time")
    val time: String,
    @SerializedName("data")
    val data: ForecastTimeStepData?
)

data class ForecastTimeStepData(
    @SerializedName("instant")
    val instant: ForecastTimeInstant?,
    @SerializedName("next_1_hours")
    val next1Hours: ForecastTimePeriod?,
    @SerializedName("next_6_hours")
    val next6Hours: ForecastTimePeriod?,
    @SerializedName("next_12_hours")
    val next12Hours: ForecastTimePeriod?
)

data class ForecastTimeInstant(
    @SerializedName("details")
    val data: ForecastInstantData,
)

data class ForecastInstantData(
    @SerializedName("air_temperature")
    val airTemperature: Double?,
    @SerializedName("cloud_area_fraction_low")
    val cloudAreaFractionLow: Double?,
    @SerializedName("cloud_area_fraction_high")
    val cloudAreaFractionHigh: Double?,
    @SerializedName("cloud_area_fraction_medium")
    val cloudAreaFractionMedium: Double?,
    @SerializedName("wind_from_direction")
    val windFromDirection: Double?,
    @SerializedName("air_pressure_at_sea_level")
    val airPressureAtSeaLevel: Double?,
    @SerializedName("cloud_area_fraction")
    val cloudAreaFraction: Double?,
    @SerializedName("wind_speed_of_gust")
    val windSpeedOfGust: Double?,
    @SerializedName("relative_humidity")
    val relativeHumidity: Double?,
    @SerializedName("dew_point_temperature")
    val dewPointTemperature: Double?,
    @SerializedName("wind_speed")
    val windSpeed: Double?,
    @SerializedName("fog_area_fraction")
    val fogAreaFraction: Double?
)

data class ForecastTimePeriod(
    @SerializedName("details")
    val data: ForecastTimePeriodData?,
    @SerializedName("summary")
    val summary: ForecastTimePeriodSummary?
)

data class ForecastTimePeriodSummary(
    @SerializedName("symbol_code")
    val symbolCode: String
)

data class ForecastTimePeriodData(
    @SerializedName("probability_of_thunder")
    val probabilityOfThunder: Double?,
    @SerializedName("ultraviolet_index_clear_sky_max")
    val ultravioletIndexClearSkyMax: Double?,
    @SerializedName("air_temperature_min")
    val airTemperatureMin: Double?,
    @SerializedName("precipitation_amount_min")
    val precipitationAmountMin: Double?,
    @SerializedName("precipitation_amount_max")
    val precipitationAmountMax: Double?,
    @SerializedName("precipitation_amount")
    val precipitationAmount: Double?,
    @SerializedName("air_temperature_max")
    val airTemperatureMax: Double?,
    @SerializedName("probability_of_precipitation")
    val probabilityOfPrecipitation: Double?
)

data class ForecastMeta(
    @SerializedName("units")
    val units: ForecastUnits,
    @SerializedName("updated_at")
    val updatedAt: String
)

data class ForecastUnits(
    @SerializedName("fog_area_fraction")
    val fogAreaFraction: String?,
    @SerializedName("dew_point_temperature")
    val dewPointTemperature: String?,
    @SerializedName("air_temperature_min")
    val airTemperatureMin: String?,
    @SerializedName("relative_humidity")
    val relativeHumidity: String?,
    @SerializedName("air_temperature_max")
    val airTemperatureMax: String?,
    @SerializedName("cloud_area_fraction")
    val cloudAreaFraction: String?,
    @SerializedName("air_pressure_at_sea_level")
    val airPressureAtSeaLevel: String?,
    @SerializedName("cloud_area_fraction_high")
    val cloudAreaFractionHigh: String?,
    @SerializedName("cloud_area_fraction_low")
    val cloudAreaFractionLow: String?,
    @SerializedName("air_temperature")
    val airTemperature: String?,
    @SerializedName("wind_speed")
    val windSpeed: String?,
    @SerializedName("precipitation_amount_min")
    val precipitationAmountMin: String?,
    @SerializedName("precipitation_amount_max")
    val precipitationAmountMax: String?,
    @SerializedName("precipitation_amount")
    val precipitationAmount: String?,
    @SerializedName("probability_of_precipitation")
    val probabilityOfPrecipitation: String?,
    @SerializedName("wind_speed_of_gust")
    val windSpeedOfGust: String?,
    @SerializedName("ultraviolet_index_clear_sky_max")
    val ultravioletIndexClearSkyMax: Int,
    @SerializedName("probability_of_thunder")
    val probabilityOfThunder: String?,
    @SerializedName("wind_from_direction")
    val windFromDirection: String?,
    @SerializedName("cloud_area_fraction_medium")
    val cloudAreaFractionMedium: String?
)