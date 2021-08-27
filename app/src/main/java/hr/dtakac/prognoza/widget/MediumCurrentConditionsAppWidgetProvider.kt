package hr.dtakac.prognoza.widget

import android.content.Context
import android.widget.RemoteViews
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.extensions.formatFeelsLike
import hr.dtakac.prognoza.extensions.formatPrecipitationTwoHours
import hr.dtakac.prognoza.extensions.formatTemperatureValue
import hr.dtakac.prognoza.uimodel.widget.CurrentConditionsWidgetUiModel

class MediumCurrentConditionsAppWidgetProvider : CurrentConditionsAppWidgetProvider() {
    override val widgetLayoutId: Int
        get() = R.layout.app_widget_current_conditions_medium

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
        views.setTextViewText(
            R.id.tv_place,
            uiModel.placeName
        )
        views.setTextViewText(
            R.id.tv_feels_like,
            context?.resources?.formatFeelsLike(uiModel.feelsLike, uiModel.displayDataInUnit)
        )
        views.setTextViewText(
            R.id.tv_precipitation_forecast,
            context?.formatPrecipitationTwoHours(uiModel.precipitationTwoHours, uiModel.displayDataInUnit)
        )
    }

    override fun onError(views: RemoteViews, context: Context?) {
        TODO("Not yet implemented")
    }
}