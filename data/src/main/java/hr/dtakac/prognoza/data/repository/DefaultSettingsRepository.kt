package hr.dtakac.prognoza.data.repository

import hr.dtakac.prognoza.data.GetSettings
import hr.dtakac.prognoza.data.SettingsQueries
import hr.dtakac.prognoza.domain.place.SavedPlaceGetter
import hr.dtakac.prognoza.domain.settings.SettingsRepository
import hr.dtakac.prognoza.entities.Place
import hr.dtakac.prognoza.entities.forecast.units.LengthUnit
import hr.dtakac.prognoza.entities.forecast.units.PressureUnit
import hr.dtakac.prognoza.entities.forecast.units.SpeedUnit
import hr.dtakac.prognoza.entities.forecast.units.TemperatureUnit
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DefaultSettingsRepository(
    private val settingsQueries: SettingsQueries,
    private val savedPlaceGetter: SavedPlaceGetter,
    private val ioDispatcher: CoroutineDispatcher
) : SettingsRepository {
    override suspend fun getPlace(): Place? {
        val settings = getSettings()
        return savedPlaceGetter.get(
            latitude = settings.placeLatitude ?: 91.0,
            longitude = settings.placeLongitude ?: 181.0
        )
    }

    override suspend fun setPlace(place: Place) {
        settingsQueries.setPlace(
            placeLatitude = place.latitude,
            placeLongitude = place.longitude
        )
    }

    override suspend fun getPressureUnit(): PressureUnit = getSettings().pressureUnit

    override suspend fun setPressureUnit(unit: PressureUnit) {
        settingsQueries.setPressureUnit(unit)
    }

    override suspend fun getPrecipitationUnit(): LengthUnit = getSettings().precipitationUnit

    override suspend fun setPrecipitationUnit(unit: LengthUnit) {
        settingsQueries.setPrecipitationUnit(unit)
    }

    override suspend fun getTemperatureUnit(): TemperatureUnit = getSettings().temperatureUnit

    override suspend fun setTemperatureUnit(unit: TemperatureUnit) {
        settingsQueries.setTemperatureUnit(unit)
    }

    override suspend fun getWindUnit(): SpeedUnit = getSettings().windUnit

    override suspend fun setWindUnit(unit: SpeedUnit) {
        settingsQueries.setWindUnit(unit)
    }

    private suspend fun getSettings(): GetSettings = withContext(ioDispatcher) {
        settingsQueries.getSettings().executeAsOne()
    }
}