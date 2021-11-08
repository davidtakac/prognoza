package hr.dtakac.prognoza.fakes

import hr.dtakac.prognoza.common.TEST_PLACE
import hr.dtakac.prognoza.core.repository.preferences.PreferencesRepository

class FakePreferencesRepository : PreferencesRepository {
    override suspend fun getSelectedPlaceId(): String {
        return TEST_PLACE.id
    }

    override suspend fun setSelectedPlaceId(placeId: String) {
        // do nothing
    }

    override suspend fun getSelectedUnit(): hr.dtakac.prognoza.core.model.ui.MeasurementUnit {
        return hr.dtakac.prognoza.core.model.ui.MeasurementUnit.METRIC
    }

    override suspend fun setSelectedUnit(unit: hr.dtakac.prognoza.core.model.ui.MeasurementUnit) {
        // do nothing
    }
}