package hr.dtakac.prognoza.widget

import android.content.Context
import android.widget.RemoteViews
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.extensions.formatPrecipitationTwoHours
import hr.dtakac.prognoza.uimodel.widget.CurrentConditionsWidgetUiModel

open class MediumCurrentConditionsAppWidgetProvider : SmallCurrentConditionsAppWidgetProvider() {
    override val widgetLayoutId: Int
        get() = R.layout.app_widget_current_conditions_medium

    override fun onSuccess(
        views: RemoteViews,
        context: Context?,
        uiModel: CurrentConditionsWidgetUiModel
    ) {
        super.onSuccess(views, context, uiModel)
        views.setTextViewText(
            R.id.tv_precipitation_forecast,
            context?.formatPrecipitationTwoHours(
                uiModel.precipitationTwoHours,
                uiModel.displayDataInUnit,
                significantPrecipitationColor = false
            )
        )
    }
}