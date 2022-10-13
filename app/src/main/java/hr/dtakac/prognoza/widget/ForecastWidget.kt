package hr.dtakac.prognoza.widget

import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.*
import androidx.glance.*
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.*
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.color.dynamicThemeColorProviders
import androidx.glance.layout.*
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import hr.dtakac.prognoza.domain.usecase.GetForecast
import hr.dtakac.prognoza.domain.usecase.GetForecastResult
import hr.dtakac.prognoza.presentation.asGlanceString
import hr.dtakac.prognoza.presentation.forecast.*
import hr.dtakac.prognoza.ui.MainActivity
import kotlinx.coroutines.*
import java.lang.IllegalStateException
import javax.inject.Inject
import kotlin.random.Random

class ForecastWidget : GlanceAppWidget() {

    companion object {
        // https://developer.android.com/develop/ui/views/appwidgets/layouts#anatomy_determining_size
        private val tiny = DpSize(width = 57.dp, height = 102.dp) // 1x1
        private val small = DpSize(width = 130.dp, height = 102.dp) // 2x1
        private val normal = DpSize(width = 130.dp, height = 220.dp) // 2x2
        private val normalWide = DpSize(width = 203.dp, height = 220.dp) // 3x2
        private val normalExtraWide = DpSize(width = 276.dp, height = 220.dp) // 4x2
    }

    override val sizeMode: SizeMode = SizeMode.Responsive(
        setOf(tiny, small, normal, normalWide, normalExtraWide)
    )

    override val stateDefinition: GlanceStateDefinition<*> = PreferencesGlanceStateDefinition

    @Composable
    override fun Content() {
        val colors = dynamicThemeColorProviders()
        val prefs = currentState<Preferences>()
        val size = LocalSize.current

        Box(
            modifier = GlanceModifier
                .appWidgetBackgroundRadius()
                .background(colors.surface)
                .appWidgetBackground()
                .padding(16.dp)
                .fillMaxSize()
                .clickable(actionStartActivity<MainActivity>()),
            contentAlignment = Alignment.Center
        ) {
            val state = ForecastWidgetStateRepository(
                prefs = prefs.toMutablePreferences(),
                gson = Gson()
            ).getState()

            if (state !is ForecastWidgetState.Success) {
                EmptyWidget(colors)
            } else {
                val placeName = state.placeName
                val temperatureUnit = state.temperatureUnit
                val icon = state.description.toDrawableId()
                val currentTemperature = getTemperature(
                    temperature = state.temperature,
                    unit = temperatureUnit
                ).asGlanceString()

                when (size) {
                    tiny -> PlaceAndTempWidget(
                        placeName = placeName,
                        currentTemperature = currentTemperature,
                        colors = colors,
                        placeSize = 14.sp,
                        temperatureSize = 22.sp
                    )
                    small -> PlaceAndTempAndIconWidget(
                        placeName = placeName,
                        currentTemperature = currentTemperature,
                        iconResId = icon,
                        colors = colors,
                        modifier = GlanceModifier.fillMaxSize()
                    )
                    else -> PlaceAndTempAndIconAndHoursWidget(
                        placeName = placeName,
                        currentTemperature = currentTemperature,
                        iconResId = icon,
                        hours = state.hours.take(
                            when (size) {
                                normal -> 3
                                normalWide -> 5
                                normalExtraWide -> 7
                                else -> throw IllegalStateException("Unsupported widget size.")
                            }
                        ),
                        temperatureUnit = temperatureUnit,
                        colors = colors
                    )
                }
            }
        }
    }
}

@AndroidEntryPoint
class ForecastWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = ForecastWidget()

    @Inject
    lateinit var getForecast: GetForecast

    private val scope = MainScope()

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        scheduleNextRefresh(context)
        refreshData(context)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == REFRESH_ACTION) {
            refreshData(context)
        }
    }

    override fun onDisabled(context: Context?) {
        super.onDisabled(context)
        cancelScheduledRefresh(context)
    }

    private fun refreshData(context: Context) {
        scope.launch {
            val result = getForecast()
            val glanceIds = GlanceAppWidgetManager(context).getGlanceIds(ForecastWidget::class.java)

            glanceIds.forEach { glanceId ->
                updateAppWidgetState(
                    context = context,
                    definition = PreferencesGlanceStateDefinition,
                    glanceId = glanceId
                ) { prefs ->
                    val mutablePrefs = prefs.toMutablePreferences()
                    ForecastWidgetStateRepository(
                        prefs = mutablePrefs,
                        gson = Gson()
                    ).setState(result as? GetForecastResult.Success)
                    mutablePrefs
                }
                glanceAppWidget.update(context, glanceId)
            }
        }
    }

    companion object {
        fun refresh(context: Context) {
            context.sendBroadcast(getRefreshIntent(context))
        }
    }
}

private const val REFRESH_ACTION = "refresh_action"

private fun getRefreshIntent(context: Context) = Intent(
    context,
    ForecastWidgetReceiver::class.java
).apply { action = REFRESH_ACTION }

private fun scheduleNextRefresh(context: Context?) {
    val jitter = Random.nextLong(
        0L, 1000L * 60L * 5
    )
    val interval = AlarmManager.INTERVAL_HOUR + jitter
    (context?.getSystemService(Context.ALARM_SERVICE) as? AlarmManager)?.set(
        AlarmManager.ELAPSED_REALTIME,
        SystemClock.elapsedRealtime() + interval,
        getRefreshPendingIntent(context)
    )
}

private fun cancelScheduledRefresh(context: Context?) {
    (context?.getSystemService(Context.ALARM_SERVICE) as? AlarmManager)?.cancel(
        getRefreshPendingIntent(context)
    )
}

private fun getRefreshPendingIntent(context: Context): PendingIntent {
    return PendingIntent.getBroadcast(
        context,
        0,
        getRefreshIntent(context),
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
}