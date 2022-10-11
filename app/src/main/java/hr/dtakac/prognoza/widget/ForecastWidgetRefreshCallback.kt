package hr.dtakac.prognoza.widget

import android.content.Context
import android.content.Intent
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback

class ForecastWidgetRefreshCallback : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val action = Intent(context, ForecastWidgetReceiver::class.java).apply { action = REFRESH_ACTION }
        context.sendBroadcast(action)
    }

    companion object {
        const val REFRESH_ACTION = "refresh_action"
    }
}