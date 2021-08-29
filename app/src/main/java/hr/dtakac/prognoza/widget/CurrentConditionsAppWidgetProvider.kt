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
import hr.dtakac.prognoza.dbmodel.ForecastHour
import hr.dtakac.prognoza.dbmodel.Place
import hr.dtakac.prognoza.extensions.*
import hr.dtakac.prognoza.repomodel.CachedSuccess
import hr.dtakac.prognoza.repomodel.Success
import hr.dtakac.prognoza.repository.forecast.ForecastRepository
import hr.dtakac.prognoza.repository.place.PlaceRepository
import hr.dtakac.prognoza.repository.preferences.PreferencesRepository
import hr.dtakac.prognoza.uimodel.MeasurementUnit
import hr.dtakac.prognoza.uimodel.WEATHER_ICONS
import hr.dtakac.prognoza.uimodel.widget.CurrentConditionsWidgetUiModel
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

abstract class CurrentConditionsAppWidgetProvider : AppWidgetProvider(), KoinComponent {
    abstract val widgetLayoutId: Int

    private val forecastRepository by inject<ForecastRepository>()
    private val preferencesRepository by inject<PreferencesRepository>()
    private val placeRepository by inject<PlaceRepository>()

    abstract fun onSuccess(
        views: RemoteViews,
        context: Context?,
        uiModel: CurrentConditionsWidgetUiModel
    )

    abstract fun onError(
        views: RemoteViews,
        context: Context?
    )

    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        runBlocking {
            val uiModel = getCurrentConditionsWidgetUiModel()
            appWidgetIds?.forEach { appWidgetId ->
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

    private suspend fun getCurrentConditionsWidgetUiModel(): CurrentConditionsWidgetUiModel? {
        val selectedPlaceId = preferencesRepository.getSelectedPlaceId()
        val selectedUnit = preferencesRepository.getSelectedUnit()
        val result = forecastRepository.getTodayForecastHours(selectedPlaceId)
        val selectedPlace = placeRepository.get(selectedPlaceId)
        return if (selectedPlace != null) {
            val hours = when (result) {
                is Success -> {
                    result.hours
                }
                is CachedSuccess -> {
                    result.success.hours
                }
                else -> {
                    null
                }
            }
            hours?.toCurrentConditionsWidgetUiModel(selectedUnit, selectedPlace)
        } else {
            null
        }
    }

    private fun setOnClickOpenApplication(views: RemoteViews, context: Context?) {
        if (BuildConfig.VERSION_CODE < Build.VERSION_CODES.S) {
            val intent = Intent(context, ForecastActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
            views.setOnClickPendingIntent(android.R.id.background, pendingIntent)
        }
    }

    private fun List<ForecastHour>.toCurrentConditionsWidgetUiModel(
        selectedUnit: MeasurementUnit,
        selectedPlace: Place
    ): CurrentConditionsWidgetUiModel {
        val precipitationTwoHours = subList(0, 2).totalPrecipitationAmount()
        val currentHour = get(0)
        return CurrentConditionsWidgetUiModel(
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
            precipitationTwoHours = if (precipitationTwoHours <= 0f) null else precipitationTwoHours,
        )
    }
}