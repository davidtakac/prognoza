package hr.dtakac.prognoza.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hr.dtakac.prognoza.BuildConfig
import hr.dtakac.prognoza.platform.AndroidRfc1123UtcDateTimeParser
import hr.dtakac.prognoza.shared.platform.Rfc1123UtcDateTimeParser
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
    fun provideRfc1123UtcDateTimeParser(): Rfc1123UtcDateTimeParser = AndroidRfc1123UtcDateTimeParser()
}