package hr.dtakac.prognoza.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import hr.dtakac.prognoza.domain.place.SavedPlaceGetter
import hr.dtakac.prognoza.domain.settings.SettingsRepository
import hr.dtakac.prognoza.entities.Place
import hr.dtakac.prognoza.entities.forecast.units.LengthUnit
import hr.dtakac.prognoza.entities.forecast.units.PressureUnit
import hr.dtakac.prognoza.entities.forecast.units.SpeedUnit
import hr.dtakac.prognoza.entities.forecast.units.TemperatureUnit
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlin.coroutines.suspendCoroutine

class DefaultSettingsRepository(
    private val sharedPreferences: SharedPreferences,
    private val savedPlaceGetter: SavedPlaceGetter,
    private val ioDispatcher: CoroutineDispatcher
) : SettingsRepository {
    override suspend fun getPlace(): Place? {
        // Lat possible values are -90 to 90
        val lat = getFloat(PLACE_LAT, 91f).toDouble()
        // Lon possible values are -180 to 180
        val lon = getFloat(PLACE_LON, 181f).toDouble()
        return savedPlaceGetter.get(lat, lon)
    }

    override suspend fun setPlace(place: Place) {
        commit {
            putFloat(PLACE_LAT, place.latitude.toFloat())
            putFloat(PLACE_LON, place.longitude.toFloat())
        }
    }

    override suspend fun getPressureUnit(): PressureUnit {
        val ordinal = getInt(AIR_PRESSURE_UNIT)
        return if (ordinal == -1) {
            val default = PressureUnit.HPA
            setPressureUnit(default)
            default
        } else {
            PressureUnit.values()[ordinal]
        }
    }

    override suspend fun setPressureUnit(unit: PressureUnit) {
        commit {
            putInt(AIR_PRESSURE_UNIT, unit.ordinal)
        }
    }

    override suspend fun getPrecipitationUnit(): LengthUnit {
        val ordinal = getInt(PRECIPITATION_UNIT)
        return if (ordinal == -1) {
            val default = LengthUnit.MM
            setPrecipitationUnit(default)
            default
        } else {
            LengthUnit.values()[ordinal]
        }
    }

    override suspend fun setPrecipitationUnit(unit: LengthUnit) {
        commit {
            putInt(PRECIPITATION_UNIT, unit.ordinal)
        }
    }

    override suspend fun getTemperatureUnit(): TemperatureUnit {
        val ordinal = getInt(TEMPERATURE_UNIT)
        return if (ordinal == -1) {
            val default = TemperatureUnit.C
            setTemperatureUnit(default)
            default
        } else {
            TemperatureUnit.values()[ordinal]
        }
    }

    override suspend fun setTemperatureUnit(unit: TemperatureUnit) {
        commit {
            putInt(TEMPERATURE_UNIT, unit.ordinal)
        }
    }

    override suspend fun getWindUnit(): SpeedUnit {
        val ordinal = getInt(WIND_UNIT)
        return if (ordinal == -1) {
            val default = SpeedUnit.MPS
            setWindUnit(default)
            default
        } else {
            SpeedUnit.values()[ordinal]
        }
    }

    override suspend fun setWindUnit(unit: SpeedUnit) {
        commit {
            putInt(WIND_UNIT, unit.ordinal)
        }
    }

    private suspend fun commit(
        action: SharedPreferences.Editor.() -> Unit
    ) = withContext(ioDispatcher) {
        suspendCoroutine {
            sharedPreferences.edit(commit = true, action = action)
            it.resumeWith(Result.success(Unit))
        }
    }

    private suspend fun getInt(
        key: String,
        default: Int = -1
    ): Int = withContext(ioDispatcher) {
        suspendCoroutine {
            it.resumeWith(Result.success(sharedPreferences.getInt(key, default)))
        }
    }

    private suspend fun getFloat(
        key: String,
        default: Float = -1f
    ) = withContext(ioDispatcher) {
        suspendCoroutine {
            it.resumeWith(Result.success(sharedPreferences.getFloat(key, default)))
        }
    }
}

private const val PLACE_LAT = "place_lat"
private const val PLACE_LON = "place_lon"
private const val AIR_PRESSURE_UNIT = "air_pressure_unit"
private const val PRECIPITATION_UNIT = "precipitation_unit"
private const val TEMPERATURE_UNIT = "temperature_unit"
private const val WIND_UNIT = "wind_unit"