package hr.dtakac.prognoza.widget

import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.SystemClock
import android.widget.RemoteViews
import hr.dtakac.prognoza.ACTION_APP_WIDGET_CURRENT_CONDITIONS_UPDATE
import hr.dtakac.prognoza.BuildConfig
import hr.dtakac.prognoza.REQUEST_CODE_APP_WIDGET_CURRENT_CONDITIONS_INTENT_TRAMPOLINE
import hr.dtakac.prognoza.REQUEST_CODE_APP_WIDGET_CURRENT_CONDITIONS_UPDATE
import hr.dtakac.prognoza.activity.ForecastActivity
import hr.dtakac.prognoza.dbmodel.ForecastHour
import hr.dtakac.prognoza.dbmodel.Place
import hr.dtakac.prognoza.extensions.calculateFeelsLikeTemperature
import hr.dtakac.prognoza.extensions.shortenedName
import hr.dtakac.prognoza.extensions.totalPrecipitationAmount
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
import kotlin.random.Random

abstract class CurrentConditionsAppWidgetProvider : AppWidgetProvider(), KoinComponent {
    abstract val widgetLayoutId: Int
    abstract val widgetErrorLayoutId: Int

    private val forecastRepository by inject<ForecastRepository>()
    private val preferencesRepository by inject<PreferencesRepository>()
    private val placeRepository by inject<PlaceRepository>()

    abstract fun onSuccess(
        views: RemoteViews,
        context: Context?,
        uiModel: CurrentConditionsWidgetUiModel
    )

    protected open fun onError(
        views: RemoteViews,
        context: Context?
    ) {
    }

    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
        setOnUpdateAlarm(context)
    }

    override fun onDisabled(context: Context?) {
        super.onDisabled(context)
        cancelOnUpdateAlarm(context)
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        if (intent?.action == ACTION_APP_WIDGET_CURRENT_CONDITIONS_UPDATE && context != null) {
            val componentName = ComponentName(context, this::class.java)
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(componentName)
            onUpdate(context, appWidgetManager, appWidgetIds)
        }
    }

    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        setOnUpdateAlarm(context)
        val uiModel = runBlocking { getCurrentConditionsWidgetUiModel() }
        appWidgetIds?.forEach { appWidgetId ->
            val remoteViews: RemoteViews?
            if (uiModel != null) {
                remoteViews = RemoteViews(
                    context?.packageName,
                    widgetLayoutId
                )
                onSuccess(remoteViews, context, uiModel)
            } else {
                remoteViews = RemoteViews(
                    context?.packageName,
                    widgetErrorLayoutId
                )
                onError(remoteViews, context)
            }
            setOnClickOpenApplication(remoteViews, context)
            appWidgetManager?.updateAppWidget(appWidgetId, remoteViews)
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
            val pendingIntent = PendingIntent.getActivity(
                context,
                REQUEST_CODE_APP_WIDGET_CURRENT_CONDITIONS_INTENT_TRAMPOLINE,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
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

    private fun setOnUpdateAlarm(context: Context?) {
        val jitter = Random.nextLong(
            0L, 1000L * 60L * 5
        )
        val interval = AlarmManager.INTERVAL_HOUR + AlarmManager.INTERVAL_HALF_HOUR + jitter
        (context?.getSystemService(Context.ALARM_SERVICE) as? AlarmManager)?.setInexactRepeating(
            AlarmManager.ELAPSED_REALTIME,
            SystemClock.elapsedRealtime() + interval,
            interval,
            getAlarmPendingIntent(context)
        )
    }

    private fun cancelOnUpdateAlarm(context: Context?) {
        (context?.getSystemService(Context.ALARM_SERVICE) as? AlarmManager)?.cancel(
            getAlarmPendingIntent(context)
        )
    }

    private fun getAlarmPendingIntent(context: Context): PendingIntent {
        val alarmIntent = Intent(context, this::class.java).apply {
            action = ACTION_APP_WIDGET_CURRENT_CONDITIONS_UPDATE
        }
        return PendingIntent.getBroadcast(
            context,
            REQUEST_CODE_APP_WIDGET_CURRENT_CONDITIONS_UPDATE,
            alarmIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }
}