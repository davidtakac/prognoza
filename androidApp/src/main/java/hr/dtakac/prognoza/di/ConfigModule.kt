package hr.dtakac.prognoza.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ConfigModule {
  @Provides
  @Singleton
  @UserAgent
  fun provideUserAgent(): String {
    val appName = "Prognoza"
    val version = "1"
    val platform = "Android"
    val source = "https://github.com/davidtakac/prognoza"
    val email = "developer.takac@gmail.com"
    return "$appName $version, platform: $platform, source: $source, contact: $email"
  }

  @Provides
  @ComputationDispatcher
  fun provideComputationDispatcher(): CoroutineDispatcher = Dispatchers.Default
}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class UserAgent

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ComputationDispatcher