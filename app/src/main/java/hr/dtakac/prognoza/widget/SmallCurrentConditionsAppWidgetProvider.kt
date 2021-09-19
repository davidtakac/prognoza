package hr.dtakac.prognoza.widget

import android.content.Context
import android.widget.RemoteViews
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.utils.formatFeelsLike
import hr.dtakac.prognoza.uimodel.widget.CurrentConditionsWidgetUiModel

open class SmallCurrentConditionsAppWidgetProvider : TinyCurrentConditionsAppWidgetProvider() {
    override val widgetLayoutId: Int
        get() = R.layout.app_widget_current_conditions_small
    override val widgetErrorLayoutId: Int
        get() = R.layout.app_widget_current_conditions_small_empty

    override fun onSuccess(
        views: RemoteViews,
        context: Context?,
        uiModel: CurrentConditionsWidgetUiModel
    ) {
        super.onSuccess(views, context, uiModel)
        views.setTextViewText(
            R.id.tv_place,
            uiModel.placeName
        )
        views.setTextViewText(
            R.id.tv_feels_like,
            context?.formatFeelsLike(uiModel.feelsLike, uiModel.displayDataInUnit)
        )
    }
}