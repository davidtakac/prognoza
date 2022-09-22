package hr.dtakac.prognoza.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import hr.dtakac.prognoza.domain.repository.ForecastRepository
import hr.dtakac.prognoza.domain.repository.PlaceRepository
import hr.dtakac.prognoza.domain.repository.SettingsRepository
import hr.dtakac.prognoza.domain.usecase.GetSavedPlaces
import hr.dtakac.prognoza.domain.usecase.GetSelectedPlace
import hr.dtakac.prognoza.domain.usecase.SearchPlaces
import hr.dtakac.prognoza.domain.usecase.SelectPlace
import hr.dtakac.prognoza.domain.usecase.GetForecast

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
}