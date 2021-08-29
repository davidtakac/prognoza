package hr.dtakac.prognoza.widget

import android.content.Context
import android.widget.RemoteViews
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.extensions.formatTemperatureValue
import hr.dtakac.prognoza.uimodel.widget.CurrentConditionsWidgetUiModel

open class TinyCurrentConditionsAppWidgetProvider : CurrentConditionsAppWidgetProvider() {
    override val widgetLayoutId: Int
        get() = R.layout.app_widget_current_conditions_tiny

    override fun onSuccess(
        views: RemoteViews,
        context: Context?,
        uiModel: CurrentConditionsWidgetUiModel
    ) {
        views.setTextViewText(
            R.id.tv_temperature,
            context?.resources?.formatTemperatureValue(
                uiModel.temperature,
                uiModel.displayDataInUnit
            )
        )
        views.setImageViewResource(
            R.id.iv_weather_icon,
            uiModel.iconResourceId ?: R.drawable.ic_cloud
        )
    }

    override fun onError(views: RemoteViews, context: Context?) {
        TODO("Not yet implemented")
    }
}