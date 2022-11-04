package hr.dtakac.prognoza.metnorwayforecastprovider

import hr.dtakac.prognoza.entities.forecast.ForecastDatum
import hr.dtakac.prognoza.entities.forecast.ForecastDescription
import hr.dtakac.prognoza.entities.forecast.units.*
import hr.dtakac.prognoza.entities.forecast.Wind
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.ZonedDateTime

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

fun mapAdjacentTimeStepsToEntity(
    current: ForecastTimeStep,
    next: ForecastTimeStep?
): ForecastDatum? {
    val endTime = next?.time?.let { ZonedDateTime.parse(it) } ?: return null
    val startTime = ZonedDateTime.parse(current.time)

    val details = when (endTime) {
        startTime.plusHours(1) -> {
            current.data.next1Hours
        }
        startTime.plusHours(6) -> {
            current.data.next6Hours
        }
        startTime.plusHours(12) -> {
            current.data.next12Hours
        }
        else -> {
            null
        }
    }

    return ForecastDatum(
        start = startTime,
        end = endTime,
        temperature = Temperature(
            value = current.data.instant.data.airTemperature,
            unit = TemperatureUnit.C
        ),
        description = details?.summary?.symbolCode?.let {
            mapToForecastDescription(it)
        } ?: ForecastDescription.UNKNOWN,
        precipitation = Length(
            value = details?.data?.precipitationAmount ?: 0.0,
            unit = LengthUnit.MM
        ),
        wind = Wind(
            speed = Speed(
                value = current.data.instant.data.windSpeed,
                unit = SpeedUnit.MPS
            ),
            fromDirection = Angle(
                value = current.data.instant.data.windFromDirection,
                unit = AngleUnit.DEG
            )
        ),
        humidity = Percentage(
            value = current.data.instant.data.relativeHumidity,
            unit = PercentageUnit.PERCENT
        ),
        airPressure = Pressure(
            value = current.data.instant.data.airPressureAtSeaLevel,
            unit = PressureUnit.HPA
        )
    )
}

