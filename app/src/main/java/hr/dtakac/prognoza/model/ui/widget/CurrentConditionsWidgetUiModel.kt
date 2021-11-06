package hr.dtakac.prognoza.model.ui.widget

import androidx.compose.runtime.Immutable
import hr.dtakac.prognoza.model.ui.MeasurementUnit

@Immutable
data class CurrentConditionsWidgetUiModel(
    val temperature: Double?,
    val feelsLike: Double?,
    val placeName: String?,
    val iconResourceId: Int?,
    val precipitationTwoHours: Double?,
    val displayDataInUnit: MeasurementUnit,
)