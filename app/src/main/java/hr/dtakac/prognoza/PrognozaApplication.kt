package hr.dtakac.prognoza

import android.app.Application
import hr.dtakac.prognoza.core.di.coreModule
import hr.dtakac.prognoza.forecast.di.forecastModule
import hr.dtakac.prognoza.places.di.placesModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class PrognozaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@PrognozaApplication)
            modules(
                coreModule,
                forecastModule,
                placesModule
            )
        }
    }
}