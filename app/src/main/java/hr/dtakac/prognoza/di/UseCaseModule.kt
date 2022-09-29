package hr.dtakac.prognoza.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import hr.dtakac.prognoza.domain.repository.ForecastRepository
import hr.dtakac.prognoza.domain.repository.PlaceRepository
import hr.dtakac.prognoza.domain.repository.SettingsRepository
import hr.dtakac.prognoza.domain.usecase.*

@Module
@InstallIn(ViewModelComponent::class)
class UseCaseModule {
    @Provides
    @ViewModelScoped
    fun provideGetTodayForecastUseCase(
        getSelectedPlaceUseCase: GetSelectedPlace,
        forecastRepository: ForecastRepository,
        settingsRepository: SettingsRepository
    ): GetForecast = GetForecast(getSelectedPlaceUseCase, forecastRepository, settingsRepository)

    @Provides
    @ViewModelScoped
    fun provideGetSelectedPlaceUseCase(
        settingsRepository: SettingsRepository
    ): GetSelectedPlace = GetSelectedPlace(settingsRepository)

    @Provides
    @ViewModelScoped
    fun provideSearchPlacesUseCase(
        placeRepository: PlaceRepository
    ): SearchPlaces = SearchPlaces(placeRepository)

    @Provides
    @ViewModelScoped
    fun provideGetSavedPlacesUseCase(
        placeRepository: PlaceRepository
    ): GetSavedPlaces = GetSavedPlaces(placeRepository)

    @Provides
    @ViewModelScoped
    fun provideSelectPlaceUseCase(
        placeRepository: PlaceRepository,
        settingsRepository: SettingsRepository
    ): SelectPlace = SelectPlace(
        placeRepository,
        settingsRepository
    )

    @Provides
    @ViewModelScoped
    fun provideSetTemperatureUnit(
        settingsRepository: SettingsRepository
    ): SetTemperatureUnit = SetTemperatureUnit(settingsRepository)

    @Provides
    @ViewModelScoped
    fun provideGetTemperatureUnit(
        settingsRepository: SettingsRepository
    ): GetTemperatureUnit = GetTemperatureUnit(settingsRepository)

    @Provides
    @ViewModelScoped
    fun provideGetAllTemperatureUnits(): GetAllTemperatureUnits = GetAllTemperatureUnits()

    @Provides
    @ViewModelScoped
    fun provideSetWindUnit(
        settingsRepository: SettingsRepository
    ): SetWindUnit = SetWindUnit(settingsRepository)

    @Provides
    @ViewModelScoped
    fun provideGetWindUnit(
        settingsRepository: SettingsRepository
    ): GetWindUnit = GetWindUnit(settingsRepository)

    @Provides
    @ViewModelScoped
    fun provideGetAllWindUnits(): GetAllWindUnits = GetAllWindUnits()
}