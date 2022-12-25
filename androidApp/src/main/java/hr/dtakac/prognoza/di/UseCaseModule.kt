package hr.dtakac.prognoza.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import hr.dtakac.prognoza.shared.PrognozaSdk
import hr.dtakac.prognoza.shared.domain.*
import hr.dtakac.prognoza.shared.platform.PrognozaSdkFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {
    @Provides
    @Singleton
    fun providePrognozaSdk(
        @ApplicationContext context: Context,
        @Named("user_agent") userAgent: String
    ): PrognozaSdk = PrognozaSdkFactory(context, userAgent).create()

    @Provides
    fun provideGetForecastUseCase(
        prognozaSdk: PrognozaSdk
    ): GetForecast = prognozaSdk.getForecast

    @Provides
    @Named("frugal")
    fun provideGetForecastFrugalUseCase(
        prognozaSdk: PrognozaSdk
    ): GetForecast = prognozaSdk.getForecastFrugal

    @Provides
    fun provideGetSelectedPlaceUseCase(
        prognozaSdk: PrognozaSdk
    ): GetSelectedPlace = prognozaSdk.getSelectedPlace

    @Provides
    fun provideSearchPlacesUseCase(
        prognozaSdk: PrognozaSdk
    ): SearchPlaces = prognozaSdk.searchPlaces

    @Provides
    fun provideGetSavedPlacesUseCase(
        prognozaSdk: PrognozaSdk
    ): GetSavedPlaces = prognozaSdk.getSavedPlaces

    @Provides
    fun provideSelectPlaceUseCase(
        prognozaSdk: PrognozaSdk
    ): SelectPlace = prognozaSdk.selectPlace

    @Provides
    fun provideDeleteSavedPlaceUseCase(
        prognozaSdk: PrognozaSdk
    ): DeleteSavedPlace = prognozaSdk.deleteSavedPlace

    @Provides
    fun provideSetTemperatureUnit(
        prognozaSdk: PrognozaSdk
    ): SetTemperatureUnit = prognozaSdk.setTemperatureUnit

    @Provides
    fun provideGetTemperatureUnit(
        prognozaSdk: PrognozaSdk
    ): GetTemperatureUnit = prognozaSdk.getTemperatureUnit

    @Provides
    fun provideGetAllTemperatureUnits(
        prognozaSdk: PrognozaSdk
    ): GetAllTemperatureUnits = prognozaSdk.getAllTemperatureUnits

    @Provides
    fun provideSetWindUnit(
        prognozaSdk: PrognozaSdk
    ): SetWindUnit = prognozaSdk.setWindUnit

    @Provides
    fun provideGetWindUnit(
        prognozaSdk: PrognozaSdk
    ): GetWindUnit = prognozaSdk.getWindUnit

    @Provides
    fun provideGetAllWindUnits(
        prognozaSdk: PrognozaSdk
    ): GetAllWindUnits = prognozaSdk.getAllWindUnits

    @Provides
    fun provideSetPrecipitationUnit(
        prognozaSdk: PrognozaSdk
    ): SetPrecipitationUnit = prognozaSdk.setPrecipitationUnit

    @Provides
    fun provideGetPrecipitationUnit(
        prognozaSdk: PrognozaSdk
    ): GetPrecipitationUnit = prognozaSdk.getPrecipitationUnit

    @Provides
    fun provideGetAllPrecipitationUnits(
        prognozaSdk: PrognozaSdk
    ): GetAllPrecipitationUnits = prognozaSdk.getAllPrecipitationUnits

    @Provides
    fun provideSetPressureUnit(
        prognozaSdk: PrognozaSdk
    ): SetPressureUnit = prognozaSdk.setPressureUnit

    @Provides
    fun provideGetPressureUnit(
        prognozaSdk: PrognozaSdk
    ): GetPressureUnit = prognozaSdk.getPressureUnit

    @Provides
    fun provideGetAllPressureUnits(
        prognozaSdk: PrognozaSdk
    ): GetAllPressureUnits = prognozaSdk.getAllPressureUnits

    @Provides
    fun provideSetForecastProvider(
        prognozaSdk: PrognozaSdk
    ): SetForecastProvider = prognozaSdk.setForecastProvider

    @Provides
    fun provideGetForecastProvider(
        prognozaSdk: PrognozaSdk
    ): GetForecastProvider = prognozaSdk.getForecastProvider

    @Provides
    fun provideGetAllForecastProviders(
        prognozaSdk: PrognozaSdk
    ): GetAllForecastProviders = prognozaSdk.getAllForecastProviders
}