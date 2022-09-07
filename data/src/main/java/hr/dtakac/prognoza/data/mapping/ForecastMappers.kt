package hr.dtakac.prognoza.data.mapping

import hr.dtakac.prognoza.data.database.forecast.model.ForecastDbModel
import hr.dtakac.prognoza.data.network.forecast.ForecastTimeStep
import hr.dtakac.prognoza.entities.forecast.ForecastDescription.*
import hr.dtakac.prognoza.entities.forecast.units.*
import hr.dtakac.prognoza.entities.forecast.wind.Wind
import java.time.ZonedDateTime

fun mapResponseToDbModel(
    current: ForecastTimeStep,
    next: ForecastTimeStep?,
    latitude: Double,
    longitude: Double
): ForecastDbModel? {
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

    return ForecastDbModel(
        startTime = startTime,
        endTime = endTime,
        latitude = latitude,
        longitude = longitude,
        temperature = Temperature(
            value = current.data.instant.data.airTemperature,
            unit = TemperatureUnit.C
        ),
        forecastDescription = details?.summary?.symbolCode?.let {
            symbolCodeToForecastDescription[it]!!
        } ?: UNKNOWN,
        precipitation = Length(
            value = details?.data?.precipitationAmount ?: 0.0,
            unit = LengthUnit.MM
        ),
        wind = Speed(
            value = current.data.instant.data.windSpeed,
            unit = SpeedUnit.MPS
        ),
        windFromDirection = Angle(
            value = current.data.instant.data.windFromDirection,
            unit = AngleUnit.DEG
        ),
        humidity = Percentage(
            value = current.data.instant.data.relativeHumidity,
            unit = PercentageUnit.PERCENT
        ),
        airPressureAtSeaLevel = Pressure(
            value = current.data.instant.data.airPressureAtSeaLevel,
            unit = PressureUnit.HPA
        )
    )
}

fun mapDbModelToEntity(dbModel: ForecastDbModel): hr.dtakac.prognoza.entities.forecast.ForecastDatum {
    return hr.dtakac.prognoza.entities.forecast.ForecastDatum(
        start = dbModel.startTime,
        end = dbModel.endTime,
        temperature = dbModel.temperature,
        precipitation = dbModel.precipitation,
        wind = Wind(speed = dbModel.wind, fromDirection = dbModel.windFromDirection),
        airPressureAtSeaLevel = dbModel.airPressureAtSeaLevel,
        description = dbModel.forecastDescription,
        humidity = dbModel.humidity
    )
}

