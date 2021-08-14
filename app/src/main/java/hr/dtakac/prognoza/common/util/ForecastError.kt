package hr.dtakac.prognoza.common.util

import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.repository.forecast.*

fun ForecastError.toErrorResourceId() =
    when (this) {
        is Throttling -> R.string.error_met_throttling
        is ClientSide -> R.string.error_met_client
        is ServerSide -> R.string.error_met_server
        is Unknown -> R.string.error_generic
        is DatabaseError -> R.string.error_database
    }