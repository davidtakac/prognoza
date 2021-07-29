package hr.dtakac.prognoza.repository.preferences

import android.content.SharedPreferences
import hr.dtakac.prognoza.DEFAULT_PLACE_ID

private const val PLACE_ID_KEY = "place_id"

class DefaultPreferencesRepository(
    private val sharedPreferences: SharedPreferences
) : PreferencesRepository {
    override var placeId: String
        get() = sharedPreferences.getString(PLACE_ID_KEY, DEFAULT_PLACE_ID) ?: DEFAULT_PLACE_ID
        set(value) {
            sharedPreferences.edit().putString(PLACE_ID_KEY, value).apply()
        }
}