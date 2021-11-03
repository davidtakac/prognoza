package hr.dtakac.prognoza.repository.preferences

import hr.dtakac.prognoza.model.ui.MeasurementUnit

interface PreferencesRepository {
    suspend fun setSelectedPlaceId(placeId: String)
    suspend fun getSelectedPlaceId(): String
    suspend fun setSelectedUnit(unit: MeasurementUnit)
    suspend fun getSelectedUnit(): MeasurementUnit
}