private fun mapToForecastDescription(symbolCode: String): ForecastDescription = when (symbolCode) {
    "clearsky_day" -> ForecastDescription.CLEAR_SKY_DAY
    "clearsky_night" -> ForecastDescription.CLEAR_SKY_NIGHT
    "clearsky_polartwilight" -> ForecastDescription.CLEAR_SKY_POLAR_TWILIGHT
    "cloudy" -> ForecastDescription.CLOUDY
    "fair_day" -> ForecastDescription.FAIR_DAY
    "fair_night" -> ForecastDescription.FAIR_NIGHT
    "fair_polartwilight" -> ForecastDescription.FAIR_POLAR_TWILIGHT
    "fog" -> ForecastDescription.FOG
    "heavyrainandthunder" -> ForecastDescription.HEAVY_RAIN_AND_THUNDER
    "heavyrain" -> ForecastDescription.HEAVY_RAIN
    "heavyrainshowersandthunder_day" -> ForecastDescription.HEAVY_RAIN_SHOWERS_AND_THUNDER_DAY
    "heavyrainshowersandthunder_night" -> ForecastDescription.HEAVY_RAIN_SHOWERS_AND_THUNDER_NIGHT
    "heavyrainshowersandthunder_polartwilight" -> ForecastDescription.HEAVY_RAIN_SHOWERS_AND_THUNDER_POLAR_TWILIGHT
    "heavyrainshowers_day" -> ForecastDescription.HEAVY_RAIN_SHOWERS_DAY
    "heavyrainshowers_night" -> ForecastDescription.HEAVY_RAIN_SHOWERS_NIGHT
    "heavyrainshowers_polartwilight" -> ForecastDescription.HEAVY_RAIN_SHOWERS_POLAR_TWILIGHT
    "heavysleetandthunder" -> ForecastDescription.HEAVY_SLEET_AND_THUNDER
    "heavysleet" -> ForecastDescription.HEAVY_SLEET
    "heavysleetshowersandthunder_day" -> ForecastDescription.HEAVY_SLEET_SHOWERS_AND_THUNDER_DAY
    "heavysleetshowersandthunder_night" -> ForecastDescription.HEAVY_SLEET_SHOWERS_AND_THUNDER_NIGHT
    "heavysleetshowersandthunder_polartwilight" -> ForecastDescription.HEAVY_SLEET_SHOWERS_AND_THUNDER_POLAR_TWILIGHT
    "heavysleetshowers_day" -> ForecastDescription.HEAVY_SLEET_SHOWERS_DAY
    "heavysleetshowers_night" -> ForecastDescription.HEAVY_SLEET_SHOWERS_NIGHT
    "heavysleetshowers_polartwilight" -> ForecastDescription.HEAVY_SLEET_SHOWERS_POLAR_TWILIGHT
    "heavysnowandthunder" -> ForecastDescription.HEAVY_SNOW_AND_THUNDER
    "heavysnow" -> ForecastDescription.HEAVY_SNOW
    "heavysnowshowersandthunder_day" -> ForecastDescription.HEAVY_SNOW_SHOWERS_AND_THUNDER_DAY
    "heavysnowshowersandthunder_night" -> ForecastDescription.HEAVY_SNOW_SHOWERS_AND_THUNDER_NIGHT
    "heavysnowshowersandthunder_polartwilight" -> ForecastDescription.HEAVY_SNOW_SHOWERS_AND_THUNDER_POLAR_TWILIGHT
    "heavysnowshowers_day" -> ForecastDescription.HEAVY_SNOW_SHOWERS_DAY
    "heavysnowshowers_night" -> ForecastDescription.HEAVY_SNOW_SHOWERS_NIGHT
    "heavysnowshowers_polartwilight" -> ForecastDescription.HEAVY_SNOW_SHOWERS_POLAR_TWILIGHT
    "lightrainandthunder" -> ForecastDescription.LIGHT_RAIN_AND_THUNDER
    "lightrain" -> ForecastDescription.LIGHT_RAIN
    "lightrainshowersandthunder_day" -> ForecastDescription.LIGHT_RAIN_SHOWERS_AND_THUNDER_DAY
    "lightrainshowersandthunder_night" -> ForecastDescription.LIGHT_RAIN_SHOWERS_AND_THUNDER_NIGHT
    "lightrainshowersandthunder_polartwilight" -> ForecastDescription.LIGHT_RAIN_SHOWERS_AND_THUNDER_POLAR_TWILIGHT
    "lightrainshowers_day" -> ForecastDescription.LIGHT_RAIN_SHOWERS_DAY
    "lightrainshowers_night" -> ForecastDescription.LIGHT_RAIN_SHOWERS_NIGHT
    "lightrainshowers_polartwilight" -> ForecastDescription.LIGHT_RAIN_SHOWERS_POLAR_TWILIGHT
    "lightsleetandthunder" -> ForecastDescription.LIGHT_SLEET_AND_THUNDER
    "lightsleet" -> ForecastDescription.LIGHT_SLEET
    "lightsleetshowers_day" -> ForecastDescription.LIGHT_SLEET_SHOWERS_DAY
    "lightsleetshowers_night" -> ForecastDescription.LIGHT_SLEET_SHOWERS_NIGHT
    "lightsleetshowers_polartwilight" -> ForecastDescription.LIGHT_SLEET_SHOWERS_POLAR_TWILIGHT
    "lightsnowandthunder" -> ForecastDescription.LIGHT_SNOW_AND_THUNDER
    "lightsnow" -> ForecastDescription.LIGHT_SNOW
    "lightsnowshowers_day" -> ForecastDescription.LIGHT_SNOW_SHOWERS_DAY
    "lightsnowshowers_night" -> ForecastDescription.LIGHT_SNOW_SHOWERS_NIGHT
    "lightsnowshowers_polartwilight" -> ForecastDescription.LIGHT_SNOW_SHOWERS_POLAR_TWILIGHT
    "lightssleetshowersandthunder_day" -> ForecastDescription.LIGHT_SLEET_SHOWERS_AND_THUNDER_DAY
    "lightssleetshowersandthunder_night" -> ForecastDescription.LIGHT_SLEET_SHOWERS_AND_THUNDER_NIGHT
    "lightssleetshowersandthunder_polartwilight" -> ForecastDescription.LIGHT_SLEET_SHOWERS_AND_THUNDER_POLAR_TWILIGHT
    "lightssnowshowersandthunder_day" -> ForecastDescription.LIGHT_SNOW_SHOWERS_AND_THUNDER_DAY
    "lightssnowshowersandthunder_night" -> ForecastDescription.LIGHT_SNOW_SHOWERS_AND_THUNDER_NIGHT
    "lightssnowshowersandthunder_polartwilight" -> ForecastDescription.LIGHT_SNOW_SHOWERS_AND_THUNDER_POLAR_TWILIGHT
    "partlycloudy_day" -> ForecastDescription.PARTLY_CLOUDY_DAY
    "partlycloudy_night" -> ForecastDescription.PARTLY_CLOUDY_NIGHT
    "partlycloudy_polartwilight" -> ForecastDescription.PARTLY_CLOUDY_POLAR_TWILIGHT
    "rainandthunder" -> ForecastDescription.RAIN_AND_THUNDER
    "rain" -> ForecastDescription.RAIN
    "rainshowersandthunder_day" -> ForecastDescription.RAIN_SHOWERS_AND_THUNDER_DAY
    "rainshowersandthunder_night" -> ForecastDescription.RAIN_SHOWERS_AND_THUNDER_NIGHT
    "rainshowersandthunder_polartwilight" -> ForecastDescription.RAIN_SHOWERS_AND_THUNDER_POLAR_TWILIGHT
    "rainshowers_day" -> ForecastDescription.RAIN_SHOWERS_DAY
    "rainshowers_night" -> ForecastDescription.RAIN_SHOWERS_NIGHT
    "rainshowers_polartwilight" -> ForecastDescription.RAIN_SHOWERS_POLAR_TWILIGHT
    "sleetandthunder" -> ForecastDescription.SLEET_AND_THUNDER
    "sleet" -> ForecastDescription.SLEET
    "sleetshowersandthunder_day" -> ForecastDescription.SLEET_SHOWERS_AND_THUNDER_DAY
    "sleetshowersandthunder_night" -> ForecastDescription.SLEET_SHOWERS_AND_THUNDER_NIGHT
    "sleetshowersandthunder_polartwilight" -> ForecastDescription.SLEET_SHOWERS_AND_THUNDER_POLAR_TWILIGHT
    "sleetshowers_day" -> ForecastDescription.SLEET_SHOWERS_DAY
    "sleetshowers_night" -> ForecastDescription.SLEET_SHOWERS_NIGHT
    "sleetshowers_polartwilight" -> ForecastDescription.SLEET_SHOWERS_POLAR_TWILIGHT
    "snowandthunder" -> ForecastDescription.SNOW_AND_THUNDER
    "snow" -> ForecastDescription.SNOW
    "snowshowersandthunder_day" -> ForecastDescription.SNOW_SHOWERS_AND_THUNDER_DAY
    "snowshowersandthunder_night" -> ForecastDescription.SNOW_SHOWERS_AND_THUNDER_NIGHT
    "snowshowersandthunder_polartwilight" -> ForecastDescription.SNOW_SHOWERS_AND_THUNDER_POLAR_TWILIGHT
    "snowshowers_day" -> ForecastDescription.SNOW_SHOWERS_DAY
    "snowshowers_night" -> ForecastDescription.SNOW_SHOWERS_NIGHT
    "snowshowers_polartwilight" -> ForecastDescription.SNOW_SHOWERS_POLAR_TWILIGHT
    else -> ForecastDescription.UNKNOWN
}