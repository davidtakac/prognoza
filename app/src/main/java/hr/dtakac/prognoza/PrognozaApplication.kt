package hr.dtakac.prognoza

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import hr.dtakac.prognoza.ui.WidgetRefresher
import javax.inject.Inject

@HiltAndroidApp
class PrognozaApplication : Application() {
    @Inject
    lateinit var widgetRefresher: WidgetRefresher

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            widgetRefresher.refresh()
        }
    }
}