package hr.dtakac.prognoza.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hr.dtakac.prognoza.BuildConfig
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
    fun provideUserAgent(): String {
        val appName = "Prognoza"
        val version = BuildConfig.VERSION_NAME
        val platform = "Android"
        val source = "https://github.com/davidtakac/prognoza"
        val email = "developer.takac@gmail.com"
        return "$appName $version, platform: $platform, source: $source, contact: $email"
    }

    @Provides
    @Named("computation")
    fun provideComputationDispatcher(): CoroutineDispatcher = Dispatchers.Default
}