package hr.dtakac.prognoza.uimodel

import hr.dtakac.prognoza.R

data class WeatherDescription(
    val iconResourceId: Int,
    val descriptionResourceId: Int
)

data class RepresentativeWeatherDescription(
    val weatherDescription: WeatherDescription,
    val isMostly: Boolean
)

val WEATHER_ICONS = mapOf(
    "clearsky_day" to WeatherDescription(R.drawable.clearsky_day, R.string.clearsky),
    "clearsky_night" to WeatherDescription(R.drawable.clearsky_night, R.string.clearsky),
    "clearsky_polartwilight" to WeatherDescription(R.drawable.clearsky_polartwilight, R.string.clearsky),
    "cloudy" to WeatherDescription(R.drawable.cloudy, R.string.cloudy),
    "fair_day" to WeatherDescription(R.drawable.fair_day, R.string.fair),
    "fair_night" to WeatherDescription(R.drawable.fair_night, R.string.fair),
    "fair_polartwilight" to WeatherDescription(R.drawable.fair_polartwilight, R.string.fair),
    "fog" to WeatherDescription(R.drawable.fog, R.string.fog),
    "heavyrainandthunder" to WeatherDescription(
        R.drawable.heavyrainandthunder,
        R.string.heavyrainandthunder
    ),
    "heavyrain" to WeatherDescription(R.drawable.heavyrain, R.string.heavyrain),
    "heavyrainshowersandthunder_day" to WeatherDescription(
        R.drawable.heavyrainshowersandthunder_day,
        R.string.heavyrainshowersandthunder
    ),
    "heavyrainshowersandthunder_night" to WeatherDescription(
        R.drawable.heavyrainshowersandthunder_night,
        R.string.heavyrainshowersandthunder
    ),
    "heavyrainshowersandthunder_polartwilight" to WeatherDescription(
        R.drawable.heavyrainshowersandthunder_polartwilight,
        R.string.heavyrainshowersandthunder
    ),
    "heavyrainshowers_day" to WeatherDescription(
        R.drawable.heavyrainshowers_day,
        R.string.heavyrainshowers
    ),
    "heavyrainshowers_night" to WeatherDescription(
        R.drawable.heavyrainshowers_night,
        R.string.heavyrainshowers
    ),
    "heavyrainshowers_polartwilight" to WeatherDescription(
        R.drawable.heavyrainshowers_polartwilight,
        R.string.heavyrainshowers
    ),
    "heavysleetandthunder" to WeatherDescription(
        R.drawable.heavysleetandthunder,
        R.string.heavysleetandthunder
    ),
    "heavysleet" to WeatherDescription(R.drawable.heavysleet, R.string.heavysleet),
    "heavysleetshowersandthunder_day" to WeatherDescription(
        R.drawable.heavysleetshowersandthunder_day,
        R.string.heavysleetshowersandthunder
    ),
    "heavysleetshowersandthunder_night" to WeatherDescription(
        R.drawable.heavysleetshowersandthunder_night,
        R.string.heavysleetshowersandthunder
    ),
    "heavysleetshowersandthunder_polartwilight" to WeatherDescription(
        R.drawable.heavysleetshowersandthunder_polartwilight,
        R.string.heavysleetshowersandthunder
    ),
    "heavysleetshowers_day" to WeatherDescription(
        R.drawable.heavysleetshowers_day,
        R.string.heavysleetshowers
    ),
    "heavysleetshowers_night" to WeatherDescription(
        R.drawable.heavysleetshowers_night,
        R.string.heavysleetshowers
    ),
    "heavysleetshowers_polartwilight" to WeatherDescription(
        R.drawable.heavysleetshowers_polartwilight,
        R.string.heavysleetshowers
    ),
    "heavysnowandthunder" to WeatherDescription(
        R.drawable.heavysnowandthunder,
        R.string.heavysnowandthunder
    ),
    "heavysnow" to WeatherDescription(R.drawable.heavysnow, R.string.heavysnow),
    "heavysnowshowersandthunder_day" to WeatherDescription(
        R.drawable.heavysnowshowersandthunder_day,
        R.string.heavysnowshowersandthunder
    ),
    "heavysnowshowersandthunder_night" to WeatherDescription(
        R.drawable.heavysnowshowersandthunder_night,
        R.string.heavysnowshowersandthunder
    ),
    "heavysnowshowersandthunder_polartwilight" to WeatherDescription(
        R.drawable.heavysnowshowersandthunder_polartwilight,
        R.string.heavysnowshowersandthunder
    ),
    "heavysnowshowers_day" to WeatherDescription(
        R.drawable.heavysnowshowers_day,
        R.string.heavysnowshowers
    ),
    "heavysnowshowers_night" to WeatherDescription(
        R.drawable.heavysnowshowers_night,
        R.string.heavysnowshowers
    ),
    "heavysnowshowers_polartwilight" to WeatherDescription(
        R.drawable.heavysnowshowers_polartwilight,
        R.string.heavysnowshowers
    ),
    "lightrainandthunder" to WeatherDescription(
        R.drawable.lightrainandthunder,
        R.string.lightrainandthunder
    ),
    "lightrain" to WeatherDescription(R.drawable.lightrain, R.string.lightrain),
    "lightrainshowersandthunder_day" to WeatherDescription(
        R.drawable.lightrainshowersandthunder_day,
        R.string.lightrainshowersandthunder
    ),
    "lightrainshowersandthunder_night" to WeatherDescription(
        R.drawable.lightrainshowersandthunder_night,
        R.string.lightrainshowersandthunder
    ),
    "lightrainshowersandthunder_polartwilight" to WeatherDescription(
        R.drawable.lightrainshowersandthunder_polartwilight,
        R.string.lightrainshowersandthunder
    ),
    "lightrainshowers_day" to WeatherDescription(
        R.drawable.lightrainshowers_day,
        R.string.lightrainshowers
    ),
    "lightrainshowers_night" to WeatherDescription(
        R.drawable.lightrainshowers_night,
        R.string.lightrainshowers
    ),
    "lightrainshowers_polartwilight" to WeatherDescription(
        R.drawable.lightrainshowers_polartwilight,
        R.string.lightrainshowers
    ),
    "lightsleetandthunder" to WeatherDescription(
        R.drawable.lightsleetandthunder,
        R.string.lightsleetandthunder
    ),
    "lightsleet" to WeatherDescription(R.drawable.lightsleet, R.string.lightsleet),
    "lightsleetshowers_day" to WeatherDescription(
        R.drawable.lightsleetshowers_day,
        R.string.lightsleetshowers
    ),
    "lightsleetshowers_night" to WeatherDescription(
        R.drawable.lightsleetshowers_night,
        R.string.lightsleetshowers
    ),
    "lightsleetshowers_polartwilight" to WeatherDescription(
        R.drawable.lightsleetshowers_polartwilight,
        R.string.lightsleetshowers
    ),
    "lightsnowandthunder" to WeatherDescription(
        R.drawable.lightsnowandthunder,
        R.string.lightsnowandthunder
    ),
    "lightsnow" to WeatherDescription(R.drawable.lightsnow, R.string.lightsnow),
    "lightsnowshowers_day" to WeatherDescription(
        R.drawable.lightsnowshowers_day,
        R.string.lightsnowshowers
    ),
    "lightsnowshowers_night" to WeatherDescription(
        R.drawable.lightsnowshowers_night,
        R.string.lightsnowshowers
    ),
    "lightsnowshowers_polartwilight" to WeatherDescription(
        R.drawable.lightsnowshowers_polartwilight,
        R.string.lightsnowshowers
    ),
    "lightssleetshowersandthunder_day" to WeatherDescription(
        R.drawable.lightssleetshowersandthunder_day,
        R.string.lightssleetshowersandthunder
    ),
    "lightssleetshowersandthunder_night" to WeatherDescription(
        R.drawable.lightssleetshowersandthunder_night,
        R.string.lightssleetshowersandthunder
    ),
    "lightssleetshowersandthunder_polartwilight" to WeatherDescription(
        R.drawable.lightssleetshowersandthunder_polartwilight,
        R.string.lightssleetshowersandthunder
    ),
    "lightssnowshowersandthunder_day" to WeatherDescription(
        R.drawable.lightssnowshowersandthunder_day,
        R.string.lightssnowshowersandthunder
    ),
    "lightssnowshowersandthunder_night" to WeatherDescription(
        R.drawable.lightssnowshowersandthunder_night,
        R.string.lightssnowshowersandthunder
    ),
    "lightssnowshowersandthunder_polartwilight" to WeatherDescription(
        R.drawable.lightssnowshowersandthunder_polartwilight,
        R.string.lightssnowshowersandthunder
    ),
    "partlycloudy_day" to WeatherDescription(R.drawable.partlycloudy_day, R.string.partlycloudy),
    "partlycloudy_night" to WeatherDescription(R.drawable.partlycloudy_night, R.string.partlycloudy),
    "partlycloudy_polartwilight" to WeatherDescription(
        R.drawable.partlycloudy_polartwilight,
        R.string.partlycloudy
    ),
    "rainandthunder" to WeatherDescription(R.drawable.rainandthunder, R.string.rainandthunder),
    "rain" to WeatherDescription(R.drawable.rain, R.string.rain),
    "rainshowersandthunder_day" to WeatherDescription(
        R.drawable.rainshowersandthunder_day,
        R.string.rainshowersandthunder
    ),
    "rainshowersandthunder_night" to WeatherDescription(
        R.drawable.rainshowersandthunder_night,
        R.string.rainshowersandthunder
    ),
    "rainshowersandthunder_polartwilight" to WeatherDescription(
        R.drawable.rainshowersandthunder_polartwilight,
        R.string.rainshowersandthunder
    ),
    "rainshowers_day" to WeatherDescription(R.drawable.rainshowers_day, R.string.rainshowers),
    "rainshowers_night" to WeatherDescription(R.drawable.rainshowers_night, R.string.rainshowers),
    "rainshowers_polartwilight" to WeatherDescription(
        R.drawable.rainshowers_polartwilight,
        R.string.rainshowers
    ),
    "sleetandthunder" to WeatherDescription(R.drawable.sleetandthunder, R.string.sleetandthunder),
    "sleet" to WeatherDescription(R.drawable.sleet, R.string.sleet),
    "sleetshowersandthunder_day" to WeatherDescription(
        R.drawable.sleetshowersandthunder_day,
        R.string.sleetshowersandthunder
    ),
    "sleetshowersandthunder_night" to WeatherDescription(
        R.drawable.sleetshowersandthunder_night,
        R.string.sleetshowersandthunder
    ),
    "sleetshowersandthunder_polartwilight" to WeatherDescription(
        R.drawable.sleetshowersandthunder_polartwilight,
        R.string.sleetshowersandthunder
    ),
    "sleetshowers_day" to WeatherDescription(R.drawable.sleetshowers_day, R.string.sleetshowers),
    "sleetshowers_night" to WeatherDescription(R.drawable.sleetshowers_night, R.string.sleetshowers),
    "sleetshowers_polartwilight" to WeatherDescription(
        R.drawable.sleetshowers_polartwilight,
        R.string.sleetshowers
    ),
    "snowandthunder" to WeatherDescription(R.drawable.snowandthunder, R.string.snowandthunder),
    "snow" to WeatherDescription(R.drawable.snow, R.string.snow),
    "snowshowersandthunder_day" to WeatherDescription(
        R.drawable.snowshowersandthunder_day,
        R.string.snowshowersandthunder
    ),
    "snowshowersandthunder_night" to WeatherDescription(
        R.drawable.snowshowersandthunder_night,
        R.string.snowshowersandthunder
    ),
    "snowshowersandthunder_polartwilight" to WeatherDescription(
        R.drawable.snowshowersandthunder_polartwilight,
        R.string.snowshowersandthunder
    ),
    "snowshowers_day" to WeatherDescription(R.drawable.snowshowers_day, R.string.snowshowers),
    "snowshowers_night" to WeatherDescription(R.drawable.snowshowers_night, R.string.snowshowers),
    "snowshowers_polartwilight" to WeatherDescription(
        R.drawable.snowshowers_polartwilight,
        R.string.snowshowers
    )
)

val NIGHT_SYMBOL_CODES = listOf(
    "clearsky_night",
    "fair_night",
    "heavyrainshowersandthunder_night",
    "heavyrainshowers_night",
    "heavysleetshowersandthunder_night",
    "heavysleetshowers_night",
    "heavysnowshowersandthunder_night",
    "heavysnowshowers_night",
    "lightrainshowersandthunder_night",
    "lightrainshowers_night",
    "lightsleetshowers_night",
    "lightsnowshowers_night",
    "lightssleetshowersandthunder_night",
    "lightssnowshowersandthunder_night",
    "partlycloudy_night",
    "rainshowersandthunder_night",
    "rainshowers_night",
    "sleetshowersandthunder_night",
    "sleetshowers_night",
    "snowshowersandthunder_night",
    "snowshowers_night",
)