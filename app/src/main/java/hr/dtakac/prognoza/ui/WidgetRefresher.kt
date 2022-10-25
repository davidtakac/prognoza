package hr.dtakac.prognoza.ui

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import hr.dtakac.prognoza.widget.ForecastWidgetWorker
import javax.inject.Inject

class WidgetRefresher @Inject constructor(
    @ApplicationContext
    private val context: Context
) {
    fun refresh() = ForecastWidgetWorker.updateNow(context)
}