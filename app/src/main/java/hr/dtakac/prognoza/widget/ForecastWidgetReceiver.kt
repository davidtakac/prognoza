package hr.dtakac.prognoza.widget

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

class ForecastWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = ForecastWidget()

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        ForecastWidgetWorker.enqueue(context)
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        ForecastWidgetWorker.cancel(context)
    }
}