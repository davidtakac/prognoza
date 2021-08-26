package hr.dtakac.prognoza.uimodel.forecast

import hr.dtakac.prognoza.uimodel.MeasurementUnit
import hr.dtakac.prognoza.uimodel.cell.HourUiModel

data class CurrentConditionsUiModel(
    val currentHour: HourUiModel,
    val precipitationForecast: Float?,
    val displayDataInUnit: MeasurementUnit
)