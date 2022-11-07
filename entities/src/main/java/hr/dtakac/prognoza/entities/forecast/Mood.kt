package hr.dtakac.prognoza.entities.forecast

enum class Mood {
    DEFAULT, SUNNY, FOG, CLOUDY, RAIN, SNOW, SLEET;

    companion object {
        internal fun fromDescription(description: Description): Mood = when (description) {
            Description.UNKNOWN -> DEFAULT

            Description.FOG -> FOG

            Description.CLEAR_SKY_DAY,
            Description.CLEAR_SKY_NIGHT,
            Description.CLEAR_SKY_POLAR_TWILIGHT,
            Description.FAIR_DAY,
            Description.FAIR_NIGHT,
            Description.FAIR_POLAR_TWILIGHT -> SUNNY

            Description.CLOUDY,
            Description.PARTLY_CLOUDY_DAY,
            Description.PARTLY_CLOUDY_NIGHT,
            Description.PARTLY_CLOUDY_POLAR_TWILIGHT -> CLOUDY

            Description.HEAVY_RAIN_AND_THUNDER,
            Description.HEAVY_RAIN,
            Description.HEAVY_RAIN_SHOWERS_AND_THUNDER_DAY,
            Description.HEAVY_RAIN_SHOWERS_AND_THUNDER_NIGHT,
            Description.HEAVY_RAIN_SHOWERS_AND_THUNDER_POLAR_TWILIGHT,
            Description.HEAVY_RAIN_SHOWERS_DAY,
            Description.HEAVY_RAIN_SHOWERS_NIGHT,
            Description.HEAVY_RAIN_SHOWERS_POLAR_TWILIGHT,
            Description.LIGHT_RAIN_AND_THUNDER,
            Description.LIGHT_RAIN,
            Description.LIGHT_RAIN_SHOWERS_AND_THUNDER_DAY,
            Description.LIGHT_RAIN_SHOWERS_AND_THUNDER_NIGHT,
            Description.LIGHT_RAIN_SHOWERS_AND_THUNDER_POLAR_TWILIGHT,
            Description.LIGHT_RAIN_SHOWERS_DAY,
            Description.LIGHT_RAIN_SHOWERS_NIGHT,
            Description.LIGHT_RAIN_SHOWERS_POLAR_TWILIGHT,
            Description.RAIN_AND_THUNDER,
            Description.RAIN,
            Description.RAIN_SHOWERS_AND_THUNDER_DAY,
            Description.RAIN_SHOWERS_AND_THUNDER_NIGHT,
            Description.RAIN_SHOWERS_AND_THUNDER_POLAR_TWILIGHT,
            Description.RAIN_SHOWERS_DAY,
            Description.RAIN_SHOWERS_NIGHT,
            Description.RAIN_SHOWERS_POLAR_TWILIGHT -> RAIN

            Description.HEAVY_SLEET_AND_THUNDER,
            Description.HEAVY_SLEET,
            Description.HEAVY_SLEET_SHOWERS_AND_THUNDER_DAY,
            Description.HEAVY_SLEET_SHOWERS_AND_THUNDER_NIGHT,
            Description.HEAVY_SLEET_SHOWERS_AND_THUNDER_POLAR_TWILIGHT,
            Description.HEAVY_SLEET_SHOWERS_DAY,
            Description.HEAVY_SLEET_SHOWERS_NIGHT,
            Description.HEAVY_SLEET_SHOWERS_POLAR_TWILIGHT,
            Description.LIGHT_SLEET_AND_THUNDER,
            Description.LIGHT_SLEET,
            Description.LIGHT_SLEET_SHOWERS_DAY,
            Description.LIGHT_SLEET_SHOWERS_NIGHT,
            Description.LIGHT_SLEET_SHOWERS_AND_THUNDER_DAY,
            Description.LIGHT_SLEET_SHOWERS_AND_THUNDER_NIGHT,
            Description.LIGHT_SLEET_SHOWERS_AND_THUNDER_POLAR_TWILIGHT,
            Description.LIGHT_SLEET_SHOWERS_POLAR_TWILIGHT,
            Description.SLEET_AND_THUNDER,
            Description.SLEET,
            Description.SLEET_SHOWERS_AND_THUNDER_DAY,
            Description.SLEET_SHOWERS_AND_THUNDER_NIGHT,
            Description.SLEET_SHOWERS_AND_THUNDER_POLAR_TWILIGHT,
            Description.SLEET_SHOWERS_DAY,
            Description.SLEET_SHOWERS_NIGHT,
            Description.SLEET_SHOWERS_POLAR_TWILIGHT -> SLEET

            Description.HEAVY_SNOW_AND_THUNDER,
            Description.HEAVY_SNOW,
            Description.HEAVY_SNOW_SHOWERS_AND_THUNDER_DAY,
            Description.HEAVY_SNOW_SHOWERS_AND_THUNDER_NIGHT,
            Description.HEAVY_SNOW_SHOWERS_AND_THUNDER_POLAR_TWILIGHT,
            Description.HEAVY_SNOW_SHOWERS_DAY,
            Description.HEAVY_SNOW_SHOWERS_NIGHT,
            Description.HEAVY_SNOW_SHOWERS_POLAR_TWILIGHT,
            Description.LIGHT_SNOW_AND_THUNDER,
            Description.LIGHT_SNOW,
            Description.LIGHT_SNOW_SHOWERS_DAY,
            Description.LIGHT_SNOW_SHOWERS_NIGHT,
            Description.LIGHT_SNOW_SHOWERS_AND_THUNDER_DAY,
            Description.LIGHT_SNOW_SHOWERS_AND_THUNDER_NIGHT,
            Description.LIGHT_SNOW_SHOWERS_AND_THUNDER_POLAR_TWILIGHT,
            Description.LIGHT_SNOW_SHOWERS_POLAR_TWILIGHT,
            Description.SNOW_AND_THUNDER,
            Description.SNOW,
            Description.SNOW_SHOWERS_AND_THUNDER_DAY,
            Description.SNOW_SHOWERS_AND_THUNDER_NIGHT,
            Description.SNOW_SHOWERS_AND_THUNDER_POLAR_TWILIGHT,
            Description.SNOW_SHOWERS_DAY,
            Description.SNOW_SHOWERS_NIGHT,
            Description.SNOW_SHOWERS_POLAR_TWILIGHT -> SNOW
        }
    }
}