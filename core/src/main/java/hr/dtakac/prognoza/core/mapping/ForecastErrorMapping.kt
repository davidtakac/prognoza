package hr.dtakac.prognoza.core.utils

import hr.dtakac.prognoza.core.R
import hr.dtakac.prognoza.core.model.repository.*

fun ForecastError.toErrorResourceId() =
    when (this) {
        is ForecastError.Throttling -> R.string.error_met_throttling
        is ForecastError.Client -> R.string.error_met_client
        is ForecastError.Server -> R.string.error_met_server
        is ForecastError.Unknown -> R.string.error_generic
        is ForecastError.Database -> R.string.error_database
        is ForecastError.Io -> R.string.error_io
        is ForecastError.NoSelectedPlace -> R.string.error_forecast_empty_no_selected_place
    }