package hr.dtakac.prognoza.widget.model

data class CurrentConditionsWidgetUiModel(
    val temperature: Double?,
    val feelsLike: Double?,
    val placeName: String?,
    val iconResourceId: Int?,
    val precipitationTwoHours: Double?,
    val displayDataInUnit: hr.dtakac.prognoza.core.model.ui.MeasurementUnit,
)