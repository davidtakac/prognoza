package hr.dtakac.prognoza.uimodel.forecast

import hr.dtakac.prognoza.uimodel.MeasurementUnit
import hr.dtakac.prognoza.uimodel.cell.HourCellModel

data class TodayForecastCurrentConditionsModel(
    val currentHour: HourCellModel,
    val precipitationForecast: Float?,
    val displayDataInUnit: MeasurementUnit
)