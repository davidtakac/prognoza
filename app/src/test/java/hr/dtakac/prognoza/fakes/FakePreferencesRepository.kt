package hr.dtakac.prognoza.fakes

import hr.dtakac.prognoza.common.TEST_PLACE
import hr.dtakac.prognoza.repository.preferences.PreferencesRepository
import hr.dtakac.prognoza.uimodel.MeasurementUnit

class FakePreferencesRepository : PreferencesRepository {
    override suspend fun getSelectedPlaceId(): String {
        return TEST_PLACE.id
    }

    override suspend fun setSelectedPlaceId(placeId: String) {
        // do nothing
    }

    override suspend fun getSelectedUnit(): MeasurementUnit {
        return MeasurementUnit.METRIC
    }

    override suspend fun setSelectedUnit(unit: MeasurementUnit) {
        // do nothing
    }
}