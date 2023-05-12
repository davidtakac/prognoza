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
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {
    @Provides
    @Singleton
    fun providePrognozaSdk(
        @ApplicationContext context: Context,
        @UserAgent userAgent: String
    ): PrognozaSdk = PrognozaSdkFactory(context, userAgent).create()

    @Provides
    fun provideGetOverviewUseCase(
        prognozaSdk: PrognozaSdk
    ): GetOverview = prognozaSdk.getOverview

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
    ): SelectTemperatureUnit = prognozaSdk.selectTemperatureUnit

    @Provides
    fun provideGetTemperatureUnit(
        prognozaSdk: PrognozaSdk
    ): GetSelectedTemperatureUnit = prognozaSdk.getSelectedTemperatureUnit

    @Provides
    fun provideGetAllTemperatureUnits(
        prognozaSdk: PrognozaSdk
    ): GetAvailableTemperatureUnits = prognozaSdk.getAvailableTemperatureUnits

    @Provides
    fun provideSetWindUnit(
        prognozaSdk: PrognozaSdk
    ): SelectWindSpeedUnit = prognozaSdk.selectWindSpeedUnit

    @Provides
    fun provideGetWindUnit(
        prognozaSdk: PrognozaSdk
    ): GetSelectedWindSpeedUnit = prognozaSdk.getSelectedWindSpeedUnit

    @Provides
    fun provideGetAllWindUnits(
        prognozaSdk: PrognozaSdk
    ): GetAvailableWindSpeedUnits = prognozaSdk.getAvailableWindSpeedUnits

    @Provides
    fun provideSetPrecipitationUnit(
        prognozaSdk: PrognozaSdk
    ): SelectLengthUnit = prognozaSdk.selectLengthUnit

    @Provides
    fun provideGetPrecipitationUnit(
        prognozaSdk: PrognozaSdk
    ): GetSelectedLengthUnit = prognozaSdk.getSelectedLengthUnit

    @Provides
    fun provideGetAllPrecipitationUnits(
        prognozaSdk: PrognozaSdk
    ): GetAvailableLengthUnits = prognozaSdk.getAvailableLengthUnits

    @Provides
    fun provideSetPressureUnit(
        prognozaSdk: PrognozaSdk
    ): SelectPressureUnit = prognozaSdk.selectPressureUnit

    @Provides
    fun provideGetPressureUnit(
        prognozaSdk: PrognozaSdk
    ): GetSelectedPressureUnit = prognozaSdk.getSelectedPressureUnit

    @Provides
    fun provideGetAllPressureUnits(
        prognozaSdk: PrognozaSdk
    ): GetAvailablePressureUnits = prognozaSdk.getAvailablePressureUnits
}