package hr.dtakac.prognoza.widget

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import androidx.work.*
import hr.dtakac.prognoza.di.work.ChildWorkerFactory
import hr.dtakac.prognoza.domain.usecase.GetForecast
import hr.dtakac.prognoza.domain.usecase.GetForecastResult
import timber.log.Timber
import java.time.Duration
import javax.inject.Inject
import javax.inject.Provider

class ForecastWidgetWorker @Inject constructor(
    private val context: Context,
    workerParameters: WorkerParameters,
    private val getForecast: GetForecast
) : CoroutineWorker(context, workerParameters) {
    companion object {
        private const val uniqueWorkName = "forecast_widget_worker"

        fun enqueue(context: Context) {
            val manager = WorkManager.getInstance(context)
            val repeatInterval = Duration.ofHours(1L)
            val requestBuilder = PeriodicWorkRequestBuilder<ForecastWidgetWorker>(repeatInterval)
                // todo: ForecastWidgetReceiver's onEnabled is called before any glanceIds are
                //  added, which causes the widget update to miss the newly added widget. This
                //  is a workaround. Remove it once the API is fixed.
                .setInitialDelay(Duration.ofSeconds(1L))
            val workPolicy = ExistingPeriodicWorkPolicy.REPLACE
            manager.enqueueUniquePeriodicWork(
                uniqueWorkName,
                workPolicy,
                requestBuilder.build()
            )
        }

        fun cancel(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork(uniqueWorkName)
        }
    }

    override suspend fun doWork(): Result {
        val manager = GlanceAppWidgetManager(context)
        val glanceIds = manager.getGlanceIds(ForecastWidget::class.java)
        return try {
            setWidgetState(glanceIds, ForecastWidgetState.Loading)
            val widgetState = when (val result = getForecast()) {
                GetForecastResult.Empty.Error,
                GetForecastResult.Empty.NoSelectedPlace -> ForecastWidgetState.Error
                is GetForecastResult.Success -> ForecastWidgetState.Success(
                    placeName = result.placeName,
                    temperatureUnit = result.temperatureUnit,
                    temperature = result.forecast.current.temperature,
                    lowTemperature = result.forecast.today.lowTemperature,
                    highTemperature = result.forecast.today.highTemperature,
                    description = result.forecast.current.description,
                    hours = result.forecast.today.hourly.map {
                        WidgetHour(
                            dateTime = it.dateTime,
                            temperature = it.temperature,
                            description = it.description
                        )
                    }
                )
            }
            setWidgetState(glanceIds, widgetState)
            Result.success()
        } catch (e: Exception) {
            Timber.e(e)
            setWidgetState(glanceIds, ForecastWidgetState.Unavailable)
            Result.failure()
        }
    }

    private suspend fun setWidgetState(
        glanceIds: List<GlanceId>,
        newState: ForecastWidgetState
    ) {
        glanceIds.forEach { glanceId ->
            updateAppWidgetState(
                context = context,
                definition = ForecastWidgetStateDefinition,
                glanceId = glanceId,
                updateState = { newState }
            )
        }
        ForecastWidget().updateAll(context)
    }

    class Factory @Inject constructor(
        private val getForecast: Provider<GetForecast>
    ) : ChildWorkerFactory {
        override fun create(
            context: Context,
            params: WorkerParameters
        ): ListenableWorker {
            return ForecastWidgetWorker(
                context,
                params,
                getForecast.get()
            )
        }
    }
}