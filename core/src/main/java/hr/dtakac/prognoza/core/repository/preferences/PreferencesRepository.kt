package hr.dtakac.prognoza.core.repository.preferences

import hr.dtakac.prognoza.core.model.ui.MeasurementUnit

interface PreferencesRepository {
    suspend fun setSelectedPlaceId(placeId: String)
    suspend fun getSelectedPlaceId(): String?
    suspend fun setSelectedUnit(unit: MeasurementUnit)
    suspend fun getSelectedUnit(): MeasurementUnit
}