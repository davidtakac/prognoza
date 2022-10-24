package hr.dtakac.prognoza.ui

import android.content.Context
import hr.dtakac.prognoza.widget.ForecastWidgetWorker

class WidgetRefresher(private val appContext: Context) {
    fun refresh() = ForecastWidgetWorker.updateNow(context = appContext)
}