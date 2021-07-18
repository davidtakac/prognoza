package hr.dtakac.prognoza.repository.forecast

import hr.dtakac.prognoza.database.entity.ForecastDay
import hr.dtakac.prognoza.database.entity.ForecastHour

interface ForecastRepository {
    suspend fun getForecastToday(): List<ForecastHour>
    suspend fun getForecastTomorrow(): List<ForecastHour>
    suspend fun getForecastDays(): List<ForecastDay>
}