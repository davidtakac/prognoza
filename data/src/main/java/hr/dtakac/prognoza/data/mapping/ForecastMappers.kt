package hr.dtakac.prognoza.data.mapping

import hr.dtakac.prognoza.data.database.forecast.model.ForecastDbModel
import hr.dtakac.prognoza.data.network.forecast.ForecastTimeStep
import hr.dtakac.prognoza.entities.forecast.ForecastDatum
import hr.dtakac.prognoza.entities.forecast.ForecastDescription
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
            mapToForecastDescription(it)
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

fun mapDbModelToEntity(dbModel: ForecastDbModel): ForecastDatum {
    return ForecastDatum(
        start = dbModel.startTime,
        end = dbModel.endTime,
        temperature = dbModel.temperature,
        precipitation = dbModel.precipitation,
        wind = Wind(speed = dbModel.wind, fromDirection = dbModel.windFromDirection),
        airPressure = dbModel.airPressureAtSeaLevel,
        description = dbModel.forecastDescription,
        humidity = dbModel.humidity
    )
}

private fun mapToForecastDescription(symbolCode: String): ForecastDescription = when (symbolCode) {
    "clearsky_day" -> CLEAR_SKY_DAY
    "clearsky_night" -> CLEAR_SKY_NIGHT
    "clearsky_polartwilight" -> CLEAR_SKY_POLAR_TWILIGHT
    "cloudy" -> CLOUDY
    "fair_day" -> FAIR_DAY
    "fair_night" -> FAIR_NIGHT
    "fair_polartwilight" -> FAIR_POLAR_TWILIGHT
    "fog" -> FOG
    "heavyrainandthunder" -> HEAVY_RAIN_AND_THUNDER
    "heavyrain" -> HEAVY_RAIN
    "heavyrainshowersandthunder_day" -> HEAVY_RAIN_SHOWERS_AND_THUNDER_DAY
    "heavyrainshowersandthunder_night" -> HEAVY_RAIN_SHOWERS_AND_THUNDER_NIGHT
    "heavyrainshowersandthunder_polartwilight" -> HEAVY_RAIN_SHOWERS_AND_THUNDER_POLAR_TWILIGHT
    "heavyrainshowers_day" -> HEAVY_RAIN_SHOWERS_DAY
    "heavyrainshowers_night" -> HEAVY_RAIN_SHOWERS_NIGHT
    "heavyrainshowers_polartwilight" -> HEAVY_RAIN_SHOWERS_POLAR_TWILIGHT
    "heavysleetandthunder" -> HEAVY_SLEET_AND_THUNDER
    "heavysleet" -> HEAVY_SLEET
    "heavysleetshowersandthunder_day" -> HEAVY_SLEET_SHOWERS_AND_THUNDER_DAY
    "heavysleetshowersandthunder_night" -> HEAVY_SLEET_SHOWERS_AND_THUNDER_NIGHT
    "heavysleetshowersandthunder_polartwilight" -> HEAVY_SLEET_SHOWERS_AND_THUNDER_POLAR_TWILIGHT
    "heavysleetshowers_day" -> HEAVY_SLEET_SHOWERS_DAY
    "heavysleetshowers_night" -> HEAVY_SLEET_SHOWERS_NIGHT
    "heavysleetshowers_polartwilight" -> HEAVY_SLEET_SHOWERS_POLAR_TWILIGHT
    "heavysnowandthunder" -> HEAVY_SNOW_AND_THUNDER
    "heavysnow" -> HEAVY_SNOW
    "heavysnowshowersandthunder_day" -> HEAVY_SNOW_SHOWERS_AND_THUNDER_DAY
    "heavysnowshowersandthunder_night" -> HEAVY_SNOW_SHOWERS_AND_THUNDER_NIGHT
    "heavysnowshowersandthunder_polartwilight" -> HEAVY_SNOW_SHOWERS_AND_THUNDER_POLAR_TWILIGHT
    "heavysnowshowers_day" -> HEAVY_SNOW_SHOWERS_DAY
    "heavysnowshowers_night" -> HEAVY_SNOW_SHOWERS_NIGHT
    "heavysnowshowers_polartwilight" -> HEAVY_SNOW_SHOWERS_POLAR_TWILIGHT
    "lightrainandthunder" -> LIGHT_RAIN_AND_THUNDER
    "lightrain" -> LIGHT_RAIN
    "lightrainshowersandthunder_day" -> LIGHT_RAIN_SHOWERS_AND_THUNDER_DAY
    "lightrainshowersandthunder_night" -> LIGHT_RAIN_SHOWERS_AND_THUNDER_NIGHT
    "lightrainshowersandthunder_polartwilight" -> LIGHT_RAIN_SHOWERS_AND_THUNDER_POLAR_TWILIGHT
    "lightrainshowers_day" -> LIGHT_RAIN_SHOWERS_DAY
    "lightrainshowers_night" -> LIGHT_RAIN_SHOWERS_NIGHT
    "lightrainshowers_polartwilight" -> LIGHT_RAIN_SHOWERS_POLAR_TWILIGHT
    "lightsleetandthunder" -> LIGHT_SLEET_AND_THUNDER
    "lightsleet" -> LIGHT_SLEET
    "lightsleetshowers_day" -> LIGHT_SLEET_SHOWERS_DAY
    "lightsleetshowers_night" -> LIGHT_SLEET_SHOWERS_NIGHT
    "lightsleetshowers_polartwilight" -> LIGHT_SLEET_SHOWERS_POLAR_TWILIGHT
    "lightsnowandthunder" -> LIGHT_SNOW_AND_THUNDER
    "lightsnow" -> LIGHT_SNOW
    "lightsnowshowers_day" -> LIGHT_SNOW_SHOWERS_DAY
    "lightsnowshowers_night" -> LIGHT_SNOW_SHOWERS_NIGHT
    "lightsnowshowers_polartwilight" -> LIGHT_SNOW_SHOWERS_POLAR_TWILIGHT
    "lightssleetshowersandthunder_day" -> LIGHT_SLEET_SHOWERS_AND_THUNDER_DAY
    "lightssleetshowersandthunder_night" -> LIGHT_SLEET_SHOWERS_AND_THUNDER_NIGHT
    "lightssleetshowersandthunder_polartwilight" -> LIGHT_SLEET_SHOWERS_AND_THUNDER_POLAR_TWILIGHT
    "lightssnowshowersandthunder_day" -> LIGHT_SNOW_SHOWERS_AND_THUNDER_DAY
    "lightssnowshowersandthunder_night" -> LIGHT_SNOW_SHOWERS_AND_THUNDER_NIGHT
    "lightssnowshowersandthunder_polartwilight" -> LIGHT_SNOW_SHOWERS_AND_THUNDER_POLAR_TWILIGHT
    "partlycloudy_day" -> PARTLY_CLOUDY_DAY
    "partlycloudy_night" -> PARTLY_CLOUDY_NIGHT
    "partlycloudy_polartwilight" -> PARTLY_CLOUDY_POLAR_TWILIGHT
    "rainandthunder" -> RAIN_AND_THUNDER
    "rain" -> RAIN
    "rainshowersandthunder_day" -> RAIN_SHOWERS_AND_THUNDER_DAY
    "rainshowersandthunder_night" -> RAIN_SHOWERS_AND_THUNDER_NIGHT
    "rainshowersandthunder_polartwilight" -> RAIN_SHOWERS_AND_THUNDER_POLAR_TWILIGHT
    "rainshowers_day" -> RAIN_SHOWERS_DAY
    "rainshowers_night" -> RAIN_SHOWERS_NIGHT
    "rainshowers_polartwilight" -> RAIN_SHOWERS_POLAR_TWILIGHT
    "sleetandthunder" -> SLEET_AND_THUNDER
    "sleet" -> SLEET
    "sleetshowersandthunder_day" -> SLEET_SHOWERS_AND_THUNDER_DAY
    "sleetshowersandthunder_night" -> SLEET_SHOWERS_AND_THUNDER_NIGHT
    "sleetshowersandthunder_polartwilight" -> SLEET_SHOWERS_AND_THUNDER_POLAR_TWILIGHT
    "sleetshowers_day" -> SLEET_SHOWERS_DAY
    "sleetshowers_night" -> SLEET_SHOWERS_NIGHT
    "sleetshowers_polartwilight" -> SLEET_SHOWERS_POLAR_TWILIGHT
    "snowandthunder" -> SNOW_AND_THUNDER
    "snow" -> SNOW
    "snowshowersandthunder_day" -> SNOW_SHOWERS_AND_THUNDER_DAY
    "snowshowersandthunder_night" -> SNOW_SHOWERS_AND_THUNDER_NIGHT
    "snowshowersandthunder_polartwilight" -> SNOW_SHOWERS_AND_THUNDER_POLAR_TWILIGHT
    "snowshowers_day" -> SNOW_SHOWERS_DAY
    "snowshowers_night" -> SNOW_SHOWERS_NIGHT
    "snowshowers_polartwilight" -> SNOW_SHOWERS_POLAR_TWILIGHT
    else -> UNKNOWN
}