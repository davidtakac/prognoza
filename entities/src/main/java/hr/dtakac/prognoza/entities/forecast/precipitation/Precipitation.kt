package hr.dtakac.prognoza.entities.forecast.precipitation

import hr.dtakac.prognoza.entities.forecast.ForecastDescription
import hr.dtakac.prognoza.entities.forecast.units.Length
import hr.dtakac.prognoza.entities.forecast.precipitation.PrecipitationDescription.*
import hr.dtakac.prognoza.entities.forecast.ForecastDescription.*

class Precipitation(
    val amount: Length,
    forecastDescription: ForecastDescription
) {
    val description: PrecipitationDescription = if (amount.millimeters == 0.0) NONE else when (forecastDescription) {
        CLEAR_SKY_DAY,
        CLEAR_SKY_NIGHT,
        CLEAR_SKY_POLAR_TWILIGHT,
        CLOUDY,
        FAIR_DAY,
        FAIR_NIGHT,
        FAIR_POLAR_TWILIGHT,
        PARTLY_CLOUDY_DAY,
        PARTLY_CLOUDY_NIGHT,
        PARTLY_CLOUDY_POLAR_TWILIGHT,
        FOG -> NONE

        HEAVY_RAIN_AND_THUNDER,
        ForecastDescription.HEAVY_RAIN,
        HEAVY_RAIN_SHOWERS_AND_THUNDER_DAY,
        HEAVY_RAIN_SHOWERS_AND_THUNDER_NIGHT,
        HEAVY_RAIN_SHOWERS_AND_THUNDER_POLAR_TWILIGHT,
        HEAVY_RAIN_SHOWERS_DAY,
        HEAVY_RAIN_SHOWERS_NIGHT,
        HEAVY_RAIN_SHOWERS_POLAR_TWILIGHT -> PrecipitationDescription.HEAVY_RAIN

        HEAVY_SLEET_AND_THUNDER,
        ForecastDescription.HEAVY_SLEET,
        HEAVY_SLEET_SHOWERS_AND_THUNDER_DAY,
        HEAVY_SLEET_SHOWERS_AND_THUNDER_NIGHT,
        HEAVY_SLEET_SHOWERS_AND_THUNDER_POLAR_TWILIGHT,
        HEAVY_SLEET_SHOWERS_DAY,
        HEAVY_SLEET_SHOWERS_NIGHT,
        HEAVY_SLEET_SHOWERS_POLAR_TWILIGHT -> PrecipitationDescription.HEAVY_SLEET

        HEAVY_SNOW_AND_THUNDER,
        ForecastDescription.HEAVY_SNOW,
        HEAVY_SNOW_SHOWERS_AND_THUNDER_DAY,
        HEAVY_SNOW_SHOWERS_AND_THUNDER_NIGHT,
        HEAVY_SNOW_SHOWERS_AND_THUNDER_POLAR_TWILIGHT,
        HEAVY_SNOW_SHOWERS_DAY,
        HEAVY_SNOW_SHOWERS_NIGHT,
        HEAVY_SNOW_SHOWERS_POLAR_TWILIGHT -> PrecipitationDescription.HEAVY_SNOW

        LIGHT_RAIN_AND_THUNDER,
        ForecastDescription.LIGHT_RAIN,
        LIGHT_RAIN_SHOWERS_AND_THUNDER_DAY,
        LIGHT_RAIN_SHOWERS_AND_THUNDER_NIGHT,
        LIGHT_RAIN_SHOWERS_AND_THUNDER_POLAR_TWILIGHT,
        LIGHT_RAIN_SHOWERS_DAY,
        LIGHT_RAIN_SHOWERS_NIGHT,
        LIGHT_RAIN_SHOWERS_POLAR_TWILIGHT -> PrecipitationDescription.LIGHT_RAIN

        LIGHT_SLEET_AND_THUNDER,
        ForecastDescription.LIGHT_SLEET,
        LIGHT_SLEET_SHOWERS_DAY,
        LIGHT_SLEET_SHOWERS_NIGHT,
        LIGHT_SLEET_SHOWERS_AND_THUNDER_DAY,
        LIGHT_SLEET_SHOWERS_AND_THUNDER_NIGHT,
        LIGHT_SLEET_SHOWERS_AND_THUNDER_POLAR_TWILIGHT,
        LIGHT_SLEET_SHOWERS_POLAR_TWILIGHT -> PrecipitationDescription.LIGHT_SLEET

        LIGHT_SNOW_AND_THUNDER,
        ForecastDescription.LIGHT_SNOW,
        LIGHT_SNOW_SHOWERS_DAY,
        LIGHT_SNOW_SHOWERS_NIGHT,
        LIGHT_SNOW_SHOWERS_AND_THUNDER_DAY,
        LIGHT_SNOW_SHOWERS_AND_THUNDER_NIGHT,
        LIGHT_SNOW_SHOWERS_AND_THUNDER_POLAR_TWILIGHT,
        LIGHT_SNOW_SHOWERS_POLAR_TWILIGHT -> PrecipitationDescription.LIGHT_SNOW


        RAIN_AND_THUNDER,
        ForecastDescription.RAIN,
        RAIN_SHOWERS_AND_THUNDER_DAY,
        RAIN_SHOWERS_AND_THUNDER_NIGHT,
        RAIN_SHOWERS_AND_THUNDER_POLAR_TWILIGHT,
        RAIN_SHOWERS_DAY,
        RAIN_SHOWERS_NIGHT,
        RAIN_SHOWERS_POLAR_TWILIGHT -> PrecipitationDescription.RAIN

        SLEET_AND_THUNDER,
        ForecastDescription.SLEET,
        SLEET_SHOWERS_AND_THUNDER_DAY,
        SLEET_SHOWERS_AND_THUNDER_NIGHT,
        SLEET_SHOWERS_AND_THUNDER_POLAR_TWILIGHT,
        SLEET_SHOWERS_DAY,
        SLEET_SHOWERS_NIGHT,
        SLEET_SHOWERS_POLAR_TWILIGHT -> PrecipitationDescription.SLEET

        SNOW_AND_THUNDER,
        ForecastDescription.SNOW,
        SNOW_SHOWERS_AND_THUNDER_DAY,
        SNOW_SHOWERS_AND_THUNDER_NIGHT,
        SNOW_SHOWERS_AND_THUNDER_POLAR_TWILIGHT,
        SNOW_SHOWERS_DAY,
        SNOW_SHOWERS_NIGHT,
        SNOW_SHOWERS_POLAR_TWILIGHT -> PrecipitationDescription.SNOW

        else -> NONE
    }
}

