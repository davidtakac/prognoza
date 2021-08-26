package hr.dtakac.prognoza.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.activity.ForecastActivity
import hr.dtakac.prognoza.extensions.formatTemperatureValue
import hr.dtakac.prognoza.extensions.shortenedName
import hr.dtakac.prognoza.repomodel.Success
import hr.dtakac.prognoza.repository.forecast.ForecastRepository
import hr.dtakac.prognoza.repository.place.PlaceRepository
import hr.dtakac.prognoza.repository.preferences.PreferencesRepository
import hr.dtakac.prognoza.uimodel.WEATHER_ICONS
import kotlinx.coroutines.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CurrentConditionsAppWidgetProvider : AppWidgetProvider(), KoinComponent {
    private val forecastRepository by inject<ForecastRepository>()
    private val preferencesRepository by inject<PreferencesRepository>()
    private val placeRepository by inject<PlaceRepository>()

    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        runBlocking {
            appWidgetIds?.forEach { appWidgetId ->
                // on widget click, open ForecastActivity
                val pendingIntent: PendingIntent = Intent(context, ForecastActivity::class.java)
                    .let { intent ->
                        PendingIntent.getActivity(context, 0, intent, 0)
                    }
                val views: RemoteViews = RemoteViews(
                    context?.packageName,
                    R.layout.widget_current_conditions
                ).apply {
                    setOnClickPendingIntent(R.id.ll_widget_current_conditions, pendingIntent)
                }
                // update views with new info
                val selectedPlaceId = preferencesRepository.getSelectedPlaceId()
                val result = forecastRepository.getTodayForecastHours(selectedPlaceId)
                val place = placeRepository.get(selectedPlaceId) ?: placeRepository.getDefaultPlace()
                if (result is Success) {
                    val currentHour = result.hours[0]
                    views.setTextViewText(R.id.tv_temperature, context?.resources?.formatTemperatureValue(currentHour.temperature, preferencesRepository.getSelectedUnit()))
                    views.setTextViewText(R.id.tv_place, place.shortenedName)
                    views.setImageViewResource(R.id.iv_weather_icon, WEATHER_ICONS[currentHour.symbolCode]!!.iconResourceId)
                } else {
                    TODO()
                }
                appWidgetManager?.updateAppWidget(appWidgetId, views)
            }
        }
    }


}