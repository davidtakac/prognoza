package hr.dtakac.prognoza.fakes

import hr.dtakac.prognoza.common.TEST_PLACE_ID
import hr.dtakac.prognoza.repository.preferences.PreferencesRepository

class FakePreferencesRepository : PreferencesRepository {
    override suspend fun getSelectedPlaceId(): String {
        return TEST_PLACE_ID
    }

    override suspend fun setSelectedPlaceId(placeId: String) {
        // do nothing
    }
}