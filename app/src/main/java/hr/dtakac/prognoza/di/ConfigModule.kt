package hr.dtakac.prognoza.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hr.dtakac.prognoza.AndroidDotDecimalFormatter
import hr.dtakac.prognoza.AndroidRfc1123Converter
import hr.dtakac.prognoza.AndroidRfc2616LanguageGetter
import hr.dtakac.prognoza.BuildConfig
import hr.dtakac.prognoza.domain.place.Rfc2616LanguageGetter
import hr.dtakac.prognoza.metnorwayforecastprovider.DotDecimalFormatter
import hr.dtakac.prognoza.metnorwayforecastprovider.Rfc1123Converter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ConfigModule {
    @Provides
    @Singleton
    @Named("user_agent")
    fun provideUserAgent(): String = "Prognoza/${BuildConfig.VERSION_NAME}, " +
            "github.com/davidtakac/Prognoza, " +
            "developer.takac@gmail.com"

    @Provides
    @Named("io")
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Named("computation")
    fun provideComputationDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Provides
    fun provideRfc2616LanguageGetter(): Rfc2616LanguageGetter = AndroidRfc2616LanguageGetter()

    @Provides
    fun provideDotDecimalFormatter(): DotDecimalFormatter = AndroidDotDecimalFormatter()

    @Provides
    fun provideRfc1123Converter(): Rfc1123Converter = AndroidRfc1123Converter()
}