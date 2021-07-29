package hr.dtakac.prognoza.repository.preferences

import android.content.SharedPreferences

private const val PLACE_ID_KEY = "place_id"

class DefaultPreferencesRepository(
    private val sharedPreferences: SharedPreferences
) : PreferencesRepository {
    override var placeId: Long
        get() = sharedPreferences.getLong(PLACE_ID_KEY, 1)
        set(value) {
            sharedPreferences.edit().putLong(PLACE_ID_KEY, value).apply()
        }
}