package hr.dtakac.prognoza.di.work

import androidx.work.ListenableWorker
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import hr.dtakac.prognoza.widget.ForecastWidgetWorker
import kotlin.reflect.KClass

@MapKey
@Retention(AnnotationRetention.RUNTIME)
annotation class WorkerKey(val value: KClass<out ListenableWorker>)

@Module
@InstallIn(SingletonComponent::class)
interface WorkerModule {
    @Binds
    @IntoMap
    @WorkerKey(ForecastWidgetWorker::class)
    fun bindForecastWidgetWorker(factory: ForecastWidgetWorker.Factory): ChildWorkerFactory
}