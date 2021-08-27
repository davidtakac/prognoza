package hr.dtakac.prognoza.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import hr.dtakac.prognoza.BuildConfig
import hr.dtakac.prognoza.activity.ForecastActivity
import hr.dtakac.prognoza.extensions.*
import hr.dtakac.prognoza.repomodel.Success
import hr.dtakac.prognoza.repository.forecast.ForecastRepository
import hr.dtakac.prognoza.repository.place.PlaceRepository
import hr.dtakac.prognoza.repository.preferences.PreferencesRepository
import hr.dtakac.prognoza.uimodel.WEATHER_ICONS
import hr.dtakac.prognoza.uimodel.widget.CurrentConditionsWidgetUiModel
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

abstract class CurrentConditionsAppWidgetProvider : AppWidgetProvider(), KoinComponent {
    protected val forecastRepository by inject<ForecastRepository>()
    protected val preferencesRepository by inject<PreferencesRepository>()
    protected val placeRepository by inject<PlaceRepository>()

    abstract val widgetLayoutId: Int

    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        runBlocking {
            appWidgetIds?.forEach { appWidgetId ->
                val uiModel = getCurrentConditionsWidgetUiModel()
                val remoteViews = RemoteViews(
                    context?.packageName,
                    widgetLayoutId
                )
                if (uiModel != null) {
                    onSuccess(remoteViews, context, uiModel)
                } else {
                    onError(remoteViews, context)
                }
                setOnClickOpenApplication(remoteViews, context)
                appWidgetManager?.updateAppWidget(appWidgetId, remoteViews)
            }
        }
    }

    abstract fun onSuccess(
        views: RemoteViews,
        context: Context?,
        uiModel: CurrentConditionsWidgetUiModel
    )

    abstract fun onError(
        views: RemoteViews,
        context: Context?
    )

    private suspend fun getCurrentConditionsWidgetUiModel(): CurrentConditionsWidgetUiModel? {
        val selectedPlaceId = preferencesRepository.getSelectedPlaceId()
        val result = forecastRepository.getTodayForecastHours(selectedPlaceId)
        return if (result is Success) {
            val selectedPlace =
                placeRepository.get(selectedPlaceId) ?: placeRepository.getDefaultPlace()
            val selectedUnit = preferencesRepository.getSelectedUnit()
            val precipitationTwoHours = result.hours.subList(0, 2).totalPrecipitationAmount()
            val currentHour = result.hours[0]
            CurrentConditionsWidgetUiModel(
                temperature = currentHour.temperature,
                feelsLike = if (currentHour.temperature == null) {
                    null
                } else {
                    calculateFeelsLikeTemperature(
                        currentHour.temperature,
                        currentHour.windSpeed,
                        currentHour.relativeHumidity
                    )
                },
                placeName = selectedPlace.shortenedName,
                iconResourceId = WEATHER_ICONS[currentHour.symbolCode]?.iconResourceId,
                displayDataInUnit = selectedUnit,
                precipitationTwoHours = if (precipitationTwoHours <= 0f) null else precipitationTwoHours
            )
        } else {
            null
        }
    }

    private fun RemoteViews.initialize(context: Context?, uiModel: CurrentConditionsWidgetUiModel) =
        apply {

        }

    private fun setOnClickOpenApplication(views: RemoteViews, context: Context?) {
        if (BuildConfig.VERSION_CODE < Build.VERSION_CODES.S) {
            val intent = Intent(context, ForecastActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
            views.setOnClickPendingIntent(android.R.id.background, pendingIntent)
        }
    }
}