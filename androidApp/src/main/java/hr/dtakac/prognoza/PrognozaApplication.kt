package hr.dtakac.prognoza

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager
import dagger.hilt.android.HiltAndroidApp
import hr.dtakac.prognoza.di.work.WorkerFactory
import hr.dtakac.prognoza.presentation.WidgetRefresher
import hr.dtakac.prognoza.shared.platform.initLogging
import javax.inject.Inject

@HiltAndroidApp
class PrognozaApplication : Application() {
    @Inject
    lateinit var widgetRefresher: WidgetRefresher

    @Inject
    lateinit var workerFactory: WorkerFactory

    override fun onCreate() {
        super.onCreate()
        WorkManager.initialize(
            this,
            Configuration.Builder().setWorkerFactory(workerFactory).build()
        )
        // todo: move this to DEBUG conditional once Glance is more stable, this is currently
        //  needed to avoid the "Can't load widget" error that happens on process recreation.
        widgetRefresher.refreshData()
        if (BuildConfig.DEBUG) {
            initLogging()
        }
    }

    override fun onConfigurationChanged(newConfig: android.content.res.Configuration) {
        super.onConfigurationChanged(newConfig)
        widgetRefresher.refreshUi()
    }
}