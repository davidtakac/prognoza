package hr.dtakac.prognoza.common.utils

import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.model.repository.*

fun ForecastError.toErrorResourceId() =
    when (this) {
        is ThrottlingError -> R.string.error_met_throttling
        is ClientError -> R.string.error_met_client
        is ServerError -> R.string.error_met_server
        is UnknownError -> R.string.error_generic
        is DatabaseError -> R.string.error_database
        is IOError -> R.string.error_io
    }