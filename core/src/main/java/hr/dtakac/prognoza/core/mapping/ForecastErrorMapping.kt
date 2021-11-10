package hr.dtakac.prognoza.core.utils

import hr.dtakac.prognoza.core.R
import hr.dtakac.prognoza.core.model.repository.*

fun ForecastError.toErrorResourceId() =
    when (this) {
        is ThrottlingForecastError -> R.string.error_met_throttling
        is ClientForecastError -> R.string.error_met_client
        is ServerForecastError -> R.string.error_met_server
        is UnknownForecastError -> R.string.error_generic
        is DatabaseForecastError -> R.string.error_database
        is IoForecastError -> R.string.error_io
    }