package hr.dtakac.prognoza.ui

import android.content.Context
import hr.dtakac.prognoza.widget.ForecastWidgetReceiver

class WidgetRefresher(
    private val appContext: Context
) {
    fun refresh() {
        ForecastWidgetReceiver.refresh(appContext)
    }
}