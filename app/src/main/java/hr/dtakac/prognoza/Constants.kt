package hr.dtakac.prognoza

const val USER_AGENT = "Prognoza/${BuildConfig.VERSION_NAME}, " +
        "github.com/davidtakac/Prognoza, " +
        "developer.takac@gmail.com"

const val MET_NORWAY_BASE_URL = "https://api.met.no/weatherapi/"
const val OSM_NOMINATIM_BASE_URL = "https://nominatim.openstreetmap.org/"
const val DEFAULT_PLACE_ID = "259515203" // corresponds to Osijek, Croatia

const val PLACE_SEARCH_REQUEST_KEY = "place_search_request_key"
const val TODAY_REQUEST_KEY = "today_request_key"
const val TOMORROW_REQUEST_KEY = "tomorrow_request_key"
const val DAYS_REQUEST_KEY = "days_request_key"
const val BUNDLE_KEY_PLACE_PICKED = "place_picked_key"
const val BUNDLE_KEY_UNITS_CHANGED = "units_changed_key"

const val SIGNIFICANT_PRECIPITATION_METRIC = 0.1f
const val SIGNIFICANT_PRECIPITATION_IMPERIAL = 0.01f