package hr.dtakac.prognoza.database.databasemodel

import hr.dtakac.prognoza.database.entity.ForecastHour
import hr.dtakac.prognoza.database.entity.ForecastMeta

data class ForecastHours(
    val meta: ForecastMeta?,
    val hours: List<ForecastHour>
)