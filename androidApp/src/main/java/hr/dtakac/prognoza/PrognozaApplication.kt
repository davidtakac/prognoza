package hr.dtakac.prognoza

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager
import dagger.hilt.android.HiltAndroidApp
import hr.dtakac.prognoza.di.work.WorkerFactory
import hr.dtakac.prognoza.shared.platform.initLogging
import javax.inject.Inject

@HiltAndroidApp
class PrognozaApplication : Application() {
    // Will probably need for new widgets
    /*@Inject
    lateinit var workerFactory: WorkerFactory*/

    override fun onCreate() {
        super.onCreate()
        /*WorkManager.initialize(
            this,
            Configuration.Builder().setWorkerFactory(workerFactory).build()
        )*/
        if (BuildConfig.DEBUG) {
            initLogging()
        }
    }
}