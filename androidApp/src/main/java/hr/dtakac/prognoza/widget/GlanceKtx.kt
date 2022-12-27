package hr.dtakac.prognoza.widget

import android.os.Build
import androidx.glance.GlanceModifier
import androidx.glance.ImageProvider
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.color.dynamicThemeColorProviders
import hr.dtakac.prognoza.R

fun GlanceModifier.appWidgetBackgroundShape(): GlanceModifier {
    return if (Build.VERSION.SDK_INT >= 31) {
        this
            .cornerRadius(android.R.dimen.system_app_widget_background_radius)
            .background(dynamicThemeColorProviders().background)
    } else {
        this
            .background(ImageProvider(R.drawable.widget_bg))
    }
}