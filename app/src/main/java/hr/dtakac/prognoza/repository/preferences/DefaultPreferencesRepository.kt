package hr.dtakac.prognoza.repository.preferences

import android.content.SharedPreferences
import hr.dtakac.prognoza.common.DEFAULT_PLACE_ID
import hr.dtakac.prognoza.coroutines.DispatcherProvider
import kotlinx.coroutines.withContext

private const val PLACE_ID_KEY = "place_id"

class DefaultPreferencesRepository(
    private val sharedPreferences: SharedPreferences,
    private val dispatcherProvider: DispatcherProvider
) : PreferencesRepository {
    override suspend fun getSelectedPlaceId(): String =
        withContext(dispatcherProvider.io) {
            val selected = sharedPreferences.getString(PLACE_ID_KEY, DEFAULT_PLACE_ID)
            if (selected == null) {
                setSelectedPlaceId(DEFAULT_PLACE_ID)
                DEFAULT_PLACE_ID
            } else {
                selected
            }
        }

    override suspend fun setSelectedPlaceId(placeId: String) {
        withContext(dispatcherProvider.io) {
            sharedPreferences.edit().putString(PLACE_ID_KEY, placeId).commit()
        }
    }
}