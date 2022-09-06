package hr.dtakac.prognoza.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import hr.dtakac.prognoza.data.database.place.PlaceDao
import hr.dtakac.prognoza.data.mapping.mapDbModelToEntity
import hr.dtakac.prognoza.domain.repository.SettingsRepository
import hr.dtakac.prognoza.entities.Place
import hr.dtakac.prognoza.entities.forecast.units.PressureUnit
import kotlin.coroutines.suspendCoroutine

class DefaultSettingsRepository(
    private val sharedPreferences: SharedPreferences,
    private val placeDao: PlaceDao
) : SettingsRepository {
    override suspend fun getPlace(): Place? {
        val latitude = sharedPreferences.getFloat(PLACE_LAT, 91f).toDouble()
        val longitude = sharedPreferences.getFloat(PLACE_LAT, 181f).toDouble()
        return placeDao.get(latitude, longitude)?.let(::mapDbModelToEntity)
    }

    override suspend fun setPlace(place: Place) {
        commit {
            putFloat(PLACE_LAT, place.latitude.toFloat())
            putFloat(PLACE_LON, place.longitude.toFloat())
        }
    }

    override suspend fun getAirPressureUnit(): PressureUnit {

    }

    override suspend fun setAirPressureUnit(unit: PressureUnit) {
        TODO("Not yet implemented")
    }

    private suspend fun commit(action: SharedPreferences.Editor.() -> Unit) = suspendCoroutine<Unit> {
        sharedPreferences.edit(commit = true, action = action)
        it.resumeWith(Result.success(Unit))
    }
}

private const val PLACE_LAT = "place_lat"
private const val PLACE_LON = "place_lon"