package hr.dtakac.prognoza.widget.appwidgetprovider

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
import hr.dtakac.prognoza.core.model.database.ForecastTimeSpan
import hr.dtakac.prognoza.core.model.database.Place
import hr.dtakac.prognoza.core.model.repository.CachedSuccess
import hr.dtakac.prognoza.core.model.repository.Success
import hr.dtakac.prognoza.core.repository.forecast.ForecastRepository
import hr.dtakac.prognoza.core.repository.meta.MetaRepository
import hr.dtakac.prognoza.core.repository.place.PlaceRepository
import hr.dtakac.prognoza.core.repository.preferences.PreferencesRepository
import hr.dtakac.prognoza.core.timeprovider.ForecastTimeProvider
import hr.dtakac.prognoza.core.utils.*
import hr.dtakac.prognoza.forecast.activity.ForecastActivity
import hr.dtakac.prognoza.widget.model.CurrentConditionsWidgetUiModel
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.random.Random

abstract class CurrentConditionsAppWidgetProvider : AppWidgetProvider(), KoinComponent {
    abstract val widgetLayoutId: Int
    abstract val widgetErrorLayoutId: Int

    private val forecastRepository by inject<ForecastRepository>()
    private val forecastTimeProvider by inject<ForecastTimeProvider>()
    private val preferencesRepository by inject<PreferencesRepository>()
    private val placeRepository by inject<PlaceRepository>()
    private val metaRepository by inject<MetaRepository>()

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
        setNextUpdateAlarm(context)
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
        val selectedPlace = preferencesRepository.getSelectedPlaceId()?.let {
            placeRepository.get(placeId = it)
        }
        return if (selectedPlace != null) {
            val result = forecastRepository.getForecastTimeSpans(
                start = forecastTimeProvider.todayStart,
                end = forecastTimeProvider.todayEnd,
                place = selectedPlace,
                oldMeta = metaRepository.get(placeId = selectedPlace.id)
            )
            val hours = when (result) {
                is Success -> {
                    result.timeSpans
                }
                is CachedSuccess -> {
                    result.success.timeSpans
                }
                else -> {
                    null
                }
            }
            hours?.toCurrentConditionsWidgetUiModel(
                preferredUnit = preferencesRepository.getSelectedUnit(),
                selectedPlace = selectedPlace
            )
        } else {
            null
        }
    }

    private fun setOnClickOpenApplication(views: RemoteViews, context: Context?) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            val intent = Intent(context, ForecastActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                context,
                REQUEST_CODE_APP_WIDGET_CURRENT_CONDITIONS_INTENT_TRAMPOLINE,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(android.R.id.background, pendingIntent)
        }
    }

    private fun List<ForecastTimeSpan>.toCurrentConditionsWidgetUiModel(
        preferredUnit: hr.dtakac.prognoza.core.model.ui.MeasurementUnit,
        selectedPlace: Place
    ): CurrentConditionsWidgetUiModel {
        val precipitationTwoHours = subList(0, 2).totalPrecipitationAmount()
        val currentHour = get(0)
        return CurrentConditionsWidgetUiModel(
            temperature = currentHour.instantTemperature,
            feelsLike = currentHour.instantTemperature?.let {
                calculateFeelsLikeTemperature(
                    it,
                    currentHour.instantWindSpeed,
                    currentHour.instantRelativeHumidity
                )
            },
            placeName = selectedPlace.shortenedName,
            iconResourceId = hr.dtakac.prognoza.core.model.ui.WEATHER_ICONS[currentHour.symbolCode]?.iconResourceId,
            displayDataInUnit = preferredUnit,
            precipitationTwoHours = if (precipitationTwoHours <= 0) null else precipitationTwoHours,
        )
    }

    private fun setNextUpdateAlarm(context: Context?) {
        val jitter = Random.nextLong(
            0L, 1000L * 60L * 5
        )
        val interval = AlarmManager.INTERVAL_HOUR + AlarmManager.INTERVAL_HALF_HOUR + jitter
        (context?.getSystemService(Context.ALARM_SERVICE) as? AlarmManager)?.set(
            AlarmManager.ELAPSED_REALTIME,
            SystemClock.elapsedRealtime() + interval,
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
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}