package hr.dtakac.prognoza.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import hr.dtakac.prognoza.domain.repository.ForecastRepository
import hr.dtakac.prognoza.domain.repository.SettingsRepository
import hr.dtakac.prognoza.domain.usecases.GetSelectedPlace
import hr.dtakac.prognoza.domain.usecases.GetTodayForecast

@Module
@InstallIn(ViewModelComponent::class)
class UseCaseModule {
    @Provides
    @ViewModelScoped
    fun provideGetTodayForecastUseCase(
        getSelectedPlaceUseCase: GetSelectedPlace,
        forecastRepository: ForecastRepository
    ): GetTodayForecast = GetTodayForecast(getSelectedPlaceUseCase, forecastRepository)

    @Provides
    @ViewModelScoped
    fun provideGetSelectedPlaceUseCase(
        settingsRepository: SettingsRepository
    ): GetSelectedPlace = GetSelectedPlace(settingsRepository)
}