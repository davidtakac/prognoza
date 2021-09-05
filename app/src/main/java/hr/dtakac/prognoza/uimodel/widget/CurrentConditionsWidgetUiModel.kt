package hr.dtakac.prognoza.uimodel.widget

import hr.dtakac.prognoza.uimodel.MeasurementUnit

data class CurrentConditionsWidgetUiModel(
    val temperature: Double?,
    val feelsLike: Double?,
    val placeName: String?,
    val iconResourceId: Int?,
    val precipitationTwoHours: Double?,
    val displayDataInUnit: MeasurementUnit,
)