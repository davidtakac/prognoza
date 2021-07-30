package hr.dtakac.prognoza.api

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
    val data: ForecastTimeStepData
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

fun ForecastTimeStepData.findSymbolCode() =
    next1Hours?.summary?.symbolCode
        ?: next6Hours?.summary?.symbolCode
        ?: next12Hours?.summary?.symbolCode

fun ForecastTimeStepData.findProbabilityOfPrecipitation() =
    next1Hours?.data?.probabilityOfPrecipitation
        ?: next6Hours?.data?.probabilityOfPrecipitation
        ?: next12Hours?.data?.probabilityOfPrecipitation

fun ForecastTimeStepData.findPrecipitationAmount() =
    next1Hours?.data?.precipitationAmount
        ?: next6Hours?.data?.precipitationAmount
        ?: next12Hours?.data?.precipitationAmount

data class ForecastTimeInstant(
    @SerializedName("details")
    val data: ForecastInstantData,
)

data class ForecastInstantData(
    @SerializedName("air_temperature")
    val airTemperature: Float?,
    @SerializedName("cloud_area_fraction_low")
    val cloudAreaFractionLow: Float?,
    @SerializedName("cloud_area_fraction_high")
    val cloudAreaFractionHigh: Float?,
    @SerializedName("cloud_area_fraction_medium")
    val cloudAreaFractionMedium: Float?,
    @SerializedName("wind_from_direction")
    val windFromDirection: Float?,
    @SerializedName("air_pressure_at_sea_level")
    val airPressureAtSeaLevel: Float?,
    @SerializedName("cloud_area_fraction")
    val cloudAreaFraction: Float?,
    @SerializedName("wind_speed_of_gust")
    val windSpeedOfGust: Float?,
    @SerializedName("relative_humidity")
    val relativeHumidity: Float?,
    @SerializedName("dew_point_temperature")
    val dewPointTemperature: Float?,
    @SerializedName("wind_speed")
    val windSpeed: Float?,
    @SerializedName("fog_area_fraction")
    val fogAreaFraction: Float?
)

data class ForecastTimePeriod(
    @SerializedName("details")
    val data: ForecastTimePeriodData,
    @SerializedName("summary")
    val summary: ForecastTimePeriodSummary
)

data class ForecastTimePeriodSummary(
    @SerializedName("symbol_code")
    val symbolCode: String
)

data class ForecastTimePeriodData(
    @SerializedName("probability_of_thunder")
    val probabilityOfThunder: Float?,
    @SerializedName("ultraviolet_index_clear_sky_max")
    val ultravioletIndexClearSkyMax: Float?,
    @SerializedName("air_temperature_min")
    val airTemperatureMin: Float?,
    @SerializedName("precipitation_amount_min")
    val precipitationAmountMin: Float?,
    @SerializedName("precipitation_amount_max")
    val precipitationAmountMax: Float?,
    @SerializedName("precipitation_amount")
    val precipitationAmount: Float?,
    @SerializedName("air_temperature_max")
    val airTemperatureMax: Float?,
    @SerializedName("probability_of_precipitation")
    val probabilityOfPrecipitation: Float?
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