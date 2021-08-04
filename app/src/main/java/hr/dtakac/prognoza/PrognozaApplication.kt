package hr.dtakac.prognoza

import android.app.Application
import hr.dtakac.prognoza.common.prognozaAppModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class PrognozaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@PrognozaApplication)
            modules(prognozaAppModule)
        }
    }
}