package hr.dtakac.prognoza.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

class ForecastWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = ForecastWidget()

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        ForecastWidgetWorker.updateNextHour(context)
    }

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        ForecastWidgetWorker.updateNow(context)
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        ForecastWidgetWorker.cancel(context)
    }
}