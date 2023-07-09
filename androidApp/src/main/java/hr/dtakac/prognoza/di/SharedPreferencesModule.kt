package hr.dtakac.prognoza.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class SharedPreferencesModule {
  @Provides
  fun provideSharedPreferences(
    @ApplicationContext context: Context
  ): SharedPreferences = context.getSharedPreferences("prognoza_prefs", Context.MODE_PRIVATE)
}