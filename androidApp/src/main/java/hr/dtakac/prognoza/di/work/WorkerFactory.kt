package hr.dtakac.prognoza.di.work

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import javax.inject.Inject
import javax.inject.Provider

class WorkerFactory @Inject constructor(
  private val workers: Map<Class<out ListenableWorker>, @JvmSuppressWildcards Provider<ChildWorkerFactory>>
) : WorkerFactory() {
  override fun createWorker(
    appContext: Context,
    workerClassName: String,
    workerParameters: WorkerParameters
  ): ListenableWorker? {
    var workerFactoryProvider: Provider<ChildWorkerFactory>? = null
    for ((clazz, provider) in workers) {
      if (clazz.name == workerClassName) {
        workerFactoryProvider = provider
        break
      }
    }
    return workerFactoryProvider?.get()?.create(appContext, workerParameters)
  }
}