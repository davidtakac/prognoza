package hr.dtakac.prognoza.shared.data.prognoza

import hr.dtakac.prognoza.shared.domain.data.SavedPlaceGetter
import hr.dtakac.prognoza.shared.domain.data.SettingsRepository
import hr.dtakac.prognoza.shared.entity.LengthUnit
import hr.dtakac.prognoza.shared.entity.PressureUnit
import hr.dtakac.prognoza.shared.entity.SpeedUnit
import hr.dtakac.prognoza.shared.entity.TemperatureUnit
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import hr.dtakac.prognoza.shared.entity.Place as PlaceEntity

class DatabaseSettingsRepository(
    private val settingsQueries: SettingsQueries,
    private val savedPlaceGetter: SavedPlaceGetter,
    private val ioDispatcher: CoroutineDispatcher
) : SettingsRepository {
    override suspend fun getPlace(): PlaceEntity? {
        val settings = getSettings()
        return savedPlaceGetter.get(
            latitude = settings.placeLatitude ?: 91.0,
            longitude = settings.placeLongitude ?: 181.0
        )
    }

    override suspend fun setPlace(place: PlaceEntity) {
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

    private suspend fun getSettings(): Get = withContext(ioDispatcher) {
        settingsQueries.get().executeAsOne()
    }
}