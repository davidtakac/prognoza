package hr.dtakac.prognoza.presentation

import android.content.Context
import androidx.glance.appwidget.updateAll
import dagger.hilt.android.qualifiers.ApplicationContext
import hr.dtakac.prognoza.widget.ForecastWidget
import hr.dtakac.prognoza.widget.ForecastWidgetWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WidgetRefresher @Inject constructor(
    @ApplicationContext
    private val context: Context
) {
    // Scope does not need to be cancelled, WidgetRefresher lives as long
    // as the app. It gets cleared with the process.
    private val scope = CoroutineScope(Dispatchers.Main + Job())

    fun refreshData() {
        ForecastWidgetWorker.updateNow(context)
    }

    fun refreshUi() {
        scope.launch {
            ForecastWidget().updateAll(context)
        }
    }
}