private val symbolCodeToForecastDescription = mapOf(
    "clearsky_day" to CLEAR_SKY_DAY,
    "clearsky_night" to CLEAR_SKY_NIGHT,
    "clearsky_polartwilight" to CLEAR_SKY_POLAR_TWILIGHT,
    "cloudy" to CLOUDY,
    "fair_day" to FAIR_DAY,
    "fair_night" to FAIR_NIGHT,
    "fair_polartwilight" to FAIR_POLAR_TWILIGHT,
    "fog" to FOG,
    "heavyrainandthunder" to HEAVY_RAIN_AND_THUNDER,
    "heavyrain" to HEAVY_RAIN,
    "heavyrainshowersandthunder_day" to HEAVY_RAIN_SHOWERS_AND_THUNDER_DAY,
    "heavyrainshowersandthunder_night" to HEAVY_RAIN_SHOWERS_AND_THUNDER_NIGHT,
    "heavyrainshowersandthunder_polartwilight" to HEAVY_RAIN_SHOWERS_AND_THUNDER_POLAR_TWILIGHT,
    "heavyrainshowers_day" to HEAVY_RAIN_SHOWERS_DAY,
    "heavyrainshowers_night" to HEAVY_RAIN_SHOWERS_NIGHT,
    "heavyrainshowers_polartwilight" to HEAVY_RAIN_SHOWERS_POLAR_TWILIGHT,
    "heavysleetandthunder" to HEAVY_SLEET_AND_THUNDER,
    "heavysleet" to HEAVY_SLEET,
    "heavysleetshowersandthunder_day" to HEAVY_SLEET_SHOWERS_AND_THUNDER_DAY,
    "heavysleetshowersandthunder_night" to HEAVY_SLEET_SHOWERS_AND_THUNDER_NIGHT,
    "heavysleetshowersandthunder_polartwilight" to HEAVY_SLEET_SHOWERS_AND_THUNDER_POLAR_TWILIGHT,
    "heavysleetshowers_day" to HEAVY_SLEET_SHOWERS_DAY,
    "heavysleetshowers_night" to HEAVY_SLEET_SHOWERS_NIGHT,
    "heavysleetshowers_polartwilight" to HEAVY_SLEET_SHOWERS_POLAR_TWILIGHT,
    "heavysnowandthunder" to HEAVY_SNOW_AND_THUNDER,
    "heavysnow" to HEAVY_SNOW,
    "heavysnowshowersandthunder_day" to HEAVY_SNOW_SHOWERS_AND_THUNDER_DAY,
    "heavysnowshowersandthunder_night" to HEAVY_SNOW_SHOWERS_AND_THUNDER_NIGHT,
    "heavysnowshowersandthunder_polartwilight" to HEAVY_SNOW_SHOWERS_AND_THUNDER_POLAR_TWILIGHT,
    "heavysnowshowers_day" to HEAVY_SNOW_SHOWERS_DAY,
    "heavysnowshowers_night" to HEAVY_SNOW_SHOWERS_NIGHT,
    "heavysnowshowers_polartwilight" to HEAVY_SNOW_SHOWERS_POLAR_TWILIGHT,
    "lightrainandthunder" to LIGHT_RAIN_AND_THUNDER,
    "lightrain" to LIGHT_RAIN,
    "lightrainshowersandthunder_day" to LIGHT_RAIN_SHOWERS_AND_THUNDER_DAY,
    "lightrainshowersandthunder_night" to LIGHT_RAIN_SHOWERS_AND_THUNDER_NIGHT,
    "lightrainshowersandthunder_polartwilight" to LIGHT_RAIN_SHOWERS_AND_THUNDER_POLAR_TWILIGHT,
    "lightrainshowers_day" to LIGHT_RAIN_SHOWERS_DAY,
    "lightrainshowers_night" to LIGHT_RAIN_SHOWERS_NIGHT,
    "lightrainshowers_polartwilight" to LIGHT_RAIN_SHOWERS_POLAR_TWILIGHT,
    "lightsleetandthunder" to LIGHT_SLEET_AND_THUNDER,
    "lightsleet" to LIGHT_SLEET,
    "lightsleetshowers_day" to LIGHT_SLEET_SHOWERS_DAY,
    "lightsleetshowers_night" to LIGHT_SLEET_SHOWERS_NIGHT,
    "lightsleetshowers_polartwilight" to LIGHT_SLEET_SHOWERS_POLAR_TWILIGHT,
    "lightsnowandthunder" to LIGHT_SNOW_AND_THUNDER,
    "lightsnow" to LIGHT_SNOW,
    "lightsnowshowers_day" to LIGHT_SNOW_SHOWERS_DAY,
    "lightsnowshowers_night" to LIGHT_SNOW_SHOWERS_NIGHT,
    "lightsnowshowers_polartwilight" to LIGHT_SNOW_SHOWERS_POLAR_TWILIGHT,
    "lightssleetshowersandthunder_day" to LIGHT_SLEET_SHOWERS_AND_THUNDER_DAY,
    "lightssleetshowersandthunder_night" to LIGHT_SLEET_SHOWERS_AND_THUNDER_NIGHT,
    "lightssleetshowersandthunder_polartwilight" to LIGHT_SLEET_SHOWERS_AND_THUNDER_POLAR_TWILIGHT,
    "lightssnowshowersandthunder_day" to LIGHT_SNOW_SHOWERS_AND_THUNDER_DAY,
    "lightssnowshowersandthunder_night" to LIGHT_SNOW_SHOWERS_AND_THUNDER_NIGHT,
    "lightssnowshowersandthunder_polartwilight" to LIGHT_SNOW_SHOWERS_AND_THUNDER_POLAR_TWILIGHT,
    "partlycloudy_day" to PARTLY_CLOUDY_DAY,
    "partlycloudy_night" to PARTLY_CLOUDY_NIGHT,
    "partlycloudy_polartwilight" to PARTLY_CLOUDY_POLAR_TWILIGHT,
    "rainandthunder" to RAIN_AND_THUNDER,
    "rain" to RAIN,
    "rainshowersandthunder_day" to RAIN_SHOWERS_AND_THUNDER_DAY,
    "rainshowersandthunder_night" to RAIN_SHOWERS_AND_THUNDER_NIGHT,
    "rainshowersandthunder_polartwilight" to RAIN_SHOWERS_AND_THUNDER_POLAR_TWILIGHT,
    "rainshowers_day" to RAIN_SHOWERS_DAY,
    "rainshowers_night" to RAIN_SHOWERS_NIGHT,
    "rainshowers_polartwilight" to RAIN_SHOWERS_POLAR_TWILIGHT,
    "sleetandthunder" to SLEET_AND_THUNDER,
    "sleet" to SLEET,
    "sleetshowersandthunder_day" to SLEET_SHOWERS_AND_THUNDER_DAY,
    "sleetshowersandthunder_night" to SLEET_SHOWERS_AND_THUNDER_NIGHT,
    "sleetshowersandthunder_polartwilight" to SLEET_SHOWERS_AND_THUNDER_POLAR_TWILIGHT,
    "sleetshowers_day" to SLEET_SHOWERS_DAY,
    "sleetshowers_night" to SLEET_SHOWERS_NIGHT,
    "sleetshowers_polartwilight" to SLEET_SHOWERS_POLAR_TWILIGHT,
    "snowandthunder" to SNOW_AND_THUNDER,
    "snow" to SNOW,
    "snowshowersandthunder_day" to SNOW_SHOWERS_AND_THUNDER_DAY,
    "snowshowersandthunder_night" to SNOW_SHOWERS_AND_THUNDER_NIGHT,
    "snowshowersandthunder_polartwilight" to SNOW_SHOWERS_AND_THUNDER_POLAR_TWILIGHT,
    "snowshowers_day" to SNOW_SHOWERS_DAY,
    "snowshowers_night" to SNOW_SHOWERS_NIGHT,
    "snowshowers_polartwilight" to SNOW_SHOWERS_POLAR_TWILIGHT
)