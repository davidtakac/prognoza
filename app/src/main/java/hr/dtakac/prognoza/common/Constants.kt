package hr.dtakac.prognoza

const val USER_AGENT = "Prognoza/${BuildConfig.VERSION_NAME}, " +
        "github.com/davidtakac/Prognoza, " +
        "developer.takac@gmail.com"

const val MET_NORWAY_BASE_URL = "https://api.met.no/weatherapi/"
const val OSM_NOMINATIM_BASE_URL = "https://nominatim.openstreetmap.org/"
const val IMAGE_PLACEHOLDER = R.drawable.ic_cloud
const val MIN_DATE_TIME_RFC_1123 = "Thu, 1 January 1970 00:00:00 GMT"
const val DEFAULT_PLACE_ID = "259515203" // corresponds to Osijek, Croatia