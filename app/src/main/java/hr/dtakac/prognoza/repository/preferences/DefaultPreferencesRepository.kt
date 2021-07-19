package hr.dtakac.prognoza.repository.preferences

import android.content.SharedPreferences

private const val LOCATION_ID_KEY = "location_id"

class DefaultPreferencesRepository(
    private val sharedPreferences: SharedPreferences
) : PreferencesRepository {
    override var locationId: Long
        get() = sharedPreferences.getLong(LOCATION_ID_KEY, 1)
        set(value) {
            sharedPreferences.edit().putLong(LOCATION_ID_KEY, value).apply()
        }
}