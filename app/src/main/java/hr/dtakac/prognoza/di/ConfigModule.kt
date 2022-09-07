package hr.dtakac.prognoza.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hr.dtakac.prognoza.BuildConfig
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
}