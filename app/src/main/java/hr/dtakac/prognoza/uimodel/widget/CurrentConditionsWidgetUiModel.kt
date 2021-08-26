package hr.dtakac.prognoza.uimodel.widget

import hr.dtakac.prognoza.uimodel.MeasurementUnit

data class CurrentConditionsWidgetUiModel(
    val temperature: Float?,
    val feelsLike: Float?,
    val placeName: String?,
    val iconResourceId: Int?,
    val displayDataInUnit: MeasurementUnit
)