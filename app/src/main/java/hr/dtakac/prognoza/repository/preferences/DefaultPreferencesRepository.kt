package hr.dtakac.prognoza.repository.preferences

import android.content.SharedPreferences
import hr.dtakac.prognoza.DEFAULT_PLACE_ID
import hr.dtakac.prognoza.common.coroutines.DispatcherProvider
import hr.dtakac.prognoza.model.ui.MeasurementUnit
import kotlinx.coroutines.withContext

private const val PLACE_ID_KEY = "place_id"
private const val UNITS_KEY = "units"

class DefaultPreferencesRepository(
    private val sharedPreferences: SharedPreferences,
    private val dispatcherProvider: DispatcherProvider
) : PreferencesRepository {
    override suspend fun getSelectedPlaceId(): String =
        withContext(dispatcherProvider.io) {
            val selected = sharedPreferences.getString(PLACE_ID_KEY, null)
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

    override suspend fun setSelectedUnit(unit: MeasurementUnit) {
        withContext(dispatcherProvider.io) {
            sharedPreferences.edit().putString(UNITS_KEY, unit.name).commit()
        }
    }

    override suspend fun getSelectedUnit(): MeasurementUnit {
        val selected = sharedPreferences.getString(UNITS_KEY, null)
        return if (selected == null) {
            setSelectedUnit(MeasurementUnit.METRIC)
            MeasurementUnit.METRIC
        } else {
            MeasurementUnit.valueOf(selected)
        }
    }
}