package hr.dtakac.prognoza.model.ui.widget

import hr.dtakac.prognoza.model.ui.MeasurementUnit

data class CurrentConditionsWidgetUiModel(
    val temperature: Double?,
    val feelsLike: Double?,
    val placeName: String?,
    val iconResourceId: Int?,
    val precipitationTwoHours: Double?,
    val displayDataInUnit: MeasurementUnit,
)