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
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import javax.inject.Provider
import kotlin.random.Random

class ForecastWidgetWorker @Inject constructor(
    private val context: Context,
    workerParameters: WorkerParameters,
    private val getForecast: GetForecast
) : CoroutineWorker(context, workerParameters) {
    companion object {
        private const val updateNowName = "update_now"
        private const val updateNextHourName = "update_next_hour"

        fun updateNow(context: Context) {
            WorkManager.getInstance(context).enqueueUniqueWork(
                updateNowName,
                ExistingWorkPolicy.REPLACE,
                getUpdateNowRequest()
            )
        }

        fun updateNextHour(context: Context) {
            WorkManager.getInstance(context).enqueueUniqueWork(
                updateNextHourName,
                ExistingWorkPolicy.KEEP,
                getUpdateNextHourRequest()
            )
        }

        fun cancel(context: Context) {
            WorkManager.getInstance(context).run {
                cancelUniqueWork(updateNowName)
                cancelUniqueWork(updateNextHourName)
            }
        }

        private fun getUpdateNowRequest(): OneTimeWorkRequest =
            OneTimeWorkRequestBuilder<ForecastWidgetWorker>()
                // todo: ForecastWidgetReceiver's onEnabled is called before any glanceIds are
                //  added, which causes the widget update to miss the newly added widget. This
                //  is a workaround. Remove it once the API is fixed.
                .setInitialDelay(Duration.ofSeconds(1L))
                .build()

        private fun getUpdateNextHourRequest(): OneTimeWorkRequest {
            val now = ZonedDateTime.now()
            val untilNextFullHour = Duration.between(now, now.truncatedTo(ChronoUnit.HOURS).plusHours(1L))
            // Required to avoid all instances of the app making a request at the same time and DDoSing the server
            val jitter = Duration.ofSeconds(Random.nextLong(from = 0L, until = 5L * 60L))
            return OneTimeWorkRequestBuilder<ForecastWidgetWorker>()
                .setInitialDelay(untilNextFullHour + jitter)
                .build()
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