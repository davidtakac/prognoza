package hr.dtakac.prognoza.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hr.dtakac.prognoza.shared.domain.*
import hr.dtakac.prognoza.shared.domain.data.*

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {
    @Provides
    fun provideGetTodayForecastUseCase(
        getSelectedPlaceUseCase: GetSelectedPlace,
        savedForecastGetter: SavedForecastGetter,
        forecastSaver: ForecastSaver,
        forecastProvider: ForecastProvider,
        settingsRepository: SettingsRepository
    ): GetForecast = GetForecast(
        getSelectedPlace = getSelectedPlaceUseCase,
        savedForecastGetter = savedForecastGetter,
        forecastSaver = forecastSaver,
        forecastProvider = forecastProvider,
        settingsRepository = settingsRepository
    )

    @Provides
    fun provideGetSelectedPlaceUseCase(
        settingsRepository: SettingsRepository
    ): GetSelectedPlace = GetSelectedPlace(settingsRepository)

    @Provides
    fun provideSearchPlacesUseCase(
        placeSearcher: PlaceSearcher,
    ): SearchPlaces = SearchPlaces(placeSearcher)

    @Provides
    fun provideGetSavedPlacesUseCase(
        savedPlaceGetter: SavedPlaceGetter
    ): GetSavedPlaces = GetSavedPlaces(savedPlaceGetter)

    @Provides
    fun provideSelectPlaceUseCase(
        placeSaver: PlaceSaver,
        settingsRepository: SettingsRepository
    ): SelectPlace = SelectPlace(
        placeSaver,
        settingsRepository
    )

    @Provides
    fun provideSetTemperatureUnit(
        settingsRepository: SettingsRepository
    ): SetTemperatureUnit = SetTemperatureUnit(settingsRepository)

    @Provides
    fun provideGetTemperatureUnit(
        settingsRepository: SettingsRepository
    ): GetTemperatureUnit = GetTemperatureUnit(settingsRepository)

    @Provides
    fun provideGetAllTemperatureUnits(): GetAllTemperatureUnits = GetAllTemperatureUnits()

    @Provides
    fun provideSetWindUnit(
        settingsRepository: SettingsRepository
    ): SetWindUnit = SetWindUnit(settingsRepository)

    @Provides
    fun provideGetWindUnit(
        settingsRepository: SettingsRepository
    ): GetWindUnit = GetWindUnit(settingsRepository)

    @Provides
    fun provideGetAllWindUnits(): GetAllWindUnits = GetAllWindUnits()

    @Provides
    fun provideSetPrecipitationUnit(
        settingsRepository: SettingsRepository
    ): SetPrecipitationUnit = SetPrecipitationUnit(settingsRepository)

    @Provides
    fun provideGetPrecipitationUnit(
        settingsRepository: SettingsRepository
    ): GetPrecipitationUnit = GetPrecipitationUnit(settingsRepository)

    @Provides
    fun provideGetAllPrecipitationUnits(): GetAllPrecipitationUnits = GetAllPrecipitationUnits()

    @Provides
    fun provideSetPressureUnit(
        settingsRepository: SettingsRepository
    ): SetPressureUnit = SetPressureUnit(settingsRepository)

    @Provides
    fun provideGetPressureUnit(
        settingsRepository: SettingsRepository
    ): GetPressureUnit = GetPressureUnit(settingsRepository)

    @Provides
    fun provideGetAllPressureUnits(): GetAllPressureUnits = GetAllPressureUnits()